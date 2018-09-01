package com.example.ctyeung.capstonestage1.utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public static Bitmap create(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                                            view.getHeight(),
                                            Bitmap.Config.ARGB_8888);

        return bitmap;
    }

    /*
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
