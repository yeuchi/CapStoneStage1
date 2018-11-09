package com.example.ctyeung.capstonestage1.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.example.ctyeung.capstonestage1.R;

import java.io.File;

/*
 * Persistence of application data
 */
public class SharedPrefUtility
{
    public static final String mypreference = "mypref";
    public static final String TEXT_IS_DIRTY = "textIsDirty";
    public static final String SHAPE_IS_DIRTY = "shapeIsDirty";

    public static final String INTERLACE_WIDTH = "interlace_width";  // number of pixels per slice
    public static final String IMAGE_HEIGHT = "image_height";        // image height in pixels (width is same as height)
    public static final String BORDER_OFFSET = "border_offset";      // image offset from background border
    public static final String PARALLAX_DIS = "parallax_distance";   // parallax distance

    public static final String COLOR1 = "color1";
    public static final String COLOR2 = "color2";
    public static final String COLOR3 = "color3";

    public static final String DOT_MODE = "dotMode";

    public static final String FRAG_TEXT_HEADER = "fragTextHeader";
    public static final String FRAG_TEXT_FOOTER = "fragTextFooter";
    public static final String FRAG_SHAPE = "fragShape";
    public static final String FILE_LEFT = "fileLeft";
    public static final String FILE_RIGHT = "fileRight";
    public static final String FILE_INTERLACE = "fileInterlace";
    public static final String IS_DEBUG = "isDebug";

    public enum DotModeEnum
    {
        STEREO_PAIR,
        INTERLACED
    }

    /*
     * uri to last random dot images
     */
    public static Uri getImageUri(String key,
                                  Context context)
    {
        String path = SharedPrefUtility.getString(key, context);
        File file = new File(path);
        Uri uri = FileProvider.getUriForFile(context, "com.example.ctyeung.capstonestage1.fileprovider", file);
        return uri;
    }

    /*
     * Config: dot mode - stereo / interlaced
     */
    public static DotModeEnum getDotMode(Context context)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        boolean isDotStereo = (sharedPreferences.contains(DOT_MODE))?
                                sharedPreferences.getBoolean(DOT_MODE, true): false;
        return (isDotStereo)? DotModeEnum.STEREO_PAIR:DotModeEnum.INTERLACED;
    }

    public static void setDotMode(Context context,
                                  DotModeEnum mode)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        boolean isDotStereo = (mode == DotModeEnum.STEREO_PAIR)? true:false;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TEXT_IS_DIRTY, isDotStereo);
        editor.commit();
    }

    protected static int getDefaultDimensionValue(String key,
                                                  Context context)
    {
        switch (key)
        {
            case COLOR1:
                return Color.argb(255, 255, 0, 0);

            case COLOR2:
                return Color.argb(255, 0, 255, 0);

            case COLOR3:
                return Color.argb(255, 0, 0, 255);

            case INTERLACE_WIDTH:
                return context.getResources().getInteger(R.integer.interlace_width);

            case IMAGE_HEIGHT:
                return context.getResources().getInteger(R.integer.image_height);

            case BORDER_OFFSET:
                return context.getResources().getInteger(R.integer.border_offset);

            case PARALLAX_DIS:
                return context.getResources().getInteger(R.integer.parallax_distance);

            default:
                return -1;
        }
    }

    /*
     * Config: image height or interlace pixel width
     */
    public static int getDimension( String key,
                                    Context context)
    {
        int defaultValue = getDefaultDimensionValue(key, context);

        SharedPreferences sharedPreferences = getSharedPref(context);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void setDimension(String key,
                                    Context context,
                                    int value)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /*
     * Text fragment: is dirty - preview needs re-rendering
     */
    public static boolean getIsDirty(String key,       // text or shape
                                     Context context)
    {
        return getBoolean(key, context);
    }

    public static void setIsDirty(String key,
                                  Context context,
                                  boolean isDirty)
    {
        setBoolean(key, context, isDirty);
    }

    public static boolean getBoolean(String key,       // text or shape
                                     Context context)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        return (sharedPreferences.contains(key))?
                sharedPreferences.getBoolean(key, false):
                false;
    }

    public static void setBoolean(String key,
                                  Context context,
                                  boolean value)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /*
     * Text or Shape Fragment : get/set string
     */
    public static String getString(String key,       // text or shape
                                    Context context)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        return (sharedPreferences.contains(key))?
                sharedPreferences.getString(key, null):
                null;
    }

    public static void setString(String key,
                                  Context context,
                                  String str)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, str);
        editor.commit();
    }

    private static SharedPreferences getSharedPref(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
