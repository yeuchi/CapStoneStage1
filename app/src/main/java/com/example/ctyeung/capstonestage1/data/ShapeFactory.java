package com.example.ctyeung.capstonestage1.data;

import com.example.ctyeung.capstonestage1.utilities.JSONhelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * create a list collection to hold SVGs.
 * - holds all shapes in shape fragment
 * - like to use this for text fragment to hold A-Z as well.
 */
public class ShapeFactory
{
    public static List<ShapeSVG> CreateShapeList(JSONArray shapeList)
    {
        List<ShapeSVG> list = new ArrayList<ShapeSVG>();
        for(int i=0; i<shapeList.length(); i++)
        {
            JSONObject json = JSONhelper.parseJsonFromArray(shapeList, i);
            ShapeSVG shapeSVG = new ShapeSVG(json);
            list.add(i, shapeSVG);
        }
        return list;
    }
}
