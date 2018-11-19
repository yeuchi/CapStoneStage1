package com.example.ctyeung.capstonestage1;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ctyeung.capstonestage1.data.ShapePreview;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.database.MsgContract;
import com.example.ctyeung.capstonestage1.database.MsgData;
import com.example.ctyeung.capstonestage1.database.MsgTuple;
import com.example.ctyeung.capstonestage1.utilities.BitmapRenderer;
import com.example.ctyeung.capstonestage1.utilities.DateTimeUtil;

import java.util.List;

/*
 * Text fragment - compose text message in this fragment
 */
public class TabFragment2 extends BaseFragment
        implements LoaderManager.LoaderCallbacks
{

    public static String PNG_FILENAME = "textSVG.png";
    private EditText mFooter;
    private  MsgTuple mMsgTuple;
    private MsgData mMsgData;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        mRoot = inflater.inflate(R.layout.tab_fragment_2, container, false);
        mContext = mRoot.getContext();
        mMsgData = new MsgData(mContext);
        initTextviews();
        getLoaderManager().initLoader(1, null, this);

        return mRoot;
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] args = {MsgTuple.BLANK};
        CursorLoader cursorLoader = new CursorLoader(mContext,
                MsgContract.CONTENT_URI,
                null,
                MsgContract.Columns.COL_TIME_STAMP+"=?",
                args,
                null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object o)
    {
        MsgData msgData = new MsgData(mContext);
        List<MsgTuple> tuples = msgData.parseResult((Cursor) o);

        if(null!=tuples && tuples.size()>0) {
            mMsgTuple = tuples.get(0);

            if (null != mMsgTuple) {
                String header = (!mMsgTuple.header.contains(MsgTuple.BLANK)) ?
                        mMsgTuple.header :
                        "";

                mEditText.setText(header);

                String footer = (!mMsgTuple.footer.contains(MsgTuple.BLANK)) ?
                        mMsgTuple.footer :
                        "";

                mFooter.setText(footer);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    private void initTextviews()
    {
        mEditText = mRoot.findViewById(R.id.txt_msg_header);

        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus)
                   updateText(MsgContract.Columns.COL_MSG_HEADER, mEditText);
           }
       });


        mFooter = mRoot.findViewById(R.id.txt_msg_footer);
        mFooter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus)
                    updateText(MsgContract.Columns.COL_MSG_FOOTER, mFooter);
            }
        });
    }

    protected void updateText(String key,
                              EditText editText)
    {
        String string = editText.getText().toString();
        mMsgData.update(mMsgTuple.id, key, string);
        SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext, true);
    }

}