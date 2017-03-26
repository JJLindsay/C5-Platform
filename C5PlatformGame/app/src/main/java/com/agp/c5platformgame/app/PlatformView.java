package com.agp.c5platformgame.app;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Map;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 * Finds the next occurrence of the currently selected text = ctrl + F3
 *
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
    private SoundManager mSoundManager;

    public PlatformView(Context context, int screenWidth, int screenHeight)
    {
        super(context);
        this.mContext = context;

        mOurHolder = getHolder();
        mPaint = new Paint();
        mViewport = new Viewport(screenWidth, screenHeight);

        mSoundManager = new SoundManager();
        mSoundManager.loadSound(context);


        //load the first level and provide the player's location
        loadLevel("LevelCave", 15, 2); //could be (10, 2)
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
        final int SIDE_COLLISION = 1;
        final int FEET_COLLISION = 2;

        for (GameObject gameObject : mLevelManager.gameObjects())
        {
            if (gameObject.isActive())
            {
                //clip anything off screen
                if (!mViewport.clipObjects(gameObject.getWorldLocation().getX(), gameObject.getWorldLocation().getY(), gameObject.getWidth(), gameObject.getHeight()))
                {
                    gameObject.setVisible(true);

                    //check player collisions
                    int hit = mLevelManager.getPlayer().checkCollisions(gameObject.getRectangleHitBox());
                    if (hit > 0)
                    {
                        //collision occurred
                        switch (gameObject.getType())
                        {
                            default:
                                if (SIDE_COLLISION == hit) //left or right
                                {
                                    mLevelManager.getPlayer().setXVelocity(0);
                                    mLevelManager.getPlayer().setPressingRight(false);
                                }
                                if (FEET_COLLISION == hit)  //feet
                                {
                                    mLevelManager.getPlayer().setFalling(false);
                                }
                                break;
                        }
                    }

                    if (mLevelManager.isPlaying())
                    {
                        // run any un-clipped updates
                        gameObject.update(mFPS, mLevelManager.getGravity());
                    }
                }
                else
                {
                    gameObject.setVisible(false);
                    //now draw() will ignore them
                }
            }
        }

        //Keep the "camera"/viewport centered on the player while falling
        if (mLevelManager.isPlaying())
        {
            //reset the players location as the center of the viewport
            mViewport.setWorldCenterPoint(mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex)
                    .getWorldLocation().getX(), mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getWorldLocation().getY());
        }
    }

    private void draw()
    {
        final int SOLID = 0xFF;
        if (mOurHolder.getSurface().isValid())
        {
            //lock the area of memory where drawing will take place
            mCanvas = mOurHolder.lockCanvas();

            //blot out the last frame with any color. Higher alpha means less transparency.
            //0xFF = (16^1)* f + (16^0)* f = (16^1)*15 + (16^0)*15 = 240 + 15 = 255
            mPaint.setColor(Color.argb(SOLID, 0, SOLID, 0));  // used for canvas.drawBitmap(...)
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
                    //Log.d(TAG, "Checking visible: " + gObject.isVisible() + " and checking z location is current layer: " + (gObject.getWorldLocation().z == layer));
                    if (gObject.isVisible() && gObject.getWorldLocation().getZ() == layer)
                    {
                        //get the real device position of the gameObjects. Covert from gameObject meters to pixels.
                        toScreen2d.set(mViewport.worldToScreen(gObject.getWorldLocation().getX(), gObject.getWorldLocation().getY(), gObject.getWidth(), gObject.getHeight()));

                        if (gObject.isAnimated())
                        {
                            //get the next frame of the bitmap and rotate if necessary
                            if (gObject.getFacing() == 1)
                            {
                                //rotate
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1, 1);
                                Rect rect = gObject.getRectToDraw(System.currentTimeMillis());
                                Bitmap bitmap = Bitmap.createBitmap(
                                        mLevelManager.getBitmapsArray()[mLevelManager.getBitmapIndex(gObject.getType())],
                                        rect.left,
                                        rect.top,
                                        rect.width(),
                                        rect.height(),
                                        flipper,
                                        true
                                );
                                mCanvas.drawBitmap(bitmap, toScreen2d.left, toScreen2d.top, mPaint);
                            }
                            else
                            {
                                //draw it the regular way round
                                mCanvas.drawBitmap(
                                        mLevelManager.getBitmapsArray()[mLevelManager.getBitmapIndex(gObject.getType())],
                                        gObject.getRectToDraw(System.currentTimeMillis()),
                                        toScreen2d,
                                        mPaint
                                );
                            }
                        }
                        else //just draw the whole bitmap
                        {
                            //draw the appropriate bitmap, at the appropriate pixels (x,y) and paint
                            //drawBitmap(Bitmap, left, top, paint)
                            // what to paint, (x,y) coordinate, and Paint which holds the style and color information abt how to draw geometries, text and bitmaps
                            mCanvas.drawBitmap(
                                    mLevelManager.getBitmapsArray()[mLevelManager.getBitmapIndex(gObject.getType())],
                                    toScreen2d.left,
                                    toScreen2d.top,
                                    mPaint
                            );
                        }
                    }
                }
            }

            //for debugging
            if (mDebugging)
            {
                mPaint.setTextSize(16);
                mPaint.setTextAlign(Paint.Align.LEFT);
                //0xFF = (16^1)* f + (16^0)* f = (16^1)*15 + (16^0)*15 = 240 + 15 = 255
                mPaint.setColor(Color.argb(SOLID, SOLID, SOLID, SOLID));
                mCanvas.drawText("fps:" + mFPS, 10, 60, mPaint);

                mCanvas.drawText("num objects:" + mLevelManager.gameObjects().size(), 10, 80, mPaint);

                mCanvas.drawText("num clipped:" + mViewport.getNumClipped(), 10, 100, mPaint);

                mCanvas.drawText("playerX:" + mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getWorldLocation().getX(), 10, 120, mPaint);

                mCanvas.drawText("PlayerY:" + mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getWorldLocation().getY(), 10, 140, mPaint);

                mCanvas.drawText("Gravity:" + mLevelManager.getGravity(), 10, 160, mPaint);

                mCanvas.drawText("X-Velocity:" + mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getXVelocity(), 10, 180, mPaint);

                mCanvas.drawText("Y-Velocity:" + mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getYVelocity(), 10, 200, mPaint);

                //for reset the number of clipped objects each frame
                mViewport.resetNumClipped();
            }

            //draw buttons
            //0xFF = (16^1)* f + (16^0)* f = (16^1)*15 + (16^0)*15 = 240 + 15 = 255
            mPaint.setColor(Color.argb(80,SOLID,SOLID,SOLID));
            Map<String, Rect> buttonsToDraw = mInputController.getButtons();

            //iterate over buttons
            for (Map.Entry<String, Rect> entry : buttonsToDraw.entrySet())
            {
                //rectF is a rectangle that takes floating points
                RectF rf = new RectF(entry.getValue().left, entry.getValue().top, entry.getValue().right, entry.getValue().bottom);
                mCanvas.drawRoundRect(rf, 15f, 15f, mPaint);

                //paint button text and size
                mPaint.setTextSize(20);
                mCanvas.drawText(entry.getKey(), (float) (entry.getValue().left + 60), (float) (entry.getValue().top + 60), mPaint);
            }

            //draw paused text
            if (!mLevelManager.isPlaying())
            {
                //paint alignment
                mPaint.setTextAlign(Paint.Align.CENTER);
                //paint color
                mPaint.setColor(Color.argb(SOLID, SOLID, SOLID, SOLID));

                //paint text size
                mPaint.setTextSize(120);
                //canvas word and location
                mCanvas.drawText("Paused", mViewport.getScreenWidth() / 2, mViewport.getScreenHeight() / 2, mPaint);
            }


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
            //Pauses the normal thread until
            //mGameThread has terminated and then normal/main thread continues
            mGameThread.join();
        } catch (InterruptedException exc)
        {
            Log.e(TAG, "ERROR: failed to pause thread");
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
        //create a new Level
        // pass in a context, screen details, level name, player location
        mLevelManager = new LevelManager(mContext, mViewport.getPixelsPerMeterX(), mViewport.getScreenWidth(), mInputController, level, px, py);

        mInputController = new InputController(mViewport.getScreenWidth(), mViewport.getScreenHeight());

        // set the player's location as the center of the World
        mViewport.setWorldCenterPoint(mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getWorldLocation().getX(), mLevelManager.gameObjects().get(mLevelManager.mPlayerIndex).getWorldLocation().getY());
    }

    /**
     * trigger by the AndroidUI and not within my control,
     * therefore, its important to make sure the levelmanager has been initialized before working
     * with it
     *
     * @param motionEvent the event that triggered the call
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        if (mLevelManager != null)
        {
            mInputController.handleInput(motionEvent, mLevelManager, mSoundManager, mViewport);
        }

        return true;
    }
}