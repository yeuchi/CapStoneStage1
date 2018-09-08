package com.example.ctyeung.capstonestage1.data;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * Persistence of application data
 */
public class SharedPrefUtility
{
    public static final String mypreference = "mypref";
    public static final String TEXT_IS_DIRTY = "textIsDirty";
    public static final String SHAPE_IS_DIRTY = "shapeIsDirty";

    public static final String IMAGE_HEIGHT = "imageHeight";        // image height in pixels (width is same as height)
    public static final String INTERLACE_WIDTH = "interlaceWidth";  // number of pixels per slice

    public static final String DOT_MODE = "dotMode";
    public enum DotModeEnum
    {
        STEREO_PAIR,
        INTERLACED
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

    /*
     * Config: image height or interlace pixel width
     */
    public static int getDimension( String key,
                                    Context context)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        return sharedPreferences.getInt(key, -1);
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
        SharedPreferences sharedPreferences = getSharedPref(context);
        return (sharedPreferences.contains(key))?
                sharedPreferences.getBoolean(key, false):
                false;
    }

    public static void setIsDirty(String key,
                                  Context context,
                                  boolean isDirty)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, isDirty);
        editor.commit();
    }

    private static SharedPreferences getSharedPref(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
