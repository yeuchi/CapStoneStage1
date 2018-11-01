package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;

import java.io.File;
import java.io.OutputStream;

/*
 * 1st fragment: handle submission - Gmail, Google Drive, Facebook, etc
 *
 * Set method of persistence (gmail, facebook, google-drive, etc)
 */
public class TabFragment1 extends Fragment
{
    private Context mContext;
    private View mRoot;
    private SharedPrefUtility.MediaTypeEnum mMediaType;
    private Button mBtnSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.tab_fragment_1, container, false);
        mContext = mRoot.getContext();
        setParams();
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
                switch(mMediaType)
                {
                    case FACEBOOK:
                        postOnFacebook();
                    break;

                    case GMAIL:
                        sendGmail();
                        break;

                    case GOOGLE_DRIVE:
                        save2GoogleDrive();
                        break;
                }
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

    public void setParams()
    {
        String title = "";
        int selectedLayout = -1;

        mMediaType = SharedPrefUtility.getMediaType(mContext);

        switch (mMediaType)
        {
            case FACEBOOK:
            title = mRoot.getResources().getString(R.string.btn_facebook);
            selectedLayout = R.id.layout_facebook;
            break;

            case GMAIL:
            title = mRoot.getResources().getString(R.string.btn_gmail);
            selectedLayout = R.id.layout_gmail;
            break;

            case GOOGLE_DRIVE:
            title = mRoot.getResources().getString(R.string.btn_google_drive);
            selectedLayout = R.id.layout_drive;
            break;

            default:
            title = mRoot.getResources().getString(R.string.unknown_media);
            break;
        }

        // set fragment title
        TextView textView = mRoot.findViewById(R.id.txt_media_title);
        textView.setText(title);
        setLayoutVisibility(selectedLayout);
    }

    /*
     * Set all layouts invisible except selected
     */
    private void setLayoutVisibility(int id)
    {
        LinearLayout layout = mRoot.findViewById(R.id.layout_gmail);
        layout.setVisibility(View.INVISIBLE);

        layout = mRoot.findViewById(R.id.layout_facebook);
        layout.setVisibility(View.INVISIBLE);

        layout = mRoot.findViewById(R.id.layout_drive);
        layout.setVisibility(View.INVISIBLE);

        if(id > -1) {
            layout = mRoot.findViewById(id);
            layout.setVisibility(View.VISIBLE);
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
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            sendEnable();
        }
        else
        {

        }
    }

    protected void postOnFacebook()
    {

    }

    protected void save2GoogleDrive()
    {

    }

    protected void sendGmail()
    {
        try
        {
            /*
             * Stack overflow
             * https://stackoverflow.com/questions/32344927/send-image-in-message-body-of-email-android
             */

            String path = SharedPrefUtility.getString(SharedPrefUtility.FILE_LEFT, mContext);
            File file = new File(path);
            Uri uri = Uri.fromFile(file);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            //emailIntent.setType("plain/text");
            emailIntent.setType("image/*");

            // TO: recipient
            String Recipient = (String)mContext.getResources().getText(R.string.recipient);
            EditText txtRecipient = mRoot.findViewById(R.id.txt_gmail_recipient);
            String recipient = txtRecipient.getText().toString();
            if(null!=recipient && recipient.length()>0 && recipient != Recipient)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient);

            // CC: carbon copy
            String CC = (String)mContext.getResources().getText(R.string.carbon_copy);
            EditText txtCC = mRoot.findViewById(R.id.txt_gmail_cc);
            String cc = txtCC.getText().toString();
            if(null!=cc && cc.length()>0 && !cc.equals(CC))
                emailIntent.putExtra(Intent.EXTRA_CC, cc);

            // BCC: blind carbon copy
            String BCC = (String)mContext.getResources().getText(R.string.blind_carbon_copy);
            EditText txtBCC = mRoot.findViewById(R.id.txt_gmail_bcc);
            String bcc = txtBCC.getText().toString();
            if(null!=bcc && bcc.length()>0 && !bcc.equals(BCC))
                emailIntent.putExtra(Intent.EXTRA_BCC, bcc);

            // Subject
            EditText txtSubject = mRoot.findViewById(R.id.txt_gmail_subject);
            String subject = txtSubject.getText().toString();
            if(null!=subject && subject.length()>0)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

            // need to insert image in the middle ...
            String header = "Header:"+SharedPrefUtility.getString(SharedPrefUtility.FRAG_TEXT_HEADER, mContext);
            String footer = "Footer:"+SharedPrefUtility.getString(SharedPrefUtility.FRAG_TEXT_FOOTER, mContext);
            emailIntent.putExtra(Intent.EXTRA_TEXT, header + "\n\n" + footer);

            // load image
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

            // will have to assume email was send.  Reset clean
            SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext, false);
            SharedPrefUtility.setIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext, false);
        }
        catch (Exception e)
        {
            Log.e("SendMail", e.getMessage(), e);
        }
    }
}