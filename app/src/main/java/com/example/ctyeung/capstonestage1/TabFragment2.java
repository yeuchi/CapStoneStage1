package com.example.ctyeung.capstonestage1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
{

    public static String PNG_FILENAME = "textSVG.png";
    private EditText mFooter;
    private MsgData mMsgData;
    private MsgTuple mTuple;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        mRoot = inflater.inflate(R.layout.tab_fragment_2, container, false);
        mContext = mRoot.getContext();
        mMsgData = new MsgData(mContext);

        initTextviews();
        loadText();
        return mRoot;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            loadText();
    }

    private void initTextviews()
    {
        mEditText = mRoot.findViewById(R.id.txt_msg_header);
        mEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                int id = SharedPrefUtility.getInteger(SharedPrefUtility.TUPLE_ID, mContext);
                mMsgData.update(id, MsgContract.Columns.COL_MSG_HEADER, s.toString());
                SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        mFooter = mRoot.findViewById(R.id.txt_msg_footer);
        mFooter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                int id = SharedPrefUtility.getInteger(SharedPrefUtility.TUPLE_ID, mContext);
                mMsgData.update(id, MsgContract.Columns.COL_MSG_FOOTER, s.toString());
                SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    /*
     * load text from SharedPreference
     */
    private void loadText()
    {
        int id = SharedPrefUtility.getInteger(SharedPrefUtility.TUPLE_ID, mContext);
        List<MsgTuple> tuples = mMsgData.query(id);
        if(null==tuples || 0==tuples.size())
        {
            String msg = mContext.getResources().getString(R.string.db_query_failed);
            Toast.makeText(getActivity(),
                    msg,
                    Toast.LENGTH_SHORT).show();
        }

        mTuple = tuples.get(0);

        if(null!=mTuple) {
            String header = (!mTuple.header.contains("blank"))?
                            mTuple.header:
                            mContext.getResources().getString(R.string.header_default_msg);

                mEditText.setText(header);

            String footer = (!mTuple.header.contains("blank"))?
                    mTuple.footer:
                    mContext.getResources().getString(R.string.footer_default_msg);

                mFooter.setText(footer);
        }
    }
}