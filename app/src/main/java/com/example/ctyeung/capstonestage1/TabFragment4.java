package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ctyeung.capstonestage1.data.RandomDotData;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.utilities.BitmapRenderer;
import com.example.ctyeung.capstonestage1.utilities.RandomDotRenderer;

/*
 * Preview fragment - load bitmaps and dither them for preview.
 */
public class TabFragment4 extends Fragment {

    private Context mContext;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.tab_fragment_4, container, false);
        mContext = root.getContext();

        // Render Preview here ...


        Button btnSend = (Button)root.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Send message
            }
        });

        Button btnCancel = (Button)root.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to MainActivity
                Intent homeIntent = new Intent(mContext, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });

        return root;
    }

    /*
     * setUserVisibleHint - switching view or activity
     * - check for bitmap updates -> re-render !
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            // has text or shapes changed ?
            boolean isTextDirty = SharedPrefUtility.getIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext);
            boolean isShapeDirty = SharedPrefUtility.getIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext);
            if(isTextDirty || isShapeDirty)
            {
                // re-render preview
                Bitmap bmpText = BitmapRenderer.Load(TabFragment2.PNG_FILENAME);
                Bitmap bmpShape = BitmapRenderer.Load(TabFragment3.PNG_FILENAME);
                RandomDotData data = RandomDotRenderer.CreateInterlaced(bmpText, bmpShape);
            }
        }
        else
        {

        }
    }
}