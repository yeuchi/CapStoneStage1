package com.example.ctyeung.capstonestage1.utilities;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class FileUtils
{
    public File getTempFile(Context context,
                            String filename)
    {
        File file=null;
        try
        {
            //String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(filename, null, context.getCacheDir());
        }
        catch (IOException e)
        {
            // Error while creating file
        }
        return file;
    }

    public File createFile(Context context,
                      String filename)
    {
        try {
            File directory = context.getFilesDir();
            File file = new File(directory, filename);
            return file;
        }
        catch (Exception ex)
        {

        }
        return null;
    }

    /*
     * https://developer.android.com/training/data-storage/files#java
     */
    public File getPrivateAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            //Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }
}
