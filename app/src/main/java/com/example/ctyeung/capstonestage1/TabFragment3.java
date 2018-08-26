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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tab_fragment_3, container, false);
        mListener = this;
        context = root.getContext();
        mNumbersList = (RecyclerView) root.findViewById(R.id.rv_shapes);

        GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
        mNumbersList.setLayoutManager(layoutManager);

        requestShapes();

        //String[] list;

        //try
        //{
            // replace with networkUtil to load assets ?

            //list = context.getAssets().list("");

            // need a factory to create list of shapes here

            //populateShapeGrid();
            /*
            GridLayout layout = root.findViewById(R.id.grid_shapes);

            for(String s : list)
            {
                SVGImageView svgImageView = new SVGImageView(context);
                svgImageView.setImageAsset(s);
                svgImageView.setBackgroundColor(88888888);
                int len=200;
                RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(len,len);
                rllp.bottomMargin=20;
                rllp.topMargin=20;
                rllp.leftMargin=20;
                rllp.rightMargin=20;

                svgImageView.setLayoutParams(rllp);

                layout.addView(svgImageView, 0);
            } */
        //}
       // catch (IOException e) {
        //    e.printStackTrace();
        //}

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
        /* for debugging only !
        String[] list = null;
        try
        {
            list = context.getAssets().list("");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        */
        mAdapter = new ShapeGridAdapter(shapes, mListener);
        mNumbersList.setAdapter(mAdapter);
        mNumbersList.setHasFixedSize(true);
    }

    @Override
    public void onListItemClick(int clickItemIndex)
    {
        if(mToast!=null)
            mToast.cancel();

        // retrieve selected shape svg
        ShapeSVG selected = shapes.get(clickItemIndex);
        String name = selected.getName();

        // Get parent view dimensions
        RelativeLayout layout = (RelativeLayout)root.findViewById(R.id.shapes_view_group);
        int numChildren = layout.getChildCount();

        int h = layout.getHeight();
        int w = layout.getWidth() / (numChildren+1);
        int len = (h>w)?w:h;

        int offsetX = (layout.getWidth() - (numChildren+1) * w) /2;
        int offsetY = (h - len)/2;

        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(len, len);
        rllp.topMargin = offsetY;

        // resize existing children
        if(numChildren>0)
        {
            for(int i=0; i<layout.getChildCount(); i++)
            {
                SVGImageView svg = (SVGImageView)layout.getChildAt(i);
                //svg.layout(left, offsetY, len, len);
                rllp.leftMargin = offsetX + (i * len);
                svg.setLayoutParams(rllp);
            }
        }

        // size new addition
        rllp = new RelativeLayout.LayoutParams(len, len);
        rllp.topMargin = offsetY;
        rllp.leftMargin = offsetX + numChildren*len;

        // create new addition
        SVGImageView svgImageView = new SVGImageView(context);
        svgImageView.setSVG(selected.GetSVG());
        svgImageView.setLayoutParams(rllp);
        layout.addView(svgImageView, rllp);
    }
}