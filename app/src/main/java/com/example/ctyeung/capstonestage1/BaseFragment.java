package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;

import java.io.File;

public class BaseFragment extends Fragment
{
    protected Context mContext = null;
    protected View mRoot;

    protected InputMethodManager mgr;
    protected EditText mEditText;

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

    protected void persist()
    {

    }

    /*
     * show android keyboard
     */
    protected void showKeyboard()
    {
        if(null==mContext)
            return;

        if(null==mgr)
            mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mgr.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    /*
     * hide android keyboard
     */
    protected void hideKeyboard()
    {
        if(null!=mgr)
        {
            mgr.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }
}
