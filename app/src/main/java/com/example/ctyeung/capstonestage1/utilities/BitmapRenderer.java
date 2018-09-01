package com.example.ctyeung.capstonestage1.utilities;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapRenderer
{
    public static Bitmap create(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                                            view.getHeight(),
                                            Bitmap.Config.ARGB_8888);

        return bitmap;
    }


    public static boolean Archive(  Bitmap bitmap,
                                    String filepath)
    {
        /*
         * https://stackoverflow.com/questions/41356494/how-to-get-bitmap-of-a-view
         */
        OutputStream fout = null;
        File imageFile = new File(filepath);
        try
        {
            fout = new FileOutputStream(imageFile);

            bitmap.compress(Bitmap.CompressFormat.PNG, 0, fout);
            fout.flush();
            fout.close();
            return true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
