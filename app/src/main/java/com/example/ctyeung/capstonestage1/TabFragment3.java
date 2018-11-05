package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.graphics.drawable.PictureDrawable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.example.ctyeung.capstonestage1.data.ShapeFactory;
import com.example.ctyeung.capstonestage1.data.ShapePreview;
import com.example.ctyeung.capstonestage1.data.ShapeSVG;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.utilities.BitmapRenderer;
import com.example.ctyeung.capstonestage1.utilities.JSONhelper;
import com.example.ctyeung.capstonestage1.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/*
 * Shape fragment - compose shape(s) message in this fragment
 */
public class TabFragment3 extends ShapeFragment
        implements ShapeGridAdapter.ListItemClickListener,
        ShapeGridAdapter.SVGLoadListener
{
    public static String PNG_FILENAME = "shapeSVG.png";

    private ShapeGridAdapter mAdapter;
    private RecyclerView mNumbersList;
    private ShapeGridAdapter.ListItemClickListener mListener;
    private ShapeGridAdapter.SVGLoadListener mLoadListener;

    private View mRoot;

    private boolean mLoaded = false;
    private int mNumSVGLoaded = 0;
    private int mNumSVGsuccess = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.tab_fragment_3, container, false);
        mLoaded = false;
        mListener = this;
        mLoadListener = this;
        mContext = mRoot.getContext();

        initGrid();
        initButton();
        return mRoot;
    }

    private void initButton()
    {
        /*
         * clear message and persist
         */
        Button btnClear = mRoot.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShapePreview.empty();
                mShapePreview.updateLayout(false);
                persist();
            }
        });
    }

    /*
     * Load & initialize shape assets from network
     */
    private void initGrid()
    {
        mShapePreview = new ShapePreview(mRoot, R.id.shapes_view_group);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 5);
        mNumbersList = (RecyclerView) mRoot.findViewById(R.id.rv_shapes);
        mNumbersList.setLayoutManager(layoutManager);

        requestShapes();
    }

    /*
     * handleShapeJson - network returns json of svg list
     * - go load these svgs from network into grid-view
     */
    @Override
    protected boolean handleShapeJson(String str)
    {
        if(true == super.handleShapeJson(str))
        {
            if(null!=mShapes &&
                    mShapes.size()>0)
            {
                populateShapeGrid();
                return true;
            }
        }
        return false;
    }

    /*
     * store shapeMessage into SharedPreference
     */
    private void persist()
    {
        // array to string
        String shapeMessage = "";
        for(String msg : mShapePreview.shapeMessage)
            shapeMessage += msg +",";

        // write to sharedPreference
        SharedPrefUtility.setString(SharedPrefUtility.FRAG_SHAPE, mContext, shapeMessage);
        SharedPrefUtility.setIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext, true);
    }

    /*
     * setUserVisibleHint - switching view or activity
     * - if isDirty: take a snap shot of the svg and archive to file for preview fragment
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
        }
        else if(null!=mContext)
        {
            //do when hidden
            if(null!=mShapePreview && mShapePreview.isDirty) // selected SVG layout
            {
                RelativeLayout view = mRoot.findViewById(R.id.shapes_view_group);
                persist();
            }
        }
    }

    /*
     * populatShapeGrid - initialize grid-view with svg in each recycler view
     */
    private void populateShapeGrid()
    {
        mAdapter = new ShapeGridAdapter(mShapes, mListener, mLoadListener);
        mNumbersList.setAdapter(mAdapter);
        mNumbersList.setHasFixedSize(true);
    }

    /*
     * click event handler for SVG Grid list at bottom of fragment.
     * - insert selected SVG into RelativeLayout preview container.
     * - input signature 'clickItemIndex' is index inside List<ShapeSVG>
     */
    @Override
    public void onListItemClick(int clickItemIndex)
    {
        // retrieve selected shape svg
        ShapeSVG selected = mShapes.get(clickItemIndex);

        mShapePreview.insertSVG(selected);
        mShapePreview.shapeMessage.add(Integer.toString(clickItemIndex));

        // resize existing children
        if(mShapePreview.childCount(false)>0)
            mShapePreview.updateLayout(false);
    }

    /*
     * count how many SVG has been loaded
     */
    @Override
    public void onLoadSVGComplete(boolean success)
    {
        mNumSVGLoaded ++;

        if(success)
            mNumSVGsuccess ++;

        if(false == mLoaded)
        {
            /*
             * Must wait until all svgs are rendered in ShapeGridAdapter
             */
            if (10 <= mNumSVGLoaded)
            {
                if (mNumSVGsuccess == mNumSVGLoaded)
                    renderUserMessage();

                else {
                    String outof = mContext.getResources().getString(R.string.out_of);
                    String loadok = mContext.getResources().getString(R.string.svg_loaded_ok);
                    Toast.makeText(getActivity(),
                            mNumSVGsuccess + outof + mNumSVGLoaded + loadok,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /*
     * load & render user's last message
     */
    protected void renderUserMessage()
    {
        // load shape message (last user input) from SharedPreference
        String str = SharedPrefUtility.getString(SharedPrefUtility.FRAG_SHAPE, mContext);
        if(null!=str && !str.isEmpty())
        {
            String[] shapeMessage = str.split(",");
            for(String msg : shapeMessage)
            {
                try {
                    int i = Integer.parseInt(msg);
                    onListItemClick(i);
                }
                catch (Exception ex)
                {
                    Toast.makeText(getActivity(),
                            (String)ex.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}