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
 * This class builds a Grass game object.
 * It knows how it should be updated and with its
 * parent, it can grab the right bitmap by name from the drawable folder.
 */
public class Grass extends GameObject
{
    public Grass(float worldStartX, float worldStartY, char type)
    {
        final float HEIGHT = 1;
        final float WIDTH = 1;

        super.setHeight(HEIGHT);
        super.setWidth(WIDTH);

        super.setType(type);

        //choose a bitmap and set its location
        super.setBitmapName("turf");
        super.setWorldLocation(worldStartX, worldStartY, 0);  //(x,y,z)

        //add hitbox
        setRectangleHitBox();
    }

    @Override
    public void update(long fps, float gravity)
    {
        //TODO
    }
}