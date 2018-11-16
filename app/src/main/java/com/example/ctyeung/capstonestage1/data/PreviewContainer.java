package com.example.ctyeung.capstonestage1.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.example.ctyeung.capstonestage1.R;

import java.util.ArrayList;
import java.util.List;

public class PreviewContainer
{
    protected View mView;
    public ViewGroup mLayout;
    public boolean mIsDirty = false;

    public PreviewContainer(View view,      // fragment view
                            int layoutId)
    {
        this.mView = view;
        mLayout = view.findViewById(layoutId);
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
        return mView.getHeight();
    }

    public int width()
    {
        return mView.getWidth();
    }

    protected int padX = 1;

    protected int calStereoImageWidth()
    {
        return width()/2-padX;
    }

    protected int calStereoImageHeight()
    {
        if(width() < height())  // portrait
        {
            return (width()+2*padX)/2;
        }
        else // landscape
        {
            return height()+2*padX;
        }
    }

    public void insertStereoImage(Bitmap bmp)
    {
        mIsDirty = true; // view has been updated

        int padY = 1;

        int w = calStereoImageWidth();
        int h = calStereoImageHeight();
        LinearLayout.LayoutParams params;

        if(w>0 && h>0) {
            params = new LinearLayout.LayoutParams(w, h);
            params.topMargin = padY;
            params.leftMargin = padX;
            params.rightMargin = padX;
        }
        else
        {
            // worst case scenario -- stick it in the view
            params = new LinearLayout.LayoutParams(100, 100);
        }

        // create new addition
        Context context = mView.getContext();
        ImageView imageView = new ImageView(context);

        if(null!=imageView)
        {
            imageView.setImageBitmap(bmp);
            this.mLayout.addView(imageView, params);
        }
    }

    public void empty()
    {
        /*
         * empty SVG rendering -- if available
         */
        int shapesViewGroupId = R.id.shapes_view_group;
        RelativeLayout childLayout = mView.findViewById(shapesViewGroupId);
        if(null!=childLayout)
            childLayout.removeAllViews();

        /*
         * empty random dot images
         */
        int count = mLayout.getChildCount();
        for(int i=count-1; i>=0; i--)
        {
            View view = mLayout.getChildAt(i);
            if(view.getId() != R.id.shapes_view_group)
                mLayout.removeViewAt(i);
        }
    }
}
