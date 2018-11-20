package com.example.ctyeung.capstonestage1;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.database.MsgContract;
import com.example.ctyeung.capstonestage1.database.MsgData;
import com.example.ctyeung.capstonestage1.database.MsgTuple;
import com.example.ctyeung.capstonestage1.utilities.DateTimeUtil;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * 1st fragment: handle submission - Gmail, Google Drive, Facebook, etc
 *
 * Set method of persistence (gmail, facebook, google-drive, etc)
 */
public class TabFragment1 extends BaseFragment
        implements LoaderManager.LoaderCallbacks
{
    private Button mBtnSend;
    private Button mBtnExit;
    private MsgData mMsgData;
    private MsgTuple mTuple;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.tab_fragment_1, container, false);
        mContext = mRoot.getContext();
        mMsgData = new MsgData(mContext);

        initTextview();
        initButtonEvents();
        enableBtnSend();

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
    public void onLoadFinished(@NonNull Loader loader, Object o) {

        List<MsgTuple> tuples = mMsgData.parseResult((Cursor)o);

        SharedPrefUtility.DotModeEnum dotMode = SharedPrefUtility.getDotMode(mContext);

        /*
         * create a tuple if none available
         */
        if(null==tuples || tuples.size()==0)
        {
            createDBTuple(dotMode.toString());
        }
        else
        {
            MsgTuple tuple = tuples.get(0);
            if(null == mTuple || tuple.id != mTuple.id) {
                mMsgData.update(tuple.id, MsgContract.Columns.COL_IMAGE_TYPE, dotMode.toString());
                SharedPrefUtility.setInteger(SharedPrefUtility.TUPLE_ID, mContext, tuple.id);

                /*
                 * update EditText with blank
                 */
                if(null!=mTuple) {
                    mTuple.id = tuple.id;
                    mEditText.setText("");
                }
            }
            mTuple = tuple;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    protected void createDBTuple(String dotMode)
    {
        MsgTuple tuple = new MsgTuple();
        tuple.type = dotMode.toString();
        tuple.subject = mContext.getResources().getString(R.string.subject);
        mMsgData.insert(tuple);
    }

    /*
     * initialize textview
     */
    protected void initTextview()
    {
        mEditText = mRoot.findViewById(R.id.txt_subject);

        // add change handler
        mEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(null!=mMsgData && null!=mTuple)
                    mMsgData.update(mTuple.id, MsgContract.Columns.COL_MSG_SUBJECT, s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }

    /*
     * When user selects send, update tuple
     */
    protected boolean updateDBTuple()
    {
        try
        {
            // add a timeStamp as verification of persistence
            String timeStamp = DateTimeUtil.getNow();
            mMsgData.update(mTuple.id, MsgContract.Columns.COL_TIME_STAMP, timeStamp);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    protected void initButtonEvents()
    {
        mBtnSend = mRoot.findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*
                 * send content
                 *  - hide keyboard so user can select destinations [email, facebook, etc]
                 */
                hideKeyboard();

                share();
                enableBtnSend();

                if(updateDBTuple()) {

                    // create new tuple for additional user composition
                    SharedPrefUtility.DotModeEnum dotMode = SharedPrefUtility.getDotMode(mContext);
                    createDBTuple(dotMode.toString());
                }
            }
        });

        mBtnExit = mRoot.findViewById(R.id.btn_exit);
        mBtnExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void enableBtnSend()
    {
        if(null==mContext || null==mBtnSend)
            return;

        Boolean isShapeDirty = SharedPrefUtility.getIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext);
        Boolean isTextDirty = SharedPrefUtility.getIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext);
        Boolean isEnabled = (isShapeDirty || isTextDirty)? true:false;

        if(isEnabled)
        {
            if(!mBtnSend.isEnabled())
                mBtnSend.setEnabled(true);
        }
        else
        {
            if(mBtnSend.isEnabled())
                mBtnSend.setEnabled(false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
     * setUserVisibleHint - switching view or activity
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        enableBtnSend();
        if (isVisibleToUser)
        {
            showKeyboard();
        }
        else
        {
            hideKeyboard();
        }
    }

    protected void share()
    {
        try
        {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Uri uriLeft = SharedPrefUtility.getImageUri(SharedPrefUtility.FILE_LEFT, mContext);
            Uri uriRight = SharedPrefUtility.getImageUri(SharedPrefUtility.FILE_RIGHT, mContext);

            ArrayList<Uri> uris = new ArrayList<Uri>();
            uris.add(uriLeft);
            uris.add(uriRight);

            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            emailIntent.setType("image/*");

            if(null==mTuple)
            {
                String msg = mContext.getResources().getString(R.string.db_query_failed);
                Toast.makeText(getActivity(),
                        msg,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Subject
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, mTuple.subject);

            // need to insert image in the middle ...
            String header = mContext.getResources().getString(R.string.header)+": "+mTuple.header;
            String footer = mContext.getResources().getString(R.string.footer)+": "+mTuple.footer;

            emailIntent.putExtra(Intent.EXTRA_TEXT, header + "\n\n" + footer);

            // load image
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

            if (emailIntent.resolveActivity(mContext.getPackageManager()) != null) {
                String send_title = mContext.getResources().getString(R.string.btn_send)+"...";
                mContext.startActivity(Intent.createChooser(emailIntent, send_title));
            }

            // will have to assume email was send.  Reset clean
            SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext, false);
            SharedPrefUtility.setIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext, false);
        }
        catch (Exception e)
        {
            String msg = mContext.getResources().getString(R.string.share_failed);

            Toast.makeText(getActivity(),
                    msg,
                    Toast.LENGTH_SHORT).show();

            Log.e(msg, e.getMessage(), e);
        }
    }
}