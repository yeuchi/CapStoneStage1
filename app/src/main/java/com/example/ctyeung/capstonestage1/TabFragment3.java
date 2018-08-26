package com.example.ctyeung.capstonestage1;

import android.content.Context;
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
import com.example.ctyeung.capstonestage1.data.ShapePreviewHelper;
import com.example.ctyeung.capstonestage1.data.ShapeSVG;
import com.example.ctyeung.capstonestage1.utilities.JSONhelper;
import com.example.ctyeung.capstonestage1.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class TabFragment3 extends Fragment implements ShapeGridAdapter.ListItemClickListener{

    private ShapeGridAdapter mAdapter;
    private RecyclerView mNumbersList;
    private Toast mToast;
    private ShapeGridAdapter.ListItemClickListener mListener;

    private SVGImageView imageView;
    private List<ShapeSVG> shapes;
    private Context context;
    private View root;
    private ShapePreviewHelper shapePreviewHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tab_fragment_3, container, false);
        shapePreviewHelper = new ShapePreviewHelper(root);
        mListener = this;
        context = root.getContext();
        mNumbersList = (RecyclerView) root.findViewById(R.id.rv_shapes);

        GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
        mNumbersList.setLayoutManager(layoutManager);

        requestShapes();
        return root;
    }

    private void requestShapes()
    {
        URL url = NetworkUtils.buildShapesJsonUrl();
        GithubQueryTask task = new GithubQueryTask();
        task.execute(url);
    }

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
        if(shapePreviewHelper.childCount(false)>0)
            updateSVGsLayout(true);

        insertSVG(selected);
    }

    /*
     * Update RelativeLayout view that contains N SVGs.
     * - call this after insert or removal of SVG to resize + align SVGs for best fit.
     * - input signature 'plusOne' => true when insert.
     * - input signature 'plusOne' => false when remove.
     */
    private void updateSVGsLayout(boolean plusOne)
    {
        int len = shapePreviewHelper.minLength(plusOne);
        int padX = shapePreviewHelper.paddingX(plusOne);
        int padY = shapePreviewHelper.paddingY(plusOne);
        for(int i=0; i<shapePreviewHelper.childCount(false); i++)
        {
            RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(len, len);
            SVGImageView svg = (SVGImageView)shapePreviewHelper.layout.getChildAt(i);
            rllp.leftMargin = padX + (i * len);
            rllp.topMargin = padY;

            svg.setLayoutParams(rllp);
        }
    }

    /*
     * Insert a SVG into RelativeLayout view container.
     * - input signature 'selected' is a ShapeSVG object.
     */
    private void insertSVG(ShapeSVG selected)
    {
        int len = shapePreviewHelper.minLength(true);
        int padX = shapePreviewHelper.paddingX(true);
        int padY = shapePreviewHelper.paddingY(true);
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(len, len);
        rllp.topMargin = padY;
        rllp.leftMargin = padX + shapePreviewHelper.childCount(false)*len;

        // create new addition
        SVGImageView svgImageView = new SVGImageView(context);
        svgImageView.setSVG(selected.GetSVG());
        shapePreviewHelper.layout.addView(svgImageView, rllp);

        // add a click listener for removal
        svgImageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                // delete item from RelativeLayout parent container.
                ((ViewGroup)view.getParent()).removeView(view);
                updateSVGsLayout(false);
            }
        });
    }
}