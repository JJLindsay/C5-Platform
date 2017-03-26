package com.agp.c5platformgame.app;

import android.graphics.Rect;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 *
 * This class is the camera for all intensive purposes:
 * This class deals with the interaction between the device, game objects,
 * and the 'camera' that follows the action of the player. Typically it will be
 * centered on the player. It defines what in the world will be shown to the player.
 * It can also be used to focus the player's attention for a moment.
 */
public class Viewport
{
    private final Vector2Point5D currentViewportWorldCenterPoint;
    private final Rect convertedRect;
    private final int pixelsPerMeterX;
    private final int pixelsPerMeterY;
    private final int screenXResolution;
    private final int screenYResolution;
    private final int screenCenterX;
    private final int screenCenterY;
    private final int metersToShowX;
    private final int metersToShowY;
    private int numClipped;

    public Viewport(int x, int y)
    {
        // record device resolution
        screenXResolution = x;
        screenYResolution = y;

        // identify the center of the screen
        screenCenterX = screenXResolution / 2;
        screenCenterY = screenYResolution / 2;

        // the number of pixels that will constitute a meter in the game
        // Adjust to zoom in/out
        // e.g. for 840x400 = 32/22
        pixelsPerMeterX = screenXResolution / 32;
        pixelsPerMeterY = screenYResolution / 18;

        // how many game meters should be visible?
        metersToShowX = 34;
        metersToShowY = 20;

        convertedRect = new Rect();
        currentViewportWorldCenterPoint = new Vector2Point5D();
    }

    /**
     * The center point in the game world, irrespective of the device.
     */
    public void setWorldCenterPoint(float x, float y)
    {
        currentViewportWorldCenterPoint.setX(x);
        currentViewportWorldCenterPoint.setY(y);
    }

    /**
     * Finds the object's relative position in the camera and converts it from meters to pixels
     * (something the device screen understands)
     * @param objectX
     * @param objectY
     * @param objectWidth
     * @param objectHeight
     * @return a rectangle that holds the literal position of a gameObject converted from meters to pixels
     */
    public Rect worldToScreen(float objectX, float objectY, float objectWidth, float objectHeight)
    {
        // screen center x (in pixels) minus ((what is the distance between the center of the camera and the x point of the object?) converted to pixels)
        int left = (int) (screenCenterX - ((currentViewportWorldCenterPoint.getX() - objectX) * pixelsPerMeterX));
        int top = (int) (screenCenterY - ((currentViewportWorldCenterPoint.getY() - objectY) * pixelsPerMeterY));
        int right = (int) (left + (objectWidth * pixelsPerMeterX));
        int bottom = (int) (top + (objectHeight * pixelsPerMeterY));

        convertedRect.set(left, top, right, bottom);

        return convertedRect;
    }

    /**
     * Every object that is beyond the viewPort  is flagged to be clipped. The maximum distance an object can
     * be away from the edge of the viewPort is the width (or height) of the object.
     *
     * @param objectX  The top left corner of the object
     * @param objectY  The top left corner of the object
     * @param objectWidth  width of the object
     * @param objectHeight  height of the object
     *
     * @return
     */
    public boolean clipObjects(float objectX, float objectY, float objectWidth, float objectHeight)
    {
        boolean clipped  = true;

        //if the object is beyond the viewPort to the right (for example), it will be clipped
        if (objectX - objectWidth < currentViewportWorldCenterPoint.getX() + (metersToShowX / 2))  //compare the leftmost side of the object to the right most side of the viewPort
        {
            if (objectX + objectWidth > currentViewportWorldCenterPoint.getX() - (metersToShowX / 2))  //compare rightmost side of the object to the leftmost side of the viewPort
            {
                if (objectY - objectHeight < currentViewportWorldCenterPoint.getY() + (metersToShowY / 2))  //compare the lowest point of the object to the
                {
                    if (objectY + objectHeight > currentViewportWorldCenterPoint.getY() - (metersToShowY / 2))
                    {
                        clipped = false;
                    }
                }
            }
        }
        //for debugging
        if (clipped)
            numClipped++;

        return clipped;
    }

    public int getNumClipped()
    {
        return numClipped;
    }

    public void resetNumClipped()
    {
        numClipped = 0;
    }

    public int getScreenWidth()
    {
        return screenXResolution;
    }

    public int getScreenHeight()
    {
        return screenYResolution;
    }

    public int getPixelsPerMeterX()
    {
        return pixelsPerMeterX;
    }
}