package com.agp.c5platformgame.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 * Finds the next occurrence of the currently selected text = ctrl + F3
 *
 * This class is for SpriteSheet Animations.
 *
 * It handles the function of keeping time and returning the appropriate part of the
 * SpriteSheet when requested. It basically loops through one at a time as the player (or gameObject) is moving.
 * Animated objects will be handled differently, like grass.
 */
public class Animation
{
    private Bitmap mBitmapSheet;
    private String mBitmapName;
    private Rect mSourceRect;  //coordinates of the currently relevant frame of the spriteSheet
    private int mFrameCount;
    private int mCurrentFrame;
    private long mFrameTicker;
    private int mFramePeriod;
    private int mFrameWidth;
    private int mFrameHeight;
    private int mPixelsPerMeter;

    /**
     * @param context
     * @param bitmapName
     * @param frameHeight
     * @param frameWidth
     * @param animFps the frame rate of the animation, not the game.
     * @param frameCount
     * @param pixelsPerMeter
     */
    public Animation (Context context, String bitmapName, float frameHeight, float frameWidth, int animFps, int frameCount, int pixelsPerMeter)
    {
        mCurrentFrame = 0;
        mFrameCount = frameCount;
        mFrameWidth = (int) frameWidth * pixelsPerMeter;
        mFrameHeight = (int) frameHeight * pixelsPerMeter;
        mSourceRect = new Rect(0, 0, mFrameWidth, mFrameHeight);
        mFramePeriod = 1000 / animFps;
        mFrameTicker = 01;
        mBitmapName = "" + bitmapName;
        mPixelsPerMeter = pixelsPerMeter;
    }

    public Rect getCurrentFrame(long time, float xVelocity, boolean moves)
    {
        /*
            Only animate if the obj is moving or it is an obj
            which doesn't move but is still animated (like fire)
         */
        if (xVelocity != 0 || !moves)
        {
            if (time > mFrameTicker + mFramePeriod)
            {
                mFrameTicker = time;
                mCurrentFrame++;
                if (mCurrentFrame >= mFrameCount)
                {
                    mCurrentFrame = 0;
                }
            }
        }

        /*
         Update the left and right values to
         the next frame on the spriteSheet.
         */
        mSourceRect.left = mCurrentFrame * mFrameWidth;
        mSourceRect.right = mSourceRect.left + mFrameWidth;

        return mSourceRect;
    }
}