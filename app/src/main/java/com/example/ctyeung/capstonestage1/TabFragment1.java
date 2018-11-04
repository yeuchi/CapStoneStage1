package com.example.ctyeung.capstonestage1;

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

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;

/*
 * 1st fragment: handle submission - Gmail, Google Drive, Facebook, etc
 *
 * Set method of persistence (gmail, facebook, google-drive, etc)
 */
public class TabFragment1 extends BaseFragment
{
    private Button mBtnSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.tab_fragment_1, container, false);
        mContext = mRoot.getContext();
        mEditText = mRoot.findViewById(R.id.txt_subject);

        initButtonEvents();
        sendEnable();
        return mRoot;
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

    protected Uri getImageUri(String key)
    {
        String path = SharedPrefUtility.getString(key, mContext);
        File file = new File(path);
        Uri uri = FileProvider.getUriForFile(mContext, "com.example.ctyeung.capstonestage1.fileprovider", file);
        return uri;
    }

    protected void share()
    {
        try
        {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Uri uriLeft = getImageUri(SharedPrefUtility.FILE_LEFT);
            Uri uriRight = getImageUri(SharedPrefUtility.FILE_RIGHT);

            ArrayList<Uri> uris = new ArrayList<Uri>();
            uris.add(uriLeft);
            uris.add(uriRight);

            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            emailIntent.setType("image/*");

            // Subject
            String subject = mEditText.getText().toString();
            if(null!=subject && subject.length()>0)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

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