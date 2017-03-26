package com.agp.c5platformgame.app;

import android.content.Context;
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
 * This class builds a Player game object.
 * It knows how it should be updated and with its
 * parent, it can grab the right bitmap by name from the drawable folder.
 */
public class Player extends GameObject
{
    private final String TAG = Player.class.getSimpleName();

    private final float MAX_X_VELOCITY = 10;
    private boolean mIsPressingRight = false;
    private boolean mIsPressingLeft = false;

    private boolean mIsFalling, mIsJumping;
    private long mJumpTime;
    private final long mMaxJumpTime = 700; // jump 7 10ths of a second

    private RectangleHitBox mRectHitboxFeet;
    private RectangleHitBox mRectHitboxHead;
    private RectangleHitBox mRectHitboxLeft;
    private RectangleHitBox mRectHitboxRight;


    public Player(Context context, float worldStartX, float worldStartY, int pixelsPerMeter)
    {
        final float HEIGHT = 2;
        final float WIDTH = 1;
        final int ANIMATION_FPS = 16;
        final int ANIMATION_FRAME_COUNT = 5;

        super.setHeight(HEIGHT);  // 2 meters tall
        super.setWidth(WIDTH);   // 1 meter wide

        setMoves(true);
        setActive(true);
        setVisible(true);

        super.setType('p');

        super.setBitmapName("player");

        //set this obj up to be animated
        setAnimationFPS(ANIMATION_FPS);
        setAnimationFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelsPerMeter, true);

        super.setWorldLocation(worldStartX, worldStartY, 0);

        //initialize hit-boxes
        mRectHitboxFeet = new RectangleHitBox();
        mRectHitboxHead = new RectangleHitBox();
        mRectHitboxLeft = new RectangleHitBox();
        mRectHitboxRight = new RectangleHitBox();

    }

    @Override
    public void update(long fps, float gravity)
    {
        if (mIsPressingRight)
        {
            setXVelocity(MAX_X_VELOCITY);
        }
        else if (mIsPressingLeft)
        {
            setXVelocity(-MAX_X_VELOCITY);
        }
        else
        {
            setXVelocity(0);
        }

        //which way is the player facing?
        if (getXVelocity() > 0)
        {
            //facing right
            setFacing(RIGHT);
        }
        else if (getXVelocity() < 0)
        {
            setFacing(LEFT);
        } //if 0, unchanged

        //jumping and gravity
        Log.d(TAG, "Is player jumping: " + mIsJumping);  //DEBUGGING
        if (mIsJumping)
        {
            long timeJumping = System.currentTimeMillis() - mJumpTime;
            if (timeJumping < mMaxJumpTime)
            {
                if (timeJumping < mMaxJumpTime / 2)
                {
                    setYVelocity(-gravity); //on the way up
                }
                else if (timeJumping > mMaxJumpTime / 2)
                {
                    setYVelocity(gravity); //going down
                }
            }
            else
            {
                setJumping(false);
            }
        }
        else
        {
            setYVelocity(gravity);
            /* TODO Remove this nextline to make the game easier.
            It means the long jumps are less punishing b/c
            the player can take off just after the platform.
            They will also be able to cheat by double jump.
             */
            setFalling(true);
        }

        move(fps);

        //update all hit-boxes to the new location
        //get the current world location of the player
        Vector2Point5D location = getWorldLocation();
        float lx = location.getX();
        float ly = location.getY();

        //update the player feet hit-box
        mRectHitboxFeet.setTop(ly + getHeight() * .95f);
        mRectHitboxFeet.setLeft(lx + getWidth() * .2f);
        mRectHitboxFeet.setBottom(ly + getHeight() * .98f);
        mRectHitboxFeet.setRight(lx + getWidth() * .8f);

        //update the player head hit-box
        mRectHitboxHead.setTop(ly);
        mRectHitboxHead.setLeft(lx + getWidth() * .4f);
        mRectHitboxHead.setBottom(ly + getHeight() * .2f);
        mRectHitboxHead.setRight(lx + getWidth() * .6f);

        //update the player left hit-box
        mRectHitboxLeft.setTop(ly + getHeight() * .2f);
        mRectHitboxLeft.setLeft(lx + getWidth() * .2f);
        mRectHitboxLeft.setBottom(ly + getHeight() * .8f);
        mRectHitboxLeft.setRight(lx + getWidth() * .3f);

        //update the player right hit-box
        mRectHitboxRight.setTop(ly + getHeight() * .2f);
        mRectHitboxRight.setLeft(lx + getWidth() * .8f);
        mRectHitboxRight.setBottom(ly + getHeight() * .8f);
        mRectHitboxRight.setRight(lx + getWidth() * .7f);

    }

    public boolean isFalling()
    {
        return mIsFalling;
    }

    public void setFalling(boolean falling)
    {
        mIsFalling = falling;
    }

    public boolean isJumping()
    {
        return mIsJumping;
    }

    public void setJumping(boolean jumping)
    {
        mIsJumping = jumping;
    }

    public int checkCollisions(RectangleHitBox foreignObjRectHitBox)
    {
        int collided = 0;  //no collision

        //left
        if (mRectHitboxLeft.intersects(foreignObjRectHitBox))
        {
            //left has collided
            //move player just to right of current hit-box
            setWorldLocationX(foreignObjRectHitBox.getRight() - getWidth() *.2f);
            collided = 1;
        }
        //right
        if (mRectHitboxRight.intersects(foreignObjRectHitBox))
        {
            //left has collided
            //move player just to right of current hit-box
            setWorldLocationX(foreignObjRectHitBox.getLeft() - getWidth() *.8f);
            collided = 1;
        }
        //feet
        if (mRectHitboxFeet.intersects(foreignObjRectHitBox))
        {
            //right has collided
            //move player just to left of current hit-box
            setWorldLocationY(foreignObjRectHitBox.getTop() - getHeight());
            collided = 2;
        }
        //head
        if (mRectHitboxHead.intersects(foreignObjRectHitBox))
        {
            //head has collided
            //move head to just below current hit-box bottom
            setWorldLocationY(foreignObjRectHitBox.getBottom());
            collided = 3;
        }

        return collided;
    }

    public boolean isPressingRight()
    {
        return mIsPressingRight;
    }

    public void setPressingRight(boolean pressingRight)
    {
        mIsPressingRight = pressingRight;
    }

    public boolean isPressingLeft()
    {
        return mIsPressingLeft;
    }

    public void setPressingLeft(boolean pressingLeft)
    {
        mIsPressingLeft = pressingLeft;
    }

    public void startJump(SoundManager soundManager)
    {
        if (!isFalling())  //no jumping if falling
        {
            if (!isJumping())  //no double jumping
            {
                mIsJumping = true;
                mJumpTime = System.currentTimeMillis();
                soundManager.playSound("jump");
            }
        }

    }
}
