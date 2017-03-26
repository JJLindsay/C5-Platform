package com.agp.c5platformgame.app;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 * Finds the next occurrence of the currently selected text = ctrl + F3
 *
 * This class will represent a hit-box for a particular side of the object.
 * There isn't actually a rectangle in this class, just coordinate points
 */
public class RectangleHitBox
{
    private float mTop, mLeft, mBottom, mRight, mHeight;

    public boolean intersects(RectangleHitBox rectHitBox)
    {
        boolean hit = false;

        if (mRight > rectHitBox.mLeft && mLeft < rectHitBox.mRight)
        {
            //intersects on the right axis
            if (mTop < rectHitBox.mBottom && mBottom > rectHitBox.mTop)
            {
                //intersects on y aa well
                //collision
                hit = true;
            }
        }
        return hit;
    }

    public float getTop()
    {
        return mTop;
    }

    public void setTop(float top)
    {
        mTop = top;
    }

    public float getLeft()
    {
        return mLeft;
    }

    public void setLeft(float left)
    {
        mLeft = left;
    }

    public float getBottom()
    {
        return mBottom;
    }

    public void setBottom(float bottom)
    {
        mBottom = bottom;
    }

    public float getRight()
    {
        return mRight;
    }

    public void setRight(float right)
    {
        mRight = right;
    }

    public float getHeight()
    {
        return mHeight;
    }

    public void setHeight(float height)
    {
        mHeight = height;
    }
}
