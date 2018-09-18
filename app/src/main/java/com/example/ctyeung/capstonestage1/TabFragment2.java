package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ctyeung.capstonestage1.data.ShapePreview;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.utilities.BitmapRenderer;

/*
 * Text fragment - compose text message in this fragment
 */
public class TabFragment2 extends Fragment
{

    public static String PNG_FILENAME = "textSVG.png";

    private Context context;
    private View root;
    private ShapePreview shapePreview;
    private InputMethodManager mgr;
    private SharedPrefUtility sharedPref;
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.tab_fragment_2, container, false);

        initKeyListener();

        shapePreview = new ShapePreview(root);
        context = root.getContext();
        SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, context, false);
        return root;
    }

    /*
     * initialize key listener
     */
    private void initKeyListener()
    {
        editText = root.findViewById(R.id.txt_msg_header);
        editText.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_SPACE)) {

                    //do code

                    return true;

                }
                return false;
            }
        });
    }

    /*
     * setUserVisibleHint - switching fragment or activity
     * - if isDirty: take a snap shot of the svg and archive to file for preview fragment
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            showKeyboard();
        }
        else
        {
            hideKeyboard();

            if(null!= shapePreview && shapePreview.isDirty) // selected SVG layout
            {
                RelativeLayout view = root.findViewById(R.id.shapes_view_group);
                String path = BitmapRenderer.Archive(context, view, PNG_FILENAME);
                SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, context, true);
            }
        }
    }

    /*
     * show android keyboard
     */
    private void showKeyboard()
    {
        if(null==mgr)
            mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /*
     * hide android keyboard
     */
    private void hideKeyboard()
    {
        if(null!=mgr)
        {
            EditText editText = root.findViewById(R.id.txt_msg_header);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
}