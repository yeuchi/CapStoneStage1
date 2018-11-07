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

import com.example.ctyeung.capstonestage1.data.PreviewContainer;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.utilities.BitmapUtil;

import java.io.File;

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
    private boolean isFirstTime = true;
    protected ViewTreeObserver.OnGlobalLayoutListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        mContext = this.getBaseContext();
        mRoot = findViewById(R.id.root);
        mPreviewContainer = new PreviewContainer(mRoot, R.id.image_container);

        final ViewTreeObserver viewTreeObserver = mRoot.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {

            listener = new ViewTreeObserver.OnGlobalLayoutListener() {

                /*
                 * This is called ONLY once !!!
                 */
                @Override
                public void onGlobalLayout() {
                    if(isFirstTime)
                    loadLastRandomDots();
                    isFirstTime = false;
                }
            };
            viewTreeObserver.addOnGlobalLayoutListener(listener);
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
    protected void loadLastRandomDots()
    {
        reset();

        Bitmap bmpLeft = loadFromFile(SharedPrefUtility.FILE_LEFT);
        Bitmap bmpRight = loadFromFile(SharedPrefUtility.FILE_RIGHT);

        if(null!=bmpLeft && null!=bmpRight) {
            mPreviewContainer.insertStereoImage(bmpLeft);
            mPreviewContainer.insertStereoImage(bmpRight);
        }
    }

    /*
     * For loading left, right or interlacted image
     */
    protected Bitmap loadFromFile(String key)
    {
        String path = SharedPrefUtility.getString(key, mContext);
        Bitmap bmp = BitmapUtil.load(path);
        return bmp;
    }
}
