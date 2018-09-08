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
    public Bitmap[] Render(DotTypeEnum type)
    {
        switch(type)
        {
            case LEFT:
            case RIGHT:
                return createStereo();

            default:
            case INTERLACED:
                return createInterlaced();
        }
    }

    /*
     * create + return random dot stereograms pair
     */
    private Bitmap[] createStereo()
    {
        if(list.size()>1)
        {
            // render a bitmap
        }
        return null;
    }

    /*
     * create + return single random dot interlaced image
     */
    private Bitmap[] createInterlaced()
    {
        if(list.size()>1)
        {
            // 1st create stereo pair
            Bitmap[] bitmaps = createStereo();

            // interlace the 2 bitmaps

        }
        return null;
    }


}
