package com.example.ctyeung.capstonestage1.data;

import java.util.Locale;

/*
 * only support english + arabic
 */
public class ShapeHelper
{

    public final static String ENGLISH = "English";
    public final static String EN = "en";
    public final static String AR = "ar";
    final public static String KEY_NAME = "name";
    final public static String KEY_PATH = "path";

    final public static String BASE_URL = "http://ctyeung.com/Udacity/capstone";
    final public static String ASSETS_DIR = "/assets/";

    public static String GetAssetInfo()
    {
        String suffix = (isEnglish())?EN:AR;
        return BASE_URL + "/shapes_"+suffix+".json";
    }

    public static String GetAssetPathPrefix()
    {
        String suffix = (isEnglish())?EN:AR;
        return BASE_URL + ASSETS_DIR + suffix;
    }

    public static String getLanguage()
    {
        String language = Locale.getDefault().getDisplayLanguage();
        return language;// "English"
    }

    public static boolean isEnglish()
    {
        //return (getLanguage().contains(ENGLISH))?true:false;
        return false;
    }
}
