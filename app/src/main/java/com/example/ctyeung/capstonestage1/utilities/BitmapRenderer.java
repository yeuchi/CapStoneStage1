package com.example.ctyeung.capstonestage1.utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapRenderer extends BaseRenderer
{
    public BitmapRenderer(Context context)
    {
        super(context);
    }

    /*
     * dither random dot background
     * - should use poisson distribution to produce
     *   more visually appealing random images.
     *   But for now .. random function to start.
     */
    public Bitmap randomDot(int imageHeight)
    {
        Bitmap bitmap = blank(imageHeight, imageHeight);
        boolean isDebug = SharedPrefUtility.getBoolean(SharedPrefUtility.IS_DEBUG, mContext);

        int pixelWhite = Color.argb(255, 255, 255, 255);
        int pixelBlack = Color.argb(255, 0, 0, 0);

        int[] colors = getColors();

        for(int y=0; y<bitmap.getHeight(); y++)
        {
            for(int x=0; x<bitmap.getWidth(); x++)
            {
                int index = (int)(Math.random() * 4.0);
                int darkPixel = (isDebug)? pixelBlack : colors[index];

                int des = (Math.random() < 0.5)?
                        darkPixel:
                        pixelWhite;

                bitmap.setPixel(x,y, des);
            }
        }
        return bitmap;
    }

    /*
     * Create a bitmap from View; in fragments: text + shape
     */
    public Bitmap blank(int width, int height)
    {
        Bitmap bitmap = Bitmap.createBitmap(width,
                                            height,
                                            Bitmap.Config.ARGB_8888);
        return bitmap;
    }
}
