package com.example.ctyeung.capstonestage1.utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapRenderer
{
    public static String IMAGE_DIR = "imageDir";

    /*
     * dither random dot background
     * - should use poisson distribution to produce
     *   more visually appealing random images.
     *   But for now .. random function to start.
     */
    public static Bitmap dither(int imageHeight)
    {
        Bitmap bitmap = Bitmap.createBitmap(imageHeight,
                                            imageHeight,
                                            Bitmap.Config.ARGB_8888);

        for(int y=0; y<bitmap.getHeight(); y++)
        {
            for(int x=0; x<bitmap.getWidth(); x++)
            {
                int random = (int)(Math.random() * 255);
                int pixel = Color.argb(255, random, random, random);
                bitmap.setPixel(x,y, pixel);
            }
        }
        return bitmap;
    }

    /*
     * Create a bitmap from View; in fragments: text + shape
     */
    public static Bitmap create(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                                            view.getHeight(),
                                            Bitmap.Config.ARGB_8888);

        return bitmap;
    }

    /*
     * Save bitmap to file in fragments: text + shape
     * https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
     */
    public static String Archive(Context context,
                                  Bitmap bitmap,
                                  String filename)  // shapes.png
    {
        ContextWrapper wrapper = new ContextWrapper(context);
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

    public static Bitmap Load(String filename)
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
