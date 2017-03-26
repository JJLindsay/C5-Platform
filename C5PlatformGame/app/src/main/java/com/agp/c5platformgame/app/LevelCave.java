package com.agp.c5platformgame.app;

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
 * This class contains the starting position for the all game objects
 * in the Cave level
 */
public class LevelCave extends LevelData
{
    private static final String EMPTY_TILE = "..............................................";

    public LevelCave(){
        mTiles = new ArrayList<String>();
        mTiles.add("p....................................");  // p is just an arbitrary position for the player to start
        mTiles.add(EMPTY_TILE);
        mTiles.add("....................111111...............");  // 1 is grass and also arbitrarily placed
        mTiles.add(EMPTY_TILE);
        mTiles.add(".............111111...........................");
        mTiles.add(EMPTY_TILE);
        mTiles.add(".....111111..........................");
        mTiles.add(EMPTY_TILE);
        mTiles.add(EMPTY_TILE);
        mTiles.add(".........................111111......");
        mTiles.add(EMPTY_TILE);
    }
}
