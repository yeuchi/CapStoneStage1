package com.example.ctyeung.capstonestage1.data;

public class ShapeHelper
{
    final public static String KEY_NAME = "name";

    final public static String BASE_URL = "http://ctyeung.com/Udacity/capstone";
    final public static String ASSETS_DIR = "/assets";

    public static String GetAssetInfo()
    {
        return BASE_URL + "/shapes.json";
    }

    public static String GetAssetPathPrefix()
    {
        return BASE_URL + ASSETS_DIR;
    }
}
