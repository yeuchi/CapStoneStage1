package com.example.ctyeung.capstonestage1.utilities;

import android.content.Context;
import android.graphics.Color;

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;

public class BaseRenderer
{
    protected Context mContext;

    public BaseRenderer(Context context)
    {
        this.mContext = context;
    }

    protected int[] getColors()
    {
        return new int[] {  Color.argb(255, 0, 0, 0),
                SharedPrefUtility.getDimension(SharedPrefUtility.COLOR1, mContext),
                SharedPrefUtility.getDimension(SharedPrefUtility.COLOR2, mContext),
                SharedPrefUtility.getDimension(SharedPrefUtility.COLOR3, mContext)};
    }
}
