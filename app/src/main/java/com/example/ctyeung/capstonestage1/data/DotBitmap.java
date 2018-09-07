package com.example.ctyeung.capstonestage1.data;

import android.graphics.Bitmap;
import android.graphics.Color;

/*
 * value object class to hold bitmap + descriptor
 * - has operator: dither
 */
public class DotBitmap
{
    public enum BmpTypeEnum
    {
        BACKGROUND,
        LEFT_SHAPE,
        LEFT_TEXT,
        LEFT_FLATTENED,
        RIGHT_SHAPE,
        RIGHT_TEXT,
        RIGHT_FLATTENED,
        INTERLACED
    }

    private Bitmap bitmap;
    private BmpTypeEnum bmpType;

    public DotBitmap(BmpTypeEnum bmpType,
                     int width,
                     int height)
    {
        /*
         * if bmpType is background, go dither !
         */
        bitmap = Bitmap.createBitmap(width,
                                    height,
                                    Bitmap.Config.ARGB_8888);

        if(BmpTypeEnum.BACKGROUND == bmpType)
            dither();
    }

    public DotBitmap(BmpTypeEnum bmpType,
                     Bitmap bmp)
    {
        bitmap = bmp;
    }

    public DotBitmap clone()
    {
        Bitmap bmpClone = bitmap.copy(bitmap.getConfig(), true);
        return new DotBitmap(bmpType, bmpClone);
    }

    public BmpTypeEnum getType()
    {
        return bmpType;
    }

    public void setType(BmpTypeEnum type)
    {
        bmpType = type;
    }

    public Bitmap getBmp()
    {
        return bitmap;
    }

    public void setBmp(Bitmap bmp)
    {
        bitmap = bmp;
    }

    /*
     * dither random dot background
     * - should use poisson distribution to produce
     *   more visually appealing random images.
     *   But for now .. random function to start.
     */
    private void dither()
    {

        for(int y=0; y<bitmap.getHeight(); y++)
        {
            for(int x=0; x<bitmap.getWidth(); x++)
            {
                int random = (int)(Math.random() * 255);
                int pixel = Color.argb(255, random, random, random);
                bitmap.setPixel(x,y, pixel);
            }
        }
    }
}
