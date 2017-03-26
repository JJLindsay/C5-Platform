package com.agp.c5platformgame.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 * Finds the next occurrence of the currently selected text = ctrl + F3
 *
 * The base class for all game objects.
 * All child classes can utilize gameObject to
 * prepare the correct bitmap from drawable based on
 * the name. The bitmap is also positioned here.
 */
public abstract class GameObject
{
    private static final String TAG = GameObject.class.getSimpleName();

    private Vector2Point5D mWorldLocation;
    private float mWidth;
    private float mHeight;
    private Boolean mActive = true;
    private Boolean mVisible = true;
    private int mAnimationFrameCount = 1;
    private char mType;
    private String mBitmapName;

    private float mXVelocity, mYVelocity;

    public final int LEFT = -1;
    public final int RIGHT = 1;

    private int mFacing;
    private boolean mMoves = false;

    //TODO only one hitbox??
    private final RectangleHitBox mRectangleHitBox = new RectangleHitBox();

    //Most objects only have 1 frame and won't need these
    private Animation mAnimation = null;
    private boolean mAnimated;
    private int mAnimationFPS = 1;

    public abstract void update(long fps, float gravity);

    public Bitmap prepareBitmap(Context context, String bitmapName, int pixelsPerMeter)
    {
        //make a resource from the bitmap name
        int resID = context.getResources().getIdentifier(bitmapName, "drawable", context.getPackageName());
        Log.d(TAG, "The resID: " + resID);

        //create bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

        //scale the bitmap based on the number of pixels per meter
        //multiply by the number of frames in the image
        //default 1 frame
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(mWidth * mAnimationFrameCount * pixelsPerMeter), (int)(mHeight * pixelsPerMeter), false);

        return bitmap;
    }

    public Vector2Point5D getWorldLocation()
    {
        return mWorldLocation;
    }

    /**
     * The position of the gameObject within the "camera's" view
     * @param x
     * @param y
     * @param z
     */
    public void setWorldLocation(float x, float y, int z)
    {
        this.mWorldLocation = new Vector2Point5D();
        this.mWorldLocation.setX(x);
        this.mWorldLocation.setY(y);
        this.mWorldLocation.setZ(z);
    }

    public void setWorldLocationX(float x)
    {
        this.mWorldLocation.setX(x);
    }

    public void setWorldLocationY(float y)
    {
        this.mWorldLocation.setY(y);
    }

    public void setWorldLocationZ(int z)
    {
        this.mWorldLocation.setZ(z);
    }


    /**
     * Checks the velocity on either on either axis is not 0 and it moves the object by
     * changing its world location.
     * Divides the velocity by the frames per second to calculate the distance to move each
     * frame. This ensures the movement will be correct regardless of the current fps. It does
     * not matter if the game executes smoothly or fluctuates, or how powerful or puny the CPU is.
     * @param fps
     */
    public void move(long fps)
    {
        if (mXVelocity != 0)
        {
            getWorldLocation().setX(getWorldLocation().getX() + mXVelocity / fps);
        }

        if (mYVelocity != 0)
        {
            getWorldLocation().setY(getWorldLocation().getY() + mYVelocity / fps);
        }
    }

    public String getBitmapName()
    {
        return mBitmapName;
    }

    public void setBitmapName(String bitmapName)
    {
        this.mBitmapName = bitmapName;
    }

    public float getWidth()
    {
        return mWidth;
    }

    public void setWidth(float width)
    {
        mWidth = width;
    }

    public float getHeight()
    {
        return mHeight;
    }

    public void setHeight(float height)
    {
        mHeight = height;
    }

    public Boolean isActive()
    {
        return mActive;
    }

    public void setActive(Boolean active)
    {
        mActive = active;
    }

    public Boolean isVisible()
    {
        return mVisible;
    }

    public void setVisible(Boolean visible)
    {
        mVisible = visible;
    }

    public char getType()
    {
        return mType;
    }

    public void setType(char type)
    {
        mType = type;
    }

    public Boolean getActive()
    {
        return mActive;
    }

    public float getXVelocity()
    {
        return mXVelocity;
    }

    public void setXVelocity(float XVelocity)
    {
        Log.d(TAG, "X velocity changed to: " + XVelocity);  //DEBUGGING
        mXVelocity = XVelocity;
    }

    public float getYVelocity()
    {
        return mYVelocity;
    }

    public void setYVelocity(float YVelocity)
    {
        Log.d(TAG, "Y velocity changed to: " + YVelocity);  //DEBUGGING
        mYVelocity = YVelocity;
    }

    public int getFacing()
    {
        return mFacing;
    }

    public void setFacing(int facing)
    {
        mFacing = facing;
    }

    public boolean isMoves()
    {
        return mMoves;
    }

    public void setMoves(boolean moves)
    {
        mMoves = moves;
    }

    public void setRectangleHitBox()
    {
        mRectangleHitBox.setTop(mWorldLocation.getY());
        mRectangleHitBox.setLeft(mWorldLocation.getX());
        mRectangleHitBox.setBottom(mWorldLocation.getY() + mHeight);
        mRectangleHitBox.setRight(mWorldLocation.getX() + mWidth);
    }

    public RectangleHitBox getRectangleHitBox()
    {
        return mRectangleHitBox;
    }

    public void setAnimationFPS(int animationFPS)
    {
        mAnimationFPS = animationFPS;
    }

    public void setAnimationFrameCount(int animationFrameCount)
    {
        mAnimationFrameCount = animationFrameCount;
    }

    /**
     * Objects that require animation can use this to setup their whole animation obj.
     * @param context
     * @param pixelsPerMeter
     * @param animated
     */
    public void setAnimated(Context context, int pixelsPerMeter, boolean animated)
    {
        mAnimated = animated;
        mAnimation = new Animation(context,
                mBitmapName,
                mHeight,
                mWidth,
                mAnimationFPS,
                mAnimationFrameCount,
                pixelsPerMeter);
    }

    public boolean isAnimated()
    {
        return mAnimated;
    }

    public int getAnimationFPS()
    {
        return mAnimationFPS;
    }

    public int getAnimationFrameCount()
    {
        return mAnimationFrameCount;
    }

    /**
     * A go between for the draw method of the PlatformView and Animation getRectToDraw()
     * @param deltaTime
     * @return
     */
    public Rect getRectToDraw(long deltaTime)
    {
        return mAnimation.getCurrentFrame(deltaTime, mXVelocity, isMoves());
    }



}
