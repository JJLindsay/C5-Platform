package com.agp.c5platformgame.app;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 *
 *The main activity. This is where it all starts.
 */
public class PlatformActivity extends Activity
{
    private PlatformView mPlatformView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //store screen (or display) details
        Display display = getWindowManager().getDefaultDisplay();

        //store the screen size in the x,y of Point
        Point resolution = new Point();
        display.getSize(resolution);

        //Create a surfaceView object
        mPlatformView = new PlatformView(this, resolution.x, resolution.y);

        //Defer view to the surfaceView
        setContentView(mPlatformView);
    }

    public void onResume()
    {
        super.onResume();
        mPlatformView.resume();
    }

    public void onPause()
    {
        super.onPause();
        mPlatformView.pause();
    }

}
