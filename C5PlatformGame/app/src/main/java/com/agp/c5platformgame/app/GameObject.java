package com.agp.c5platformgame.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 *
 * The base class for all game objects.
 * All child classes can utilize gameObject to
 * prepare the correct bitmap from drawable based on
 * the name. The bitmap is also positioned here.
 */
public abstract class GameObject
{
    private static final String TAG = GameObject.class.getSimpleName();

    public Vector2Point5D mWorldLocation;
    public float mWidth;
    public float mHeight;
    public Boolean mActive = true;
    public Boolean mVisible = true;
    public int mAnimeFrameCount = 1;
    public char mType;
    public String mBitmapName;

    public abstract void update(Long fps, float gravity);

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
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(mWidth * mAnimeFrameCount * pixelsPerMeter), (int)(mHeight * pixelsPerMeter), false);

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
        this.mWorldLocation.x = x;
        this.mWorldLocation.y = y;
        this.mWorldLocation.z = z;
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
}
