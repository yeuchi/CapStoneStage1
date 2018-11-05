package com.example.ctyeung.capstonestage1.data;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.example.ctyeung.capstonestage1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Linear layout View at the bottom of Shape fragment
 * - this is the container that holds and display all the SVG selectable elements.
 * - like to use this for text fragment as well.
 */
public class ShapePreview
{
    public ViewGroup layout;
    protected View view;
    public boolean isDirty = false;
    public List<String> shapeMessage;

    public ShapePreview(View view, int layoutId)
    {
        shapeMessage = new ArrayList<String>();
        this.view = view;
        layout = view.findViewById(layoutId);
        layout.setVisibility(View.VISIBLE);
    }

    /*
     * view that contains all the svg
     */
    public View getView()
    {
        return layout;
    }

    public int height()
    {
        return layout.getHeight();
    }

    public int width()
    {
        return layout.getWidth();
    }

    public int maxShapeHeight()
    {
        return layout.getHeight();
    }

    public int maxShapeWidth(boolean plusOne)
    {
        int count = childCount(plusOne);
        int width = layout.getWidth();

        if(0==count)
            return width;

        return layout.getWidth() / childCount(plusOne);
    }

    public int childCount(boolean plusOne)
    {
       return layout.getChildCount() +
               ((true==plusOne)? 1:0);
    }

    public int minLength(boolean plusOne)
    {
        int h = maxShapeHeight();
        int w = maxShapeWidth(plusOne);
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

    public void empty()
    {
        this.layout.removeAllViews();
        shapeMessage.clear();
    }

    /*
     * Update RelativeLayout view that contains N SVGs.
     * - call this after insert or removal of SVG to resize + align SVGs for best fit.
     * - input signature 'plusOne' => true when insert.
     * - input signature 'plusOne' => false when remove.
     */
    public void updateLayout(boolean plusOne)
    {
        int len = this.minLength(plusOne);
        int padX = this.paddingX(plusOne);
        int padY = this.paddingY(plusOne);
        int count = this.childCount(false);

        for(int i=0; i<count; i++)
        {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(len, len);
            View view = this.layout.getChildAt(i);
            params.leftMargin = padX + ((ShapeHelper.isEnglish())?
                                        i * len:            // english: left -> right
                                        (count-1-i)*len);   // arabic: right -> left
            params.topMargin = padY;
            view.setLayoutParams(params);
        }
    }

    /*
     * Insert a SVG into RelativeLayout view container.
     * - input signature 'selected' is a ShapeSVG object.
     */
    public void insertSVG(ShapeSVG selected)
    {
        isDirty = true; // view has been updated

        int len = this.minLength(true);
        int padX = this.paddingX(true);
        int padY = this.paddingY(true);
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(len, len);
        rllp.topMargin = padY;
        rllp.leftMargin = padX + ((ShapeHelper.isEnglish())?
                                    this.childCount(false)*len: 0);

        // create new addition
        Context context = view.getContext();
        SVGImageView svgImageView = new SVGImageView(context);
        SVG svg = selected.GetSVG();

        // only if svg loaded ok
        if(null!=svg)
        {
            svgImageView.setSVG(svg);

            if(ShapeHelper.isEnglish())
                this.layout.addView(svgImageView, 0, rllp);
            else
                this.layout.addView(svgImageView, rllp);

            // add a click listener for removal
            svgImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    isDirty = true; // changes has been made

                    // get view id and remove from shapeMessage
                    int index = layout.indexOfChild(view);
                    shapeMessage.remove(index);

                    // delete item from RelativeLayout parent container.
                    ((ViewGroup) view.getParent()).removeView(view);
                    updateLayout(false);
                }
            });
        }
    }
}
