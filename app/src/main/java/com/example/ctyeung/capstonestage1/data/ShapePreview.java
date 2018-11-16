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
    protected View mView;
    public ViewGroup mLayout;
    public boolean mIsDirty = false;
    public List<String> mShapeMessage;

    public ShapePreview(View view, int layoutId)
    {
        mShapeMessage = new ArrayList<String>();
        this.mView = view;
        mLayout = mView.findViewById(layoutId);
        mLayout.setVisibility(View.VISIBLE);
    }

    /*
     * view that contains all the svg
     */
    public View getView()
    {
        return mLayout;
    }

    public int height()
    {
        return mLayout.getHeight();
    }

    public int width()
    {
        return mLayout.getWidth();
    }

    public int maxShapeHeight()
    {
        return mLayout.getHeight();
    }

    public int maxShapeWidth(boolean plusOne)
    {
        int count = childCount(plusOne);
        int width = mLayout.getWidth();

        if(0==count)
            return width;

        return mLayout.getWidth() / childCount(plusOne);
    }

    public int childCount(boolean plusOne)
    {
       return mLayout.getChildCount() +
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
        return (mLayout.getWidth() - childCount(plusOne) * minLength(plusOne)) /2;
    }

    public int paddingY(boolean plusOne)
    {
        return (mLayout.getHeight() - minLength(plusOne)) / 2;
    }

    public void empty()
    {
        this.mLayout.removeAllViews();
        mShapeMessage.clear();
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
            View view = this.mLayout.getChildAt(i);

            if(ShapeHelper.isEnglish())
                params.leftMargin = padX + (i * len);
            else
                params.rightMargin = padX + (i * len);

            params.topMargin = padY;
            mView.setLayoutParams(params);
        }
    }

    /*
     * Insert a SVG into RelativeLayout view container.
     * - input signature 'selected' is a ShapeSVG object.
     */
    public void insertSVG(ShapeSVG selected)
    {
        mIsDirty = true; // view has been updated

        int len = this.minLength(true);
        int padX = this.paddingX(true);
        int padY = this.paddingY(true);
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(len, len);
        rllp.topMargin = padY;

        if(ShapeHelper.isEnglish())
            rllp.leftMargin = padX + this.childCount(false)*len;

        else
            rllp.rightMargin = padX + this.childCount(false)*len;

        // create new addition
        Context context = mView.getContext();
        SVGImageView svgImageView = new SVGImageView(context);
        SVG svg = selected.GetSVG();

        // only if svg loaded ok
        if(null!=svg)
        {
            svgImageView.setSVG(svg);
            this.mLayout.addView(svgImageView, rllp);

            // add a click listener for removal
            svgImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    mIsDirty = true; // changes has been made

                    // get view id and remove from shapeMessage
                    int index = mLayout.indexOfChild(view);
                    mShapeMessage.remove(index);

                    // delete item from RelativeLayout parent container.
                    ((ViewGroup) mView.getParent()).removeView(view);
                    updateLayout(false);
                }
            });
        }
    }
}
