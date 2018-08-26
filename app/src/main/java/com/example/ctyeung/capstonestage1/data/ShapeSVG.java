package com.example.ctyeung.capstonestage1.data;

import org.json.JSONObject;

import com.caverock.androidsvg.SVG;
import com.example.ctyeung.capstonestage1.utilities.JSONhelper;
import com.example.ctyeung.capstonestage1.utilities.ShapeHelper;

public class ShapeSVG
{
    private JSONObject json;
    private SVG svg;

    public ShapeSVG(JSONObject json)
    {
        this.json = json;
    }

    public String getName()
    {
        return JSONhelper.parseValueByKey(json, ShapeHelper.KEY_NAME);
    }

    public String getJSONString()
    {
        return json.toString();
    }

    public SVG GetSVG()
    {
        return svg;
    }

    public SVG Create(String str)
    {
        try
        {
            svg = SVG.getFromString(str);
            if(null==svg)
                throw new Exception("Invalid SVG string");

            return svg;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}
