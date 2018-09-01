package com.example.ctyeung.capstonestage1.data;

import android.graphics.Bitmap;

import java.util.ArrayList;

/*
 * rendered random dot data -- value object class
 */
public class RandomDotData
{
    private ArrayList<Bitmap> bitmaps;

    public RandomDotData()
    {
        bitmaps = new ArrayList<Bitmap>();
    }

    public void AddBmp(Bitmap bitmap)
    {
        bitmaps.add(bitmap);
    }

}
