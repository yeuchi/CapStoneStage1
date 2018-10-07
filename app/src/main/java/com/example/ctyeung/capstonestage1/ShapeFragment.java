package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.ctyeung.capstonestage1.data.ShapeFactory;
import com.example.ctyeung.capstonestage1.data.ShapePreview;
import com.example.ctyeung.capstonestage1.data.ShapeSVG;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.utilities.JSONhelper;
import com.example.ctyeung.capstonestage1.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ShapeFragment extends Fragment
{
    protected List<ShapeSVG> mShapes;
    protected ShapePreview mShapePreview;
    protected Context mContext;

    /*
     * SVGs in grid-view is available on network.
     * - go fetch the path and load it in background
     */
    protected void requestShapes()
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

        /*
         * Async network retrieval ok
         */
        protected void onPostExecute(String str)
        {
            if(null!=str && !str.isEmpty())
                handleShapeJson(str);
        }
    }

    /*
     * handleShapeJson - network returns json of svg list
     * - go load these svgs from network into grid-view
     */
    protected boolean handleShapeJson(String str)
    {
        mShapes = null;
        JSONObject json = JSONhelper.parseJson(str);
        if(null != json)
        {
            JSONArray jsonArray = JSONhelper.getJsonArray(json, "shapes");
            if(null!=jsonArray)
            {
                mShapes = ShapeFactory.CreateShapeList(jsonArray);
                return true;
            }
        }
        return false;
    }

}
