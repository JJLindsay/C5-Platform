package com.agp.c5platformgame.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 * Finds the next occurrence of the currently selected text = ctrl + F3
 *
 * This class knows all the levels, the gameObjects in each level, and all the bitmaps in each level.
 * This class is called to build a level.
 */
public class LevelManager
{
    private String mLevel;

    private int mMapWidth;
    private int mMapHeight;

    private Player mPlayer;
    int mPlayerIndex;

    private boolean mPlaying;
    private float mGravity;

    private LevelData mLevelData;
    private ArrayList<GameObject> mGameObjects;

    private ArrayList<Rect> mCurrentButtons;
    private Bitmap[] mBitmapsArray;

    /**
     * Initializes a level and gives it a set amount of space for bitmap objects.
     * @param context
     * @param pixelsPerMeter
     * @param screenWidth
     * @param inputController
     * @param level
     * @param px
     * @param py
     */
    public LevelManager(Context context, int pixelsPerMeter, int screenWidth, InputController inputController, String level, float px, float py)
    {
        this.mLevel = level;

        switch (level)
        {
            case "LevelCave":
                mLevelData = new LevelCave();
                break;

            //TODO add more levels
        }

        //holds all game objects
        mGameObjects = new ArrayList<>();

        mBitmapsArray = new Bitmap[25];

        loadMapData(context, pixelsPerMeter, px, py);

        //ready to play
        //mPlaying = true;
    }

    /**
     *
     * @param blockType
     * @return a bitmap object found in the current level given a block type
     */
    public Bitmap getBitmap(char blockType)
    {
        int index;
        switch (blockType)
        {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            default:
                index = 0;
                break;
        }

        return mBitmapsArray[index];
    }

    /**
     *
     * @param blockType
     * @return the position of the block type in the bitmap array
     */
    public int getBitmapIndex(char blockType)
    {
        int index;
        switch (blockType)
        {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            default:
                index = 0;
                break;
        }
        return index;
    }

    /**
     * Loads the level row by row and column by column into 2 arrays.
     * One array holds a bitmap for every unique gameObject.
     * Another array holds all the gameObjects that make ever space of the level.
     * @param context
     * @param pixelsPerMeter
     * @param px
     * @param py
     */
    private void loadMapData(Context context, int pixelsPerMeter, float px, float py)
    {
        char singleChar;

        // keep track of where we load our game objects
        int currentIndex = -1;

        //how wide and high is the map? Viewport needs to know
        mMapHeight = mLevelData.mTiles.size();
        mMapWidth = mLevelData.mTiles.get(0).length();

        for (int row = 0; row < mLevelData.mTiles.size(); row++)
        {
            for (int column = 0; column < mLevelData.mTiles.get(row).length(); column++)
            {
                singleChar = mLevelData.mTiles.get(row).charAt(column);

                //as long as we aren't looking at one of the empty spaces
                if (singleChar != '.')
                {
                    currentIndex++;
                    switch (singleChar)
                    {
                        case '1':
                            //add grass to the gameObjects
                            mGameObjects.add(new Grass(column, row, singleChar));
                            break;
                        case 'p':
                            //add a player to the gameObjects
                            mGameObjects.add(new Player(context, px, py, pixelsPerMeter));

                            //we want to store the index of the player's position in the array
                            mPlayerIndex = currentIndex;
                            //we want a reference to the player
                            mPlayer = (Player)mGameObjects.get(mPlayerIndex);
                            break;
                    }

                    //if the bitmap for the gameObject hasn't been prepared yet, prepare it
                    if (mBitmapsArray[getBitmapIndex(singleChar)] == null)
                    {
                        //prepare a bitmap for the given char for future reference
                        mBitmapsArray[getBitmapIndex(singleChar)] = mGameObjects.get(currentIndex).prepareBitmap(context, mGameObjects.get(currentIndex).getBitmapName(), pixelsPerMeter);
                    }
                }
            }
        }
    }

    /**
     * There will be many gameObjects since every row/column must have a gameObject of some kind.
     * @return all the gameObjects found in this level.
     */
    public ArrayList<GameObject> gameObjects()
    {
        return mGameObjects;
    }

    /**
     * There is one bitmap for every unique gameObject.
     * @return all the unique bitmap objects needed for this level.
     */
    public Bitmap[] getBitmapsArray()
    {
        return mBitmapsArray;
    }

    public boolean isPlaying()
    {
        return mPlaying;
    }

    public void setPlaying(boolean playing)
    {
        mPlaying = playing;
    }

    public float getGravity()
    {
        return mGravity;
    }

    public void setGravity(float gravity)
    {
        mGravity = gravity;
    }

    public void switchPlayingStatus()
    {
        setPlaying(!isPlaying());

        if (isPlaying())
        {
            setGravity(6);
        }
        else
        {
            setGravity(0);
        }
    }

    public Player getPlayer()
    {
        return mPlayer;
    }
}