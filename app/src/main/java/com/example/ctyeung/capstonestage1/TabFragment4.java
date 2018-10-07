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
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.example.ctyeung.capstonestage1.data.RandomDotData;
import com.example.ctyeung.capstonestage1.data.ShapeSVG;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.utilities.BitmapRenderer;
import com.example.ctyeung.capstonestage1.utilities.NetworkLoader;
import com.example.ctyeung.capstonestage1.utilities.NetworkUtils;
import com.example.ctyeung.capstonestage1.utilities.RandomDotRenderer;

import java.net.URL;

/*
 * Preview fragment - load bitmaps and dither them for preview.
 */
public class TabFragment4 extends ShapeFragment
    implements NetworkLoader.OnResponseListener
{
    private Context mContext;
    private SharedPrefUtility.DotModeEnum mDotModeEnum;
    private RandomDotRenderer dotRenderer;
    private View root;
    private int mNumShapes;
    private int mSVGAvailable;

    @Override
    protected boolean handleShapeJson(String str)
    {
        if(true == super.handleShapeJson(str))
        {
            if(null!=mShapes &&
                    mShapes.size()>0)
            {
                renderIfDirty();
                return true;
            }
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mNumShapes = 0;
        mSVGAvailable = 0;
        View root = inflater.inflate(R.layout.tab_fragment_4, container, false);
        mContext = root.getContext();
        requestShapes();
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
     * load last image persisted after change (text + shape)
     * - maybe only load image-dirty ?
     */
    protected void renderIfDirty()
    {
        // has text or shapes changed ?
        boolean isShapeDirty = SharedPrefUtility.getIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext);

        if(isShapeDirty)
        {
            // load shape data
            String shapeString = SharedPrefUtility.getString(SharedPrefUtility.FRAG_SHAPE, mContext);
            if(null==shapeString || shapeString.isEmpty())
            {
                Toast.makeText(mContext, "Nothing to render", Toast.LENGTH_LONG).show();
                return;
            }
            loadSVGs(shapeString);
        }
    }

    /*
     * parse list and load SVGs
     */
    protected void loadSVGs(String shapeString)
    {
        String[] shapeMessage = shapeString.split(",");

        if(null!=shapeMessage)
            mNumShapes = shapeMessage.length;

        for(String msg : shapeMessage)
        {
            try
            {
                int i = Integer.parseInt(msg);
                ShapeSVG shapeSVG = mShapes.get(i);
                URL url = NetworkUtils.buildSVGUrl(shapeSVG.getName());
                NetworkLoader loader = new NetworkLoader(this, shapeSVG, url);
            }
            catch (Exception ex)
            {
                Toast.makeText(getActivity(),
                        (String)ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
     * Callback from SVG retrieval
     * -> Go create SVG instance
     */
    public void onResponse(ShapeSVG shapeSVG,
                           String str)
    {
        if(null!=str && !str.isEmpty())
        {
            SVG svg = shapeSVG.Create(str);
            mSVGAvailable ++;

            /*
             * done loading SVG, go render forth !
             */
            if(mNumShapes == mSVGAvailable)
                render();
        }
    }

    /*
     * Do rendering here ... Heavy lifting !
     */
    protected void render()
    {
        /*
         * 03Oct18 WIP -> load SVGs and draw to bitmap
         */
        RandomDotData randomDotData = null;
        Bitmap bmpShape = null;

        //BitmapRenderer.Load(TabFragment3.PNG_FILENAME);

        // if something to render
        if(null!=bmpShape)
        {
            // render random dot type by configuration setting
            mDotModeEnum = SharedPrefUtility.getDotMode(mContext);

            if (null == dotRenderer)
                dotRenderer = new RandomDotRenderer(mContext);

            switch (mDotModeEnum) {
                case INTERLACED:
                    randomDotData = dotRenderer.createInterlaced(bmpShape);
                    break;

                default:
                case STEREO_PAIR:
                    randomDotData = dotRenderer.createStereoPair(bmpShape);
                    break;
            }
        }

        // display render content
        display(randomDotData);

        SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext, false);
        SharedPrefUtility.setIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext, false);
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
}