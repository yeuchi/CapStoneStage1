package com.example.ctyeung.capstonestage1.data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.example.ctyeung.capstonestage1.R;

import java.util.ArrayList;
import java.util.List;

/*
 * Linear layout View at the bottom of Shape fragment
 * - this is the container that holds and display all the SVG selectable elements.
 * - like to use this for text fragment as well.
 */
public class ShapePreview
{
    public RelativeLayout layout;
    private View view;
    public boolean isDirty = false;
    public List<String> shapeMessage;

    public ShapePreview(View view)
    {
        shapeMessage = new ArrayList<String>();
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

    /*
     * Update RelativeLayout view that contains N SVGs.
     * - call this after insert or removal of SVG to resize + align SVGs for best fit.
     * - input signature 'plusOne' => true when insert.
     * - input signature 'plusOne' => false when remove.
     */
    public void updateSVGsLayout(boolean plusOne)
    {
        int len = this.minLength(plusOne);
        int padX = this.paddingX(plusOne);
        int padY = this.paddingY(plusOne);
        for(int i=0; i<this.childCount(false); i++)
        {
            RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(len, len);
            SVGImageView svg = (SVGImageView)this.layout.getChildAt(i);
            rllp.leftMargin = padX + (i * len);
            rllp.topMargin = padY;

            svg.setLayoutParams(rllp);
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
        rllp.leftMargin = padX + this.childCount(false)*len;

        // create new addition
        Context context = view.getContext();
        SVGImageView svgImageView = new SVGImageView(context);
        SVG svg = selected.GetSVG();

        // only if svg loaded ok
        if(null!=svg)
        {
            svgImageView.setSVG(svg);
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
                    updateSVGsLayout(false);
                }
            });
        }
    }
}
