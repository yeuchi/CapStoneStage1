package com.example.ctyeung.capstonestage1.data;

import com.example.ctyeung.capstonestage1.utilities.JSONhelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShapeFactory
{
    public static List<ShapeSVG> CreateShapeList(JSONArray shapeList)
    {
        List<ShapeSVG> movies = new ArrayList<ShapeSVG>();
        for(int i=0; i<shapeList.length(); i++)
        {
            JSONObject json = JSONhelper.parseJsonFromArray(shapeList, i);
            ShapeSVG shapeSVG = new ShapeSVG(json);
            movies.add(i, shapeSVG);
        }
        return movies;
    }
}
