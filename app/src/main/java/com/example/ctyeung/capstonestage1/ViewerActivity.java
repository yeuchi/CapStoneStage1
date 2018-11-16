package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ctyeung.capstonestage1.data.PreviewContainer;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.database.MsgData;
import com.example.ctyeung.capstonestage1.database.MsgTuple;
import com.example.ctyeung.capstonestage1.utilities.BitmapUtil;

import java.io.File;
import java.util.List;

/*
 * only landscape mode is supported for viewing of either interlaced or stereo-pair
 * - might want to consider a VR SDK for viewing stereo ?
 *
 *  1. load last random-dot-images from fragment->preview by default (on file).
 *  2. add ability to load from Google drive !
 */
public class ViewerActivity extends AppCompatActivity {

    private PreviewContainer mPreviewContainer;
    private Context mContext;
    private View mRoot;
    private TextView mTxtView;
    private boolean mIsFirstTime = true;
    protected ViewTreeObserver.OnGlobalLayoutListener mListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        mContext = this.getBaseContext();
        mRoot = findViewById(R.id.root);
        mTxtView = mRoot.findViewById(R.id.txtView);
        mPreviewContainer = new PreviewContainer(mRoot, R.id.image_container);

        final ViewTreeObserver viewTreeObserver = mRoot.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {

            mListener = new ViewTreeObserver.OnGlobalLayoutListener() {

                /*
                 * This is called ONLY once !!!
                 */
                @Override
                public void onGlobalLayout() {
                    if(mIsFirstTime)
                    loadRandomDots();
                    mIsFirstTime = false;
                }
            };
            viewTreeObserver.addOnGlobalLayoutListener(mListener);
        }
    }

    protected void reset()
    {
        mPreviewContainer.empty();
    }

    /*
     * By default, load the last random-dot-images from file
     * - assume stereo pair for now !!!!
     */
    protected void loadRandomDots()
    {
        String left = null;
        String right = null;
        String string = null;
        reset();

        /*
         * Load widget selection
         */
        int id = this.getIntent().getIntExtra("SELECT_ID", -1);
        if (id != -1) {
            MsgData msgData = new MsgData(mContext);
            List<MsgTuple> tuples = msgData.query(id);

            if (null != tuples || tuples.size() > 0) {
                MsgTuple tuple = tuples.get(0);
                left = tuple.path + "/" + BitmapUtil.getShapeName(SharedPrefUtility.FILE_LEFT);
                right = tuple.path + "/" + BitmapUtil.getShapeName(SharedPrefUtility.FILE_RIGHT);
                string = "id:" + id + " subject:" + tuple.subject + " time:" + tuple.timeStamp;
            }
        }

        /*
         * default file path
         */
        if (null == left || null == right) {
            left = SharedPrefUtility.getString(SharedPrefUtility.FILE_LEFT, mContext);
            right = SharedPrefUtility.getString(SharedPrefUtility.FILE_RIGHT, mContext);
            string = mContext.getResources().getString(R.string.latest);
        }

        if (null == left || null == right) {
            String msg = mContext.getResources().getString(R.string.no_image_available);
            Toast.makeText(this,
                    msg,
                    Toast.LENGTH_SHORT).show();

            return;
        }

        mTxtView.setText(string);
        loadImagePair(left, right);
    }

    private void loadImagePair(String left,
                               String right)
    {
        try {
            Bitmap bmpLeft = BitmapUtil.load(left);
            Bitmap bmpRight = BitmapUtil.load(right);

            if (null != bmpLeft && null != bmpRight) {
                mPreviewContainer.insertStereoImage(bmpLeft);
                mPreviewContainer.insertStereoImage(bmpRight);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,
                    "load failed:" + ex,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
