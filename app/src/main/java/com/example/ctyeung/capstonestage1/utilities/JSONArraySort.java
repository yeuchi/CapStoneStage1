package com.example.ctyeung.capstonestage1.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ctyeung on 8/19/17.
 */

public class JSONArraySort
{
    private JSONArray mJsonArray;
    private String mKey;

    public JSONArraySort(JSONArray jsonArray, String key)
    {
        this.mJsonArray = jsonArray;
        this.mKey = key;
    }

    public JSONArray sort() {
        JSONArray sorted = new JSONArray();

        // step through all objects
        for (int i = 0; i < mJsonArray.length(); i++) {
            JSONObject json = JSONhelper.parseJsonFromArray(mJsonArray, i);
            String value = JSONhelper.parseValueByKey(json, mKey);
            Double num = Double.parseDouble(value);
            int index = bisection(num);
            sorted.put(index);
        }
        return sorted;
    }

    /*
     * find appropriate index to insert
     */
    protected int bisection(Double num)
    {
        int leftIndex = 0;
        int rightIndex = mJsonArray.length()-1;

        while(rightIndex>leftIndex)
        {
            int midIndex = (rightIndex + leftIndex ) / 2;
            JSONObject json = JSONhelper.parseJsonFromArray(mJsonArray, midIndex);
            String value = JSONhelper.parseValueByKey(json, mKey);
            Double n = Double.parseDouble(value);

            if(n < num)
            {
                leftIndex = midIndex;
            }
            else
            {
                rightIndex = midIndex;
            }
        }

        // almost there... but not placing at the end of the list for larger value
        return leftIndex;
    }
}
