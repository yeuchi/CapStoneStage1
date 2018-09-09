package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class TabFragment3 extends Fragment implements ShapeGridAdapter.ListItemClickListener{

    public static String PNG_FILENAME = "shapeSVG.png";

    private ShapeGridAdapter mAdapter;
    private RecyclerView mNumbersList;
    private Toast mToast;
    private ShapeGridAdapter.ListItemClickListener mListener;

    private SVGImageView imageView;
    private List<ShapeSVG> shapes;
    private Context context;
    private View root;
    private ShapePreview shapePreview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tab_fragment_3, container, false);
        shapePreview = new ShapePreview(root);
        mListener = this;
        context = root.getContext();
        mNumbersList = (RecyclerView) root.findViewById(R.id.rv_shapes);

        GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
        mNumbersList.setLayoutManager(layoutManager);

        requestShapes();
        SharedPrefUtility.setIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, context, false);
        return root;
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
        else
        {
            //do when hidden
            if(null!=shapePreview && shapePreview.isDirty) // selected SVG layout
            {
                RelativeLayout view = root.findViewById(R.id.shapes_view_group);
                String path = BitmapRenderer.Archive(context, view, PNG_FILENAME);
                SharedPrefUtility.setIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, context, true);
            }
        }
    }

    /*
     * SVGs in grid-view is available on network.
     * - go fetch the path and load it in background
     */
    private void requestShapes()
    {
        URL url = NetworkUtils.buildShapesJsonUrl();
        GithubQueryTask task = new GithubQueryTask();
        task.execute(url);
    }

    /*
     * Async task to load SVG list (grid view images)
     */
    public class GithubQueryTask extends AsyncTask<URL, Void, String>
    {
        public GithubQueryTask()
        {

        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String githubSearchResults = null;
            try
            {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            return githubSearchResults;
        }

        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        protected void onPostExecute(String str)
        {
            handleShapeJson(str);
        }
    }

    /*
     * handleShapeJson - network returns json of svg list
     * - go load these svgs from network into grid-view
     */
    private void handleShapeJson(String str)
    {
        shapes = null;
        JSONObject json = JSONhelper.parseJson(str);
        if(null != json)
        {
            JSONArray jsonArray = JSONhelper.getJsonArray(json, "shapes");
            if(null!=jsonArray)
                shapes = ShapeFactory.CreateShapeList(jsonArray);

            if(null!=shapes &&
                    shapes.size()>0)
            {
                populateShapeGrid();
                return;
            }
        }
    }

    /*
     * populatShapeGrid - initialize grid-view with svg in each recycler view
     */
    private void populateShapeGrid()
    {
        mAdapter = new ShapeGridAdapter(shapes, mListener);
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
        if(mToast!=null)
            mToast.cancel();

        // retrieve selected shape svg
        ShapeSVG selected = shapes.get(clickItemIndex);

        // resize existing children
        if(shapePreview.childCount(false)>0)
            shapePreview.updateSVGsLayout(true);

        shapePreview.insertSVG(selected);
    }



}