package com.example.ctyeung.capstonestage1;

import android.app.Activity;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
{
    private Button mBtnSend;
    private Button mBtnExit;
    private String mSubject;
    private MsgData mMsgData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.tab_fragment_1, container, false);
        mContext = mRoot.getContext();
        mEditText = mRoot.findViewById(R.id.txt_subject);

        //Activity activity = ((Activity)mContext).getParent();
        mMsgData = new MsgData((Activity)mContext);

        initButtonEvents();
        sendEnable();
        createDBTuple();
        return mRoot;
    }

    /*
     * create a tuple in db
     */
    protected void createDBTuple()
    {
        MsgTuple tuple;

        // retrieve tuple not send (no timeStamp)
        String columnName = MsgContract.Columns.COL_TIME_STAMP;
        List<MsgTuple> tuples = mMsgData.query(columnName, "blank");

        SharedPrefUtility.DotModeEnum dotMode = SharedPrefUtility.getDotMode(mContext);

        /*
         * create a tuple if none available
         */
        if(null==tuples || tuples.size()==0) {
            tuple = new MsgTuple();
            tuple.type = dotMode.toString();
            tuple.subject = "subject";
            mMsgData.insert(tuple);
        }

        if(null==tuples || tuples.size()==0) {
            String errMsg = mContext.getResources().getString(R.string.db_create_failed);
            Toast.makeText(getActivity(),
                    errMsg,
                    Toast.LENGTH_LONG).show();
            return;
        }

            // assume this is going to succeed now
        tuples = mMsgData.query(columnName, "blank");

        tuple = tuples.get(0);
        String name = MsgContract.Columns.COL_IMAGE_TYPE;
        mMsgData.update(tuple.id, name, dotMode.toString());

        // store id for use in other fragments
        SharedPrefUtility.setInteger(SharedPrefUtility.TUPLE_ID, mContext, tuple.id);
    }

    /*
     * When user selects send, update tuple
     */
    protected boolean updateDBTuple()
    {
        try
        {
            // add a timeStamp as verification of persistence
            int id = SharedPrefUtility.getInteger(SharedPrefUtility.TUPLE_ID, mContext);

            String name = MsgContract.Columns.COL_TIME_STAMP;
            String timeStamp = DateTimeUtil.getNow();
            mMsgData.update(id, name, timeStamp);

            name = MsgContract.Columns.COL_MSG_HEADER;
            String header = SharedPrefUtility.getString(SharedPrefUtility.FRAG_TEXT_HEADER, mContext);
            mMsgData.update(id, name, header);

            name = MsgContract.Columns.COL_MSG_FOOTER;
            String footer = SharedPrefUtility.getString(SharedPrefUtility.FRAG_TEXT_FOOTER, mContext);
            mMsgData.update(id, name, footer);

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
                share();
                if(updateDBTuple()) {
                    updateWidget();

                    // create new tuple for additional user composition
                    createDBTuple();
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

    private void sendEnable()
    {
        if(null==mContext || null==mBtnSend)
            return;

        Boolean isShapeDirty = SharedPrefUtility.getIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext);
        Boolean isTextDirty = SharedPrefUtility.getIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext);
        Boolean isEnabled = (isShapeDirty && isTextDirty)? true:false;
        mBtnSend.setEnabled(isEnabled);
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
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            sendEnable();
            showKeyboard();
        }
        else
        {
            hideKeyboard();
        }
    }

    private void updateWidget()
    {
        Application app = ((Activity)mContext).getApplication();
        /*
         * Ravi Rupareliya's solution to update widget
         * https://stackoverflow.com/questions/28941472/update-listview-widget-with-application
         */
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] appWidgetIds= appWidgetManager.getAppWidgetIds(new ComponentName(app, HomeScreenWidget.class));

        //Toast.makeText(this,"ids:size:"+appWidgetIds.length,Toast.LENGTH_SHORT).show();
        HomeScreenWidget myWidget = new HomeScreenWidget();
        myWidget.onUpdate(mContext, AppWidgetManager.getInstance(mContext),appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetList);
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

            // Subject
            mSubject = mEditText.getText().toString();
            if(null!=mSubject && mSubject.length()>0)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, mSubject);

            // need to insert image in the middle ...
            String header_title = mContext.getResources().getString(R.string.header)+": ";
            String footer_title = mContext.getResources().getString(R.string.header)+": ";

            String header = header_title+SharedPrefUtility.getString(SharedPrefUtility.FRAG_TEXT_HEADER, mContext);
            String footer = footer_title+SharedPrefUtility.getString(SharedPrefUtility.FRAG_TEXT_FOOTER, mContext);
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