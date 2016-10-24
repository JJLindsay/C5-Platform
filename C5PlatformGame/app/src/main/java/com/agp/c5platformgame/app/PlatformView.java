package com.agp.c5platformgame.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
 */
public class PlatformView extends SurfaceView implements Runnable
{
    private final String TAG = PlatformView.class.getSimpleName();
    private boolean mDebugging = true;
    private volatile boolean mRunning;  //volatile is not to be used lightly
    private Thread mGameThread = null;

    //for drawing
    private Paint mPaint;

    //Canvas is local for now but later it will go beyond draw()
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

    public PlatformView(Context context, int sceenWidth, int screenHeight)
    {
        super(context);
        this.mContext = context;

        mOurHolder = getHolder();
        mPaint = new Paint();
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
    }

    public void draw()
    {
        if (mOurHolder.getSurface().isValid())
        {
            //lock the area of memory where we will be drawing to
            mCanvas = mOurHolder.lockCanvas();

            //blot out the last frame with any color. Higher alpha
            //means less transparency.
            mPaint.setColor(Color.argb(255, 0, 0, 255));
            mCanvas.drawColor(Color.argb(255, 0, 0, 255));

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
}
