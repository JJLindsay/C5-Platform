package com.agp.c5platformgame.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 * <p/>
 * A placeholder fragment containing a simple view.
 * This class updates / refreshes the SurfaceView
 * Here is where draw, update, and load level is performed.
 */
public class PlatformView extends SurfaceView implements Runnable
{
    private final String TAG = PlatformView.class.getSimpleName();
    private boolean mDebugging = true;
    private volatile boolean mRunning;  //volatile is not to be used lightly
    private Thread mGameThread = null;

    //for drawing
    private Paint mPaint;

    // NOTE: Canvas could be local -- for now -- but later it will go beyond draw()
    // The Canvas class holds the "draw" calls. To draw something, you need 4 basic components:
    // A Bitmap to hold the pixels, a Canvas to host the draw calls (writing into the bitmap), a
    // drawing primitive (e.g. Rect, Path, text, Bitmap), and paint (to describe the colors and styles
    // for the drawing).
    private Canvas mCanvas;
    private SurfaceHolder mOurHolder;

    public Context mContext;
    public long mStartFrameTime;
    public long mTimeThisFrame;
    public long mFPS;

    //the game engine classes
    private LevelManager mLevelManager;
    private Viewport mViewport;
    public InputController mInputController;

    public PlatformView(Context context, int screenWidth, int screenHeight)
    {
        super(context);
        this.mContext = context;

        mOurHolder = getHolder();
        mPaint = new Paint();
        mViewport = new Viewport(screenWidth, screenHeight);

        //load the first level and provide the player's location
        loadLevel("LevelCave", 15, 2);
    }


    @Override
    public void run()
    {
        while (mRunning)
        {
            mStartFrameTime = System.currentTimeMillis();

            update();
            draw();

            //knowing the FPS makes it possible to time the animations and movement.
            mTimeThisFrame = System.currentTimeMillis() - mStartFrameTime;
            if (mTimeThisFrame >= 1)
            {
                mFPS = 1000 / mTimeThisFrame;
            }


        }
    }

    public void update()
    {
        //TODO

        for (GameObject gObject : mLevelManager.gameObjects())
        {
            if (gObject.isActive())
            {
                //clip anything off screen
                if (!mViewport.clipObjects(gObject.getWorldLocation().x, gObject.getWorldLocation().y, gObject.getWidth(), gObject.getHeight()))
                {
                    gObject.setVisible(true);
                }
                else
                {
                    gObject.setVisible(false);
                    //now draw() will ignore them
                }
            }
        }
    }

    private void draw()
    {
        if (mOurHolder.getSurface().isValid())
        {
            //lock the area of memory where drawing will take place
            mCanvas = mOurHolder.lockCanvas();

            //blot out the last frame with any color. Higher alpha means less transparency.
            mPaint.setColor(Color.argb(255, 0, 255, 0));  // used for canvas.drawBitmap(...)
            mCanvas.drawColor(Color.argb(255, 0, 0, 255));  // Fill the entire canvas' bitmap with the specified ARGB color

            //draw all the gameObjects
            Rect toScreen2d = new Rect();

            //draw one layer at a time
            //Log.d(TAG, "Entering layer loop");  //DEBUGGING
            for (int layer = -1; layer <= 1; layer++)
            {
                //Log.d(TAG, "Inside layer loop");  //DEBUGGING
                for (GameObject gObject : mLevelManager.gameObjects())  //draw all objects that are on this layer
                {
                    //Log.d(TAG, "Bitmap name is: " + gObject.getBitmapName());  //DEBUGGING
                    //only draw if visible and on this layer
                    Log.d(TAG, "Checking visible: " + gObject.isVisible() + " and checking z location is current layer: " + (gObject.getWorldLocation().z == layer));
                    //if (gObject.isVisible() && gObject.getWorldLocation().z == layer)
                    {
                        //get the real device position of the gameObjects. Covert from gameObject meters to pixels.
                        toScreen2d.set(mViewport.worldToScreen(gObject.getWorldLocation().x, gObject.getWorldLocation().y, gObject.getWidth(), gObject.getHeight()));

                        //draw the appropriate bitmap, at the appropriate pixels (x,y) and paint
                        //drawBitmap(Bitmap, left, top, paint)
                        // what to paint, (x,y) coordinate, and Paint which holds the style and color information abt how to draw geometries, text and bitmaps
                        mCanvas.drawBitmap(mLevelManager.getBitmapsArray()[mLevelManager.getBitmapIndex(gObject.getType())], toScreen2d.left, toScreen2d.top, mPaint);
                    }
                }
            }

            //for debugging
            if (mDebugging)
            {
                mPaint.setTextSize(16);
                mPaint.setTextAlign(Paint.Align.LEFT);
                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mCanvas.drawText("fps:" + mFPS, 10, 60, mPaint);

                mCanvas.drawText("num objects:" + mLevelManager.gameObjects().size(), 10, 80, mPaint);

                mCanvas.drawText("num clipped:" + mViewport.getNumClipped(), 10, 100, mPaint);

                mCanvas.drawText("playerX:" + mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getWorldLocation().x, 10, 120, mPaint);

                mCanvas.drawText("PlayerY:" + mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getWorldLocation().y, 10, 140, mPaint);

                //for reset the number of clipped objects each frame
                mViewport.resetNumClipped();
            }

            //TODO - add more code later

            //Unlock and draw canvas
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }

    }

    //when the game is being paused
    public void pause()
    {
        mRunning = false;

        try
        {
            //Wait for thread to die then continue with main thread
            mGameThread.join();
        } catch (InterruptedException exc)
        {
            Log.e("error", "failed to pause thread");
        }
    }

    /**
     * When the game is resuming
     */
    public void resume()
    {
        mRunning = true;
        //create a new thread and pass in our runnable 'this' class
        mGameThread = new Thread(this);
        //Starting thread will call our run function
        mGameThread.start();
    }

    public void loadLevel(String level, int px, int py)
    {
//        mLevelManager = null;  NEEDED???

        //create a new Level
        // pass in a context, screen details, level name, player location
        mLevelManager = new LevelManager(mContext, mViewport.getPixelsPerMeterX(), mViewport.getScreenWidth(), mInputController, level, px, py);

        mInputController = new InputController(mViewport.getScreenWidth(), mViewport.getScreenHeight());

        // set the player's location as the center of the World
        mViewport.setWorldCenterPoint(mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getWorldLocation().x, mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getWorldLocation().y);
    }
}