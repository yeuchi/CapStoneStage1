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

    private Context mContext = null;
    private View mRoot;
    private InputMethodManager mgr;
    private EditText mHeader;
    private EditText mFooter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        mRoot = inflater.inflate(R.layout.tab_fragment_2, container, false);
        mContext = mRoot.getContext();

        // no text change .. yet
        loadText();
        return mRoot;
    }

    /*
     * load text from SharedPreference
     */
    private void loadText()
    {
        mHeader = mRoot.findViewById(R.id.txt_msg_header);
        mFooter = mRoot.findViewById(R.id.txt_msg_footer);

        String stringHeader = SharedPrefUtility.getString(SharedPrefUtility.FRAG_TEXT_HEADER, mContext);
        if(null!=stringHeader && !stringHeader.isEmpty())
            mHeader.setText(stringHeader);

        String stringFooter = SharedPrefUtility.getString(SharedPrefUtility.FRAG_TEXT_FOOTER, mContext);
        if(null!=stringFooter && !stringFooter.isEmpty())
            mFooter.setText(stringFooter);
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
        else if(null!=mContext)
        {
            hideKeyboard();
            persist();
        }
    }

    /*
     * Save text (if changed -> isDirty)
     */
    private void persist()
    {
        // stored
        String storedHeader = SharedPrefUtility.getString(SharedPrefUtility.FRAG_TEXT_HEADER, mContext);
        String storedFooter = SharedPrefUtility.getString(SharedPrefUtility.FRAG_TEXT_FOOTER, mContext);

        // current text
        String stringHeader = mHeader.getText().toString();
        String stringFooter = mFooter.getText().toString();

        // persist
        if(storedHeader != stringHeader || storedFooter != stringFooter) {
            SharedPrefUtility.setString(SharedPrefUtility.FRAG_TEXT_HEADER, mContext, stringHeader);
            SharedPrefUtility.setString(SharedPrefUtility.FRAG_TEXT_FOOTER, mContext, stringFooter);
            SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext, true);
        }
    }

    /*
     * show android keyboard
     */
    private void showKeyboard()
    {
        if(null==mgr)
            mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mgr.showSoftInput(mHeader, InputMethodManager.SHOW_IMPLICIT);
    }

    /*
     * hide android keyboard
     */
    private void hideKeyboard()
    {
        if(null!=mgr)
        {
            mgr.hideSoftInputFromWindow(mHeader.getWindowToken(), 0);
        }
    }
}