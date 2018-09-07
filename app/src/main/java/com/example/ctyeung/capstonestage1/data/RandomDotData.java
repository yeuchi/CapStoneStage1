package com.example.ctyeung.capstonestage1.data;

import android.graphics.Bitmap;

import java.util.ArrayList;

/*
 * rendered random dot data -- collection of random dot assets
 * - background (common for both left/right)
 * - left
 * - right
 * - or combined interlaced ?
 */
public class RandomDotData
{
    public enum DotTypeEnum
    {
        LEFT,
        RIGHT,
        INTERLACED
    }
    private ArrayList<DotBitmap> list;

    /*
     * random dot data rendering
     */
    public RandomDotData(int width,
                         int height)
    {
        DotBitmap dotBmp = new DotBitmap(DotBitmap.BmpTypeEnum.BACKGROUND, width, height);
        list = new ArrayList<DotBitmap>();
        list.add(dotBmp);
    }

    /*
     * add a shape or test image
     */
    public void AddBmp(DotBitmap dotBmp)
    {
        list.add(dotBmp);
    }

    /*
     * Merge all images into a single final bitmap
     */
    public Bitmap Render(DotTypeEnum type)
    {
        switch(type)
        {
            case LEFT:
            case RIGHT:
                return createStereo(type);

            default:
            case INTERLACED:
                return createInterlaced();
        }
    }

    private Bitmap createStereo(DotTypeEnum type)
    {
        if(list.size()>1)
        {
            // render a bitmap
        }
        return null;
    }

    private Bitmap createInterlaced()
    {
        if(list.size()>1)
        {
            // render a bitmap
        }
        return null;
    }


}
