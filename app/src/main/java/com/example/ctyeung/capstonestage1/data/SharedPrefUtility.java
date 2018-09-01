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
