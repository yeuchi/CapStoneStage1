package com.example.ctyeung.capstonestage1.data;

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
    public int Count()
    {
        return list.size();
    }
}
