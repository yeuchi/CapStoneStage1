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

public class PreviewContainer extends ShapePreview
{
    public PreviewContainer(View view, int layoutId)
    {
        super(view, layoutId);
    }

    @Deprecated
    public void insertSVG(ShapeSVG selected) { }

    @Override
    public void updateLayout(boolean plusOne)
    {
        int len = this.minLength(plusOne);
        int padX = this.paddingX(plusOne);
        int padY = this.paddingY(plusOne);
        for(int i=0; i<this.childCount(false); i++)
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(len, len);
            View view = this.layout.getChildAt(i);
            params.leftMargin = (0==i)?0:padX + (i * len);
            params.topMargin = padY;

            view.setLayoutParams(params);
        }
    }

    public void insertBitmap(Bitmap bmp, boolean hasPadX)
    {
        isDirty = true; // view has been updated

        int len = this.minLength(true);
        int padX = this.paddingX(true);
        int padY = this.paddingY(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(len, len);
        params.topMargin = padY;

        /*
         * need to determine space between 2 images
         */
        if(hasPadX)
            params.leftMargin = 5;

        // create new addition
        Context context = view.getContext();
        ImageView imageView = new ImageView(context);

        if(null!=imageView)
        {
            imageView.setImageBitmap(bmp);
            this.layout.addView(imageView, params);
        }
    }

    public void empty()
    {
        int shapesViewGroupId = R.id.shapes_view_group;
        RelativeLayout childLayout = view.findViewById(shapesViewGroupId);
        //childLayout.setVisibility(View.INVISIBLE);
        childLayout.removeAllViews();

        int count = layout.getChildCount();
        for(int i=count-1; i>=0; i--)
        {
            View view = layout.getChildAt(i);
            if(view.getId() != R.id.shapes_view_group)
                layout.removeViewAt(i);
        }
    }
}
