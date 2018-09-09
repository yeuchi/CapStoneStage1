package com.example.ctyeung.capstonestage1.data;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

/*
 * Data collection of a rendered result for preview
 * assume the following bitmap order
 * - 0:left, 1:right
 * - 0:interlaced
 */
public class RandomDotData
{
    public enum DotTypeEnum
    {
        LEFT,
        RIGHT,
        INTERLACED
    }

    private ArrayList<Bitmap> list;

    /*
     * random dot data rendering
     */
    public RandomDotData()
    {
        list = new ArrayList<Bitmap>();
    }

    /*
     * enqueue a rendered bitmap for preview
     */
    public void endQbmp(Bitmap dotBmp)
    {
        list.add(dotBmp);
    }

    /*
     * dequeue a rendered bitmap for display
     */
    public Bitmap deQbmp()
    {
        /*
         * not support a dequeue to keep data structure immutable
         * - use seek instead to access data
         */
        return null;
    }

    /*
     * retrieve a rendered dot bitmap for display; given an index
     */
    public Bitmap seek(int index)
    {
        if (index >= list.size())
            return null;

        return list.get(index);
    }

    /*
     * how many do we have ?
     * - 1 if interlaced
     * - 2 if stereo
     */
    public int count()
    {
        return list.size();
    }

    /*
     * Distance in pixel between background image and left text/shape image
     */
    public static int getBorderOffset(Context context)
    {
        return SharedPrefUtility.getDimension(SharedPrefUtility.IMAGE_HEIGHT, context);
    }

    /*
     * Background height is image height + 2 * borderOffset
     */
    public static int getBackgroundImageLength(Context context)
    {
        return getImageHeight(context) + 2*getBorderOffset(context);
    }

    public static int getImageHeight(Context context)
    {
        return SharedPrefUtility.getDimension(SharedPrefUtility.IMAGE_HEIGHT, context);
    }

    /*
     * Parallax - x offset (pixels) between left + right image
     */
    public static int getParallaxDistance(Context context)
    {
        return SharedPrefUtility.getDimension(SharedPrefUtility.PARALLAX_DIS, context);
    }
}
