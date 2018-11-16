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
public class RandomDotRenderer extends BaseRenderer
{
    private Bitmap mBmpBackground;               // common background dithered image
    private int mLongLength;
    private BitmapRenderer mBmpRenderer;

    public RandomDotRenderer(Context context,
                             int longLength)
    {
        super(context);
        mBmpRenderer = new BitmapRenderer(context);
        mLongLength = longLength;
        createCommon();
    }

    /*
     * background dither is the same for left + right / interlaced
     */
    protected void createCommon()
    {
        /*
         * Retrieve configurations and calculate image size
         */
        mLongLength +=  RandomDotData.getBorderOffset(mContext)*2 +
                        RandomDotData.getParallaxDistance(mContext);

        mBmpBackground = mBmpRenderer.randomDot(mLongLength);
    }

    /*
     * go render left + right random images
     */
    public RandomDotData createStereoPair(Bitmap bmpShape)   // shape image, maybe null, w=2*h, w=h
    {
        /*
         * check image sizes ?
         *
         * NOTE: text + shape image in the middle of the background image
         */
        RandomDotData data = new RandomDotData();
        int xOffset = RandomDotData.getBorderOffset(mContext);

        Bitmap bmpDither = ditherShape(bmpShape);

        for (int num=0; num<2; num++)
        {
            Bitmap bmp = integrate(bmpShape, bmpDither, xOffset);
            data.endQbmp(bmp);

            // horizontal offset for parallax
            xOffset += RandomDotData.getParallaxDistance(mContext);
        }
        return data;
    }

    /*
     * dither - convert text or shape image into random dots
     * - use poisson would be best
     */
    protected Bitmap dither(Bitmap bmpSrc)
    {
        if(null!=bmpSrc)
        {
            int pixelWhite = Color.argb(255, 255, 255, 255);
            int pixelBlack = Color.argb(255, 0, 0, 0);

            int index = 0;
            int[] colors = getColors();

            for(int y=0; y<bmpSrc.getHeight(); y++)
            {
                for(int x=0; x<bmpSrc.getWidth(); x++)
                {
                    int src = bmpSrc.getPixel(x, y);

                    // if pixel is 'on' -> dither
                    int des = (Math.random() < 0.5)?
                            colors[index]:
                            pixelWhite;


                    if(src != des) // if white
                        bmpSrc.setPixel(x, y, des);

                    index = (index >= 3)? 0:index+1;
                }
            }
        }
        return bmpSrc;
    }

    protected Bitmap ditherShape(Bitmap bmpSrc)
    {
        Bitmap bmpDither = null;

        if(null!=bmpSrc)
        {
            int pixelEmpty = 0;
            int pixelWhite = Color.argb(255, 255, 255, 255);
            int pixelBlack = Color.argb(255, 0, 0, 0);

            int index = 0;
            int[] colors = getColors();

            bmpDither = Bitmap.createBitmap(bmpSrc.getWidth(),
                                            bmpSrc.getHeight(),
                                            Bitmap.Config.ARGB_8888);

            for(int y=0; y<bmpSrc.getHeight(); y++)
            {
                for(int x=0; x<bmpSrc.getWidth(); x++)
                {
                    int src = bmpSrc.getPixel(x, y);

                    if(src != pixelEmpty) {

                        int des = (Math.random() < 0.5)?
                            colors[index]:
                            pixelWhite;

                        index = (index >= 3)? 0:index+1;

                        bmpDither.setPixel(x, y, des);
                    }
                }
            }
        }
        return bmpDither;
    }

    /*
     * Integrate - overlay both text and shape image(s) onto background
     * - User has option to select following:
     *
     * 1. shape only
     * 2. text + shape
     */
    protected Bitmap integrate( Bitmap bmpShape,
                                Bitmap bmpDither,
                                int xOffset)
    {
        /*
         * assume images: text + shape together make a square image
         * NOTE: there maybe 1 or the other; which text or shape would be a square
         */
        Bitmap bmpDes = mBmpBackground.copy(Bitmap.Config.ARGB_8888,true);

        /*
         * assume text on top of shape - always ?
         * - what about render them overlapping with different color dots (experiment later) ?
         *
         * - how do I scale image to fit for text + shape ?
         */
        int yStart = RandomDotData.getBorderOffset(mContext);

        if(null!=bmpShape &&
                yStart < bmpDes.getHeight())
        {
            bmpDes = overlay(bmpDes, bmpShape, bmpDither, xOffset, yStart);
        }
        return bmpDes;
    }

    /*
     * Draw text or shape image onto destination background
     */
    protected Bitmap overlay(Bitmap bmpDes,     // destination image
                             Bitmap bmpSrc,     // source image: text or shape
                             Bitmap bmpDither,  // dithered image
                             int xOffset,       // image offset from border
                             int yOffset)       // starting point on destination image
    {
        if(null!=bmpSrc)
        {
            int pixelBlack = 0;
            int pixelWhite = 1;

            int yEnd = yOffset + bmpSrc.getHeight();
            int xEnd = xOffset + bmpSrc.getWidth();

            for(int y=yOffset; y<yEnd; y++)
            {
                for(int x=xOffset; x<xEnd; x++)
                {
                    int xx = x-xOffset;
                    int yy = y-yOffset;
                    int src = bmpSrc.getPixel(xx, yy);

                    if(src != pixelBlack) {

                        int des = bmpDither.getPixel(xx, yy);
                        bmpDes.setPixel(x, y, des);
                    }
                }
            }
        }
        return bmpDes;
    }

    /*
     * 1. go render left + right random images
     * 2. go interlace above images
     */
    public RandomDotData createInterlaced(Bitmap bmpShape)
    {
        // create the random dot stereo pair
        RandomDotData randomDotData = createStereoPair(bmpShape);

        // slice-n-dice, interlace them into one image

        /*
         * CTY ... to do here !
         */

        return randomDotData;
    }
}


