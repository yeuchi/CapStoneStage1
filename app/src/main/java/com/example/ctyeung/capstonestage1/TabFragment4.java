package com.example.ctyeung.capstonestage1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.example.ctyeung.capstonestage1.data.PreviewContainer;
import com.example.ctyeung.capstonestage1.data.RandomDotData;
import com.example.ctyeung.capstonestage1.data.ShapePreview;
import com.example.ctyeung.capstonestage1.data.ShapeSVG;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.database.MsgContract;
import com.example.ctyeung.capstonestage1.database.MsgData;
import com.example.ctyeung.capstonestage1.utilities.BitmapRenderer;
import com.example.ctyeung.capstonestage1.utilities.BitmapUtil;
import com.example.ctyeung.capstonestage1.utilities.FileUtils;
import com.example.ctyeung.capstonestage1.utilities.NetworkLoader;
import com.example.ctyeung.capstonestage1.utilities.NetworkUtils;
import com.example.ctyeung.capstonestage1.utilities.RandomDotRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

/*
 * Preview fragment - load bitmaps and dither them for preview.
 * 1. load shape data from network
 * 2. load svgs from network
 * 3. render svgs into UI
 * 4. render bitmaps from svgs
 * 5. dither and display
 */
public class TabFragment4 extends ShapeFragment
    implements NetworkLoader.OnResponseListener
{
    private Context mContext;
    private SharedPrefUtility.DotModeEnum mDotModeEnum;
    private RandomDotRenderer mDotRenderer;
    private View mRoot;
    private int mNumShapes;
    private int mSVGAvailable;
    private String[] mShapeMessage;
    private PreviewContainer mPreviewContainer;
    private boolean mIsVisible = false;
    protected String rendering = "...";
    protected String nothing2Render = "...";
    protected String createSvgViewFailed = "";
    protected String createBmpFailed = "";
    protected String saveBmpFailed = "";


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {

        mRoot = inflater.inflate(R.layout.tab_fragment_4, container, false);
        mContext = mRoot.getContext();
        initStrings();

        mPreviewContainer = new PreviewContainer(mRoot, R.id.image_container);
        mShapePreview = new ShapePreview(mRoot, R.id.shapes_view_group);

        requestShapes();
        return mRoot;
    }

    protected void initStrings()
    {
        rendering = mContext.getResources().getString(R.string.rendering);
        nothing2Render = mContext.getResources().getString(R.string.nothing_to_render);
        createSvgViewFailed = mContext.getResources().getString(R.string.create_svg_view_failed);
        createBmpFailed = mContext.getResources().getString(R.string.create_bmp_failed);
        saveBmpFailed = mContext.getResources().getString(R.string.save_bmp_failed);

    }

    /*
     * Network response with shape json data
     * -> load json of different shapes
     */
    @Override
    protected boolean handleShapeJson(String str)
    {
        if (null!=mContext) {
            if (true == super.handleShapeJson(str))
            {
                InvokeRendering();
                return true;
            }
        }
        return false;
    }

    protected void reset()
    {
        mNumShapes = 0;
        mSVGAvailable = 0;
        mPreviewContainer.empty();
        mShapePreview.empty();
    }

    protected void InvokeRendering()
    {
        if(mIsVisible && null!=mShapes)
        {
            reset();
            renderSVGs();
        }
    }

    /*
     * setUserVisibleHint - switching view or activity
     * - check for bitmap updates -> re-render !
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        mIsVisible = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            showSpinner(rendering);

            InvokeRendering();

            hideSpinner();
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
    protected void renderSVGs()
    {
        // load shape data
        String shapeString = SharedPrefUtility.getString(SharedPrefUtility.FRAG_SHAPE, mContext);

        if(null==shapeString || shapeString.isEmpty())
        {

            Toast.makeText(mContext, nothing2Render, Toast.LENGTH_LONG).show();
            return;
        }
        loadSVGs(shapeString);
    }

    /*
     * parse list and load SVGs
     */
    protected void loadSVGs(String shapeString)
    {
        mShapeMessage = shapeString.split(",");

        if(null!=mShapeMessage)
            mNumShapes = mShapeMessage.length;

        for(String msg : mShapeMessage)
        {
            try
            {
                int i = Integer.parseInt(msg);
                ShapeSVG shapeSVG = mShapes.get(i);
                URL url = NetworkUtils.buildSVGUrl(shapeSVG.getPath());
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
     * create the view with SVGs inside
     */
    protected boolean createSVGView()
    {
        try {
            for (String msg : mShapeMessage) {
                int i = Integer.parseInt(msg);
                ShapeSVG shapeSVG = mShapes.get(i);
                mShapePreview.insertSVG(shapeSVG);
                mShapePreview.shapeMessage.add(Integer.toString(i));

                // resize existing children
                if(mShapePreview.childCount(false)>0)
                    mShapePreview.updateLayout(false);
            }
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText(mContext, createSvgViewFailed, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /*
     * Do rendering here ... Heavy lifting !
     */
    protected void render()
    {
        // create the view
        if(!createSVGView())
        {
            Toast.makeText(mContext, createSvgViewFailed, Toast.LENGTH_SHORT).show();
            return;
        }

        // create the raster bitmap image to work with
        Bitmap bmpShape = createBitmap();
        if(null==bmpShape)
        {
            Toast.makeText(mContext, createBmpFailed, Toast.LENGTH_SHORT).show();
            return;
        }

        // if something to render
        RandomDotData randomDotData = createRandomDot(bmpShape);
        if(null==bmpShape)
        {
            Toast.makeText(mContext, createBmpFailed, Toast.LENGTH_SHORT).show();
            return;
        }

        // display render content
        display(randomDotData);
    }

    /*
     * draw view onto bitmap
     *
     * Reference: code from this article
     * https://stackoverflow.com/questions/2801116/converting-a-view-to-bitmap-without-displaying-it-in-android
     */
    protected Bitmap createBitmap()
    {
        View view = mShapePreview.getView();
        view.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        Bitmap bitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }

    /*
     * calculate + create left / right view pairs
     * - interlace them as necessary
     */
    protected RandomDotData createRandomDot(Bitmap bitmap)
    {
        if(null!=bitmap)
        {
            // render random dot type by configuration setting
            mDotModeEnum = SharedPrefUtility.getDotMode(mContext);

            int longLength = (bitmap.getHeight() > bitmap.getWidth())?bitmap.getHeight() : bitmap.getWidth();

            if (null == mDotRenderer)
                mDotRenderer = new RandomDotRenderer(mContext, longLength);

            switch (mDotModeEnum) {
                case INTERLACED:
                    return mDotRenderer.createInterlaced(bitmap);

                default:
                case STEREO_PAIR:
                    return mDotRenderer.createStereoPair(bitmap);
            }
        }
        return null;
    }

    /*
     * display rendered content, create / insert containers
     * - assume landscape for now
     */
    protected void display(RandomDotData randomDotData)
    {
        // fragment not constructed yet
        if(null==mRoot)
            return;

        // nothing to display
        if(null==randomDotData)
            return;


        // clean up image container
        mPreviewContainer.empty();
        mDotModeEnum = SharedPrefUtility.getDotMode(mContext);

        // determine unique image name index from db

        switch (mDotModeEnum) {
            case INTERLACED:

            default:
            case STEREO_PAIR:
                for (int i = 0; i < randomDotData.count(); i++) {
                    Bitmap bmp = randomDotData.seek(i);
                    mPreviewContainer.insertStereoImage(bmp);
                    if(isExternalStorageWritable())
                    {
                        String key = (0==i)?
                                SharedPrefUtility.FILE_LEFT:
                                SharedPrefUtility.FILE_RIGHT;

                        String filename = BitmapUtil.getShapeName(key);
                        File file = BitmapUtil.saveBitmap(bmp, filename, mContext);
                        if(null!=file)
                        {
                            SharedPrefUtility.setString(key, mContext, file.getPath());
                            updateDBTuple(file.getParent());
                        }
                        else
                        {
                            Toast.makeText(mContext, saveBmpFailed, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        }
    }

    private boolean updateDBTuple(String path)
    {
        try {
            int id = SharedPrefUtility.getInteger(SharedPrefUtility.TUPLE_ID, mContext);
            MsgData msgData = new MsgData(mContext);

            String name = MsgContract.Columns.COL_IMAGE_PATH;
            return msgData.update(id, name, path);
        }
        catch (Exception ex)
        {
            return false;
        }
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}