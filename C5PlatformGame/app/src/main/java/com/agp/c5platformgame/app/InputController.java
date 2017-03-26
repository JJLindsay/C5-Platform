package com.agp.c5platformgame.app;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.HashMap;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 * Finds the next occurrence of the currently selected text = ctrl + F3
 *
 * This class creates and manages the input buttons.
 */
public class InputController
{
    private final static String TAG = InputController.class.getSimpleName();
    private Rect mLeft, mRight, mJump, mShoot, mPause;

    public InputController(int screenWidth, int screenHeight)
    {
        //configure the player buttons
        int buttonWidth = screenWidth / 8;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;

        mLeft = new Rect(buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth,
                screenHeight - buttonPadding);

        mRight = new Rect(buttonWidth + buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth + buttonPadding + buttonWidth,
                screenHeight - buttonPadding);

        mJump = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding - buttonHeight - buttonPadding);

        mShoot = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding);

        mPause = new Rect(screenWidth - buttonPadding - buttonWidth,
                buttonPadding,
                screenWidth - buttonPadding,
                buttonPadding + buttonHeight);
    }


    public void handleInput(MotionEvent motionEvent, LevelManager levelManager, SoundManager sound, Viewport viewport)
    {
        int pointerCount = motionEvent.getPointerCount();

        for (int i = 0; i < pointerCount; i++)
        {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);

            if (levelManager.isPlaying())
            {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
                {
                    case MotionEvent.ACTION_DOWN:
                        if (mRight.contains(x,y))
                        {
                            levelManager.getPlayer().setPressingRight(true);
                            levelManager.getPlayer().setPressingLeft(false);
                        }
                        else if (mLeft.contains(x,y))
                        {
                            levelManager.getPlayer().setPressingLeft(true);
                            levelManager.getPlayer().setPressingRight(false);
                        }
                        else if (mJump.contains(x,y))
                        {
                            levelManager.getPlayer().startJump(sound);
                        }
                        else if (mShoot.contains(x,y))
                        {

                        }
                        else if (mPause.contains(x,y))
                        {
                            levelManager.switchPlayingStatus();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        if (mRight.contains(x,y))
                        {
                            levelManager.getPlayer().setPressingRight(false);
                        }
                        else if (mLeft.contains(x,y))
                        {
                            levelManager.getPlayer().setPressingLeft(false);
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (mRight.contains(x, y))
                        {
                            levelManager.getPlayer().setPressingRight(true);
                            levelManager.getPlayer().setPressingLeft(false);
                        }
                        else if (mLeft.contains(x,y))
                        {
                            levelManager.getPlayer().setPressingLeft(true);
                            levelManager.getPlayer().setPressingRight(false);
                        }
                        else if (mJump.contains(x,y))
                        {
                            levelManager.getPlayer().startJump(sound);
                        }
                        else if (mShoot.contains(x,y))
                        {
                            //TODO handle shooting here
                        }
                        else if (mPause.contains(x,y))
                        {
                            levelManager.switchPlayingStatus();
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        if (mRight.contains(x, y))
                        {
                            levelManager.getPlayer().setPressingRight(false);
                            Log.w(TAG, "rightP: up");
                        }
                        else if (mLeft.contains(x,y))
                        {
                            levelManager.getPlayer().setPressingLeft(false);
                            Log.w(TAG, "leftP: up");
                        }
                        else if (mJump.contains(x,y))
                        {
                            //TODO handle more jumping stuff here later
                        }
                        else if (mShoot.contains(x,y))
                        {
                            //TODO handle shooting here
                        }
                        break;
                }
            }
            else //not playing
            {
                //move the viewport around to explore the map
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
                {
                    case MotionEvent.ACTION_DOWN:
                        if (mPause.contains(x,y))
                        {
                            levelManager.switchPlayingStatus();
                            Log.w(TAG, "Pause: DOWN");
                        }
                        break;
                }
            }
        }
    }

    /**
     * Create an arraylist of buttons for the draw method
     * @return list of buttons and their name
     */
    public HashMap<String, Rect> getButtons()
    {
        HashMap<String, Rect> currentButtonList = new HashMap<>();
        currentButtonList.put("left", mLeft);
        currentButtonList.put("right", mRight);
        currentButtonList.put("jump", mJump);
        currentButtonList.put("shoot", mShoot);
        currentButtonList.put("pause", mPause);

        return currentButtonList;
    }
}