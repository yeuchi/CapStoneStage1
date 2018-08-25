package com.example.ctyeung.capstonestage1;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.example.ctyeung.capstonestage1.data.ShapeSVG;
import com.example.ctyeung.capstonestage1.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ShapeGridAdapter extends RecyclerView.Adapter<ShapeGridAdapter.NumberViewHolder> {


    private static final String TAG = ShapeGridAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;


    private int viewHolderCount;
    private int mNumberItems;
    private List<ShapeSVG> shapes;
    private String[] listSVG = null;


    public interface ListItemClickListener
    {
        void onListItemClick(int clickItemIndex);
    }

    public ShapeGridAdapter( String[] listSVG,
                             List<ShapeSVG> list,
                             ListItemClickListener listener) {
       // this.movies = movies;
        mOnClickListener = listener;
        viewHolderCount = 0;
        this.shapes = list;
        this.listSVG = listSVG;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        viewHolderCount++;

        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return this.shapes.size();
    }

    /**
     * Cache of the children views for a list item.
     */
    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView viewHolderName;
        SVGImageView viewHolderImage;

        public NumberViewHolder(View itemView) {
            super(itemView);

            viewHolderName = (TextView) itemView.findViewById(R.id.txt_movie);
            viewHolderImage = (SVGImageView) itemView.findViewById(R.id.iv_shape);
            itemView.setOnClickListener(this);
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param position Position of the item in the list
         */
        void bind(int position)
        {
            ShapeSVG shapeSVG = shapes.get(position);
            viewHolderName.setText(shapeSVG.getName());
            URL url = NetworkUtils.buildSVGUrl(shapeSVG.getName());
            GithubQueryTask task = new GithubQueryTask();
            task.execute(url);

            Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: " + viewHolderCount);
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
                handleSVG(str);
            }
        }

        protected void handleSVG(String str)
        {
            if(null!=str && !str.isEmpty())
            {
               /* int len=200;
                RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(len,len);
                rllp.bottomMargin=20;
                rllp.topMargin=20;
                rllp.leftMargin=20;
                rllp.rightMargin=20;

                viewHolderImage.setLayoutParams(rllp);
                */

                try
                {
                    SVG svg = SVG.getFromString(str);
                    if(null==svg)
                        throw new Exception("Invalid SVG string");

                    viewHolderImage.setSVG(svg);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        @Override
        public void onClick(View view)
        {
            int clickPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickPosition);
        }

    }
}
