package com.example.ctyeung.capstonestage1.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.ctyeung.capstonestage1.data.RandomDotData;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;

/*
 * render dot bitmaps as preview.
 * - this should be converted into NDK C++ for performance.
 */
public class RandomDotRenderer
{
    private Bitmap bmpBackground;               // common background dithered image
    private Context mContext;                   // preview fragment ui context

    public RandomDotRenderer(Context context)
    {
        mContext = context;
        createCommon();
    }

    /*
     * background dither is the same for left + right / interlaced
     */
    protected void createCommon()
    {
        /*
         * text + shape image(s) are in the center of background dither image
         */
        int height = RandomDotData.getImageHeight(mContext);
        bmpBackground = BitmapRenderer.randomDot(height);
    }

    /*
     * go render left + right random images
     */
    public RandomDotData createStereoPair(Bitmap bmpText,   // text image, maybe null, w=2*h, w=h
                                         Bitmap bmpShape)   // shape image, maybe null, w=2*h, w=h
    {
        /*
         * check image sizes ?
         *
         * NOTE: text + shape image in the middle of the background image
         */
        RandomDotData data = new RandomDotData();
        int xOffset = RandomDotData.getBorderOffset(mContext);

        for (int num=0; num<2; num++)
        {
            bmpText = dither(bmpText);
            bmpShape = dither(bmpShape);
            Bitmap bmp = integrate(bmpText, bmpShape, xOffset);
            data.endQbmp(bmp);

            // horizontal offset for parallax
            xOffset += RandomDotData.getParallaxDistance(mContext);
        }


        return null;
    }

    /*
     * dither - convert text or shape image into random dots
     * - use poisson would be best
     */
    protected Bitmap dither(Bitmap bmpSrc)
    {
        if(null!=bmpSrc)
        {
            int pixelBlack = 0;
            int pixelWhite = 1;

            for(int y=0; y<bmpSrc.getHeight(); y++)
            {
                for(int x=0; x<bmpSrc.getWidth(); x++)
                {
                    int src = bmpSrc.getPixel(x, y);

                    // if pixel is 'on' -> dither
                    int des = (Math.random() >= 0.5)?
                            pixelWhite:
                            pixelBlack;

                    if(src != des) // if white
                        bmpSrc.setPixel(x, y, des);
                }
            }
        }
        return bmpSrc;
    }

    /*
     * Integrate - overlay both text and shape image(s) onto background
     * - User has option to select following:
     *
     * 1. text only
     * 2. shape only
     * 3. text + shape
     */
    protected Bitmap integrate( Bitmap bmpText,
                                Bitmap bmpShape,
                                int xOffset)
    {
        /*
         * assume images: text + shape together make a square image
         * NOTE: there maybe 1 or the other; which text or shape would be a square
         */
        Bitmap bmpDes = bmpBackground.copy(Bitmap.Config.ARGB_8888,true);

        /*
         * assume text on top of shape - always ?
         * - what about render them overlapping with different color dots (experiment later) ?
         *
         * - how do I scale image to fit for text + shape ?
         */
        int yStart = RandomDotData.getBorderOffset(mContext);
        if(null!=bmpText)
        {
            bmpDes = overlay(bmpDes, bmpText, xOffset, yStart);
            yStart += bmpText.getHeight();
        }

        if(null!=bmpShape &&
                yStart < bmpDes.getHeight())
        {
            bmpDes = overlay(bmpDes, bmpShape, xOffset, yStart);
        }
        return bmpDes;
    }

    /*
     * Draw text or shape image onto destination background
     */
    protected Bitmap overlay(Bitmap bmpDes,     // destination image
                             Bitmap bmpSrc,     // source image: text or shape
                             int xOffset,       // image offset from border
                             int yOffset)       // starting point on destination image
    {
        Canvas canvas = new Canvas(bmpDes);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bmpSrc, xOffset, yOffset, paint);
        return bmpDes;
    }

    /*
     * 1. go render left + right random images
     * 2. go interlace above images
     */
    public RandomDotData createInterlaced(Bitmap bmpText,
                                         Bitmap bmpShape)
    {
        // create the random dot stereo pair
        RandomDotData randomDotData = createStereoPair(bmpText, bmpShape);

        // slice-n-dice, interlace them into one image

        /*
         * CTY ... to do here !
         */

        return null;
    }
}


