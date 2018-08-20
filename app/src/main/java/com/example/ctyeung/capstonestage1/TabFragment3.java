package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.graphics.drawable.PictureDrawable;
import android.widget.LinearLayout;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TabFragment3 extends Fragment {

    private SVGImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_fragment_3, container, false);

        Context context = root.getContext();
        String[] list;
        try
        {
            // replace with networkUtil to load assets ?

            list = context.getAssets().list("");
            GridLayout layout = root.findViewById(R.id.grid_shapes);

            for(String s : list)
            {
                SVGImageView svgImageView = new SVGImageView(context);
                svgImageView.setImageAsset(s);
                layout.addView(svgImageView, 0);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }
}