package com.agp.c5platformgame.app;

import java.util.ArrayList;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 *
 * This class holds an arraylist with the layout tiles for a
 * level. Each level will create an instance of this class.
 * The tiles contain the initial positioning for every game object.
 */
public class LevelData
{
    public ArrayList<String> mTiles;  //TODO change to private later

    //tile types for different game objects:
    // . = no tile
    // 1 = grass
    // p = player
}
