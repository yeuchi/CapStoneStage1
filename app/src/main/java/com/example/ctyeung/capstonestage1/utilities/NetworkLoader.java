package com.example.ctyeung.capstonestage1.utilities;

import android.os.AsyncTask;

import com.example.ctyeung.capstonestage1.ShapeFragment;
import com.example.ctyeung.capstonestage1.data.ShapeSVG;

import java.io.IOException;
import java.net.URL;

public class NetworkLoader
{
    protected OnResponseListener listener;
    public URL url;
    protected ShapeSVG shapeSVG;

    public interface OnResponseListener
    {
        void onResponse(ShapeSVG shapeSVG, String str);
    }

    public NetworkLoader(OnResponseListener listener,
                         ShapeSVG shapeSVG,
                         URL url)
    {
        this.shapeSVG = shapeSVG;
        this.listener = listener;
        this.url = url;

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

        /*
         * Async network retrieval ok
         */
        protected void onPostExecute(String str)
        {
            // callback
            listener.onResponse(shapeSVG, str);
        }
    }
}
