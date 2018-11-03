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
    public static String IMAGE_DIR = "imageDir";

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

        int pixelWhite = Color.argb(255, 255, 255, 255);
        int pixelBlack = Color.argb(255, 0, 0, 0);

        int[] colors = getColors();

        for(int y=0; y<bitmap.getHeight(); y++)
        {
            for(int x=0; x<bitmap.getWidth(); x++)
            {
                int index = (int)(Math.random() * 4.0);
                int des = (Math.random() < 0.5)?
                        colors[index]:
                        //pixelBlack:
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

    /*
     * view to bitmap
     * https://stackoverflow.com/questions/34272310/android-convert-view-to-bitmap
     */
    protected Bitmap View2Bitmap(View view)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        //view.buildDrawingCache();

        /*
         * assume the view dimension is already a square ... !
         * do I need to scale the bitmap ???
         */
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    /*
     * bitmap to file
     * https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
     */
    protected String Persist2File(Bitmap bitmap,
                                         String filename)
    {
        ContextWrapper wrapper = new ContextWrapper(mContext);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = wrapper.getDir(IMAGE_DIR, Context.MODE_PRIVATE);
        File file = new File(directory,filename);

        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                stream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    /*
     * Load bitmap from file
     * - for preview fragment and viewer
     */
    public Bitmap Load(String filename)
    {
        try
        {
            File file = new File(IMAGE_DIR, filename);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
