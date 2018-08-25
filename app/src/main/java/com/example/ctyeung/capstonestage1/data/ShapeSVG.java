package com.example.ctyeung.capstonestage1.data;

import org.json.JSONObject;
import com.example.ctyeung.capstonestage1.utilities.JSONhelper;
import com.example.ctyeung.capstonestage1.utilities.ShapeHelper;

public class ShapeSVG
{
    private JSONObject json;

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

}
