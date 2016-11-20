package com.agp.c5platformgame.app;

import android.content.Context;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 *
 * This class builds a Player game object.
 * It knows how it should be updated and with its
 * parent, it can grab the right bitmap by name from the drawable folder.
 */
public class Player extends GameObject
{
    public Player(Context context, float worldStartX, float worldStartY, int pixelsPerMeter)
    {
        final float HEIGHT = 2;
        final float WIDTH = 1;

        super.setHeight(HEIGHT);  // 2 meters tall
        super.setWidth(WIDTH);   // 1 meter wide

        super.setType('p');

        super.setBitmapName("player");
        super.setWorldLocation(worldStartX, worldStartY, 0);
    }

    @Override
    public void update(Long fps, float gravity)
    {
        //TODO
    }
}
