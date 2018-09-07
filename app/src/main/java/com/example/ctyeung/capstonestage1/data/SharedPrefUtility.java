package com.example.ctyeung.capstonestage1.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ctyeung on 12/24/17.
 */

public class SharedPrefUtility
{
    public static final String mypreference = "mypref";
    public static final String MAIN_SCROLL = "mainScroll";
    public static final String TEXT_IS_DIRTY = "textIsDirty";
    public static final String SHAPE_IS_DIRTY = "shapeIsDirty";

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
     * Text fragment: is dirty - preview needs re-rendering
     */
    public static boolean getTextDirty(Context context)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);

        return (sharedPreferences.contains(TEXT_IS_DIRTY))?
                sharedPreferences.getBoolean(TEXT_IS_DIRTY, false):
                false;
    }

    public static void setTextDirty(Context context,
                                    boolean isDirty)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TEXT_IS_DIRTY, isDirty);
        editor.commit();
    }

    /*
     * Shape fragment: is dirty - preview needs re-rendering
     */
    public static boolean getShapeDirty(Context context)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);

        return (sharedPreferences.contains(SHAPE_IS_DIRTY))?
                sharedPreferences.getBoolean(SHAPE_IS_DIRTY, false):
                false;
    }

    public static void setShapeDirty(Context context,
                                     boolean isDirty)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHAPE_IS_DIRTY, isDirty);
        editor.commit();
    }

    private static SharedPreferences getSharedPref(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
