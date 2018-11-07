package com.example.ctyeung.capstonestage1.utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapUtil
{
    /*
     * bitmap to file
     * https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
     */
    public static File saveBitmap(  Bitmap bmp,
                                    String path,
                                    Context context)
    {
        //String extStorageDirectory = Environment.getDataDirectory().getPath();
        // String extStorageDirectory = Environment.getExternalStorageDirectory().getPath();
        //OutputStream outStream = null;
        // File file = new File(extStorageDirectory, imageName);

        FileUtils fileUtils = new FileUtils();
        File file = fileUtils.getAlbumStorageDir(context, path);

        try {

            if(!file.exists())
            {
                file.mkdirs();
            }

            if(!file.createNewFile())
            {
                file.delete();
                file.createNewFile();
            }

            OutputStream outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        }
        catch (Exception e) {

            e.printStackTrace();
            return null;
        }
        return file;
    }

    /*
     * Load bitmap from file
     * - for preview fragment and viewer
     */
    public static Bitmap load(String filename)
    {
        try
        {
            File file = new File(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * view to bitmap ** NOT UTILIZED **
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
}
