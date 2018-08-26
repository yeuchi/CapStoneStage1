package com.example.ctyeung.capstonestage1.data;

import android.view.View;
import android.widget.RelativeLayout;

import com.example.ctyeung.capstonestage1.R;

public class ShapePreviewHelper
{
    public RelativeLayout layout;
    private View view;

    public ShapePreviewHelper(View view)
    {
        this.view = view;
        layout = (RelativeLayout)view.findViewById(R.id.shapes_view_group);
    }

    public int height()
    {
        return layout.getHeight();
    }

    public int width()
    {
        return layout.getWidth();
    }

    public int maxSVGHeight()
    {
        return layout.getHeight();
    }

    public int maxSVGWidth(boolean plusOne)
    {
        return layout.getWidth() / childCount(plusOne);
    }

    public int childCount(boolean plusOne)
    {
       return layout.getChildCount() +
               ((true==plusOne)? 1:0);
    }

    public int minLength(boolean plusOne)
    {
        int h = maxSVGHeight();
        int w = maxSVGWidth(plusOne);
        return (h>w)?w:h;
    }

    public int paddingX(boolean plusOne)
    {
        return (layout.getWidth() - childCount(plusOne) * minLength(plusOne)) /2;
    }

    public int paddingY(boolean plusOne)
    {
        return (layout.getHeight() - minLength(plusOne)) / 2;
    }
}
