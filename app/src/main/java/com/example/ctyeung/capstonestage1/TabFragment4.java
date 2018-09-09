package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ctyeung.capstonestage1.data.RandomDotData;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.utilities.BitmapRenderer;
import com.example.ctyeung.capstonestage1.utilities.RandomDotRenderer;

/*
 * Preview fragment - load bitmaps and dither them for preview.
 */
public class TabFragment4 extends Fragment
{
    private Context mContext;
    private SharedPrefUtility.DotModeEnum mDotModeEnum;
    private RandomDotRenderer dotRenderer;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.tab_fragment_4, container, false);
        mContext = root.getContext();

        Button btnSend = (Button)root.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Send message
            }
        });

        Button btnCancel = (Button)root.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to MainActivity
                Intent homeIntent = new Intent(mContext, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });

        renderIfDirty();

        return root;
    }

    /*
     * setUserVisibleHint - switching view or activity
     * - check for bitmap updates -> re-render !
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            renderIfDirty();
        }
        else
        {
            // not visible
        }
    }

    /*
     * display rendered content, create / insert containers
     * - assume landscape for now
     */
    protected void display(RandomDotData randomDotData)
    {
        // fragment not constructed yet
        if(null==root)
            return;

        // empty the image container
        LinearLayout imageContainer = root.findViewById(R.id.image_container);
        imageContainer.removeAllViews();

        // nothing to display
        if(null==randomDotData)
            return;

        /*
         * insert preview images into container
         */
        for(int i=0; i<randomDotData.count(); i++)
        {
            ImageView imageView = new ImageView(mContext);
            Bitmap bmp = randomDotData.seek(i);
            imageView.setImageBitmap(bmp);
        }
    }

    /*
     * load last image persisted after change (text + shape)
     * - maybe only load image-dirty ?
     */
    protected void renderIfDirty()
    {
        // has text or shapes changed ?
        boolean isTextDirty = SharedPrefUtility.getIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext);
        boolean isShapeDirty = SharedPrefUtility.getIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext);

        if(isTextDirty || isShapeDirty)
        {
            render();

            SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext, false);
            SharedPrefUtility.setIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext, false);
        }
    }

    /*
     * Do rendering here ... Heavy lifting !
     */
    protected void render()
    {
        RandomDotData randomDotData = null;
        Bitmap bmpText = BitmapRenderer.Load(TabFragment2.PNG_FILENAME);
        Bitmap bmpShape = BitmapRenderer.Load(TabFragment3.PNG_FILENAME);

        // if something to render
        if(null!=bmpText || null!=bmpShape)
        {
            // render random dot type by configuration setting
            mDotModeEnum = SharedPrefUtility.getDotMode(mContext);

            if (null == dotRenderer)
                dotRenderer = new RandomDotRenderer(mContext);

            switch (mDotModeEnum) {
                case INTERLACED:
                    randomDotData = dotRenderer.createInterlaced(bmpText, bmpShape);
                    break;

                default:
                case STEREO_PAIR:
                    randomDotData = dotRenderer.createStereoPair(bmpText, bmpShape);
                    break;
            }
        }

        // display render content
        display(randomDotData);
    }
}