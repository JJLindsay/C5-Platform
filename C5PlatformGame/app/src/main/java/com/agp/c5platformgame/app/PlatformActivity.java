package com.agp.c5platformgame.app;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

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
 * The main activity. This is where it all starts.
 */
public class PlatformActivity extends Activity
{
    private PlatformView mPlatformView;

    /**
     * Called when opening for the first time or after it has been destroyed and opened again.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //store device screen/display details
        Display display = getWindowManager().getDefaultDisplay();

        //store the device's screen size in the x,y of Point
        Point resolution = new Point();
        display.getSize(resolution);

        //Create a surfaceView object which allows the view to be locked, erased, drawn on, and unlocked repeatedly.
        mPlatformView = new PlatformView(this, resolution.x, resolution.y);

        //Defer view to the surfaceView
        setContentView(mPlatformView);
    }

    /**
     * Called when this app is returned to the foreground.
     */
    public void onResume()
    {
        super.onResume();
        mPlatformView.resume();
    }

    /**
     * Called when another app has moved to the foreground
     */
    public void onPause()
    {
        super.onPause();
        mPlatformView.pause();
    }
}