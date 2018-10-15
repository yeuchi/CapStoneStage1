package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.tab_fragment_1, container, false);
        mContext = mRoot.getContext();
        setParams();
        initButtonEvents();
        return mRoot;
    }

    protected void initButtonEvents()
    {
        Button btnSend = mRoot.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener()
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

            // Uri u = null;
            // u = Uri.fromFile(mFile);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            //emailIntent.setType("image/*");

            // TO: recipient
            EditText txtRecipient = mRoot.findViewById(R.id.txt_gmail_recipient);
            String recipient = txtRecipient.getText().toString();
            emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient);

            // CC: carbon copy
            EditText txtCC = mRoot.findViewById(R.id.txt_gmail_cc);
            String cc = txtCC.getText().toString();
            emailIntent.putExtra(Intent.EXTRA_CC, cc);

            // BCC: blind carbon copy
            EditText txtBCC = mRoot.findViewById(R.id.txt_gmail_bcc);
            String bcc = txtBCC.getText().toString();
            emailIntent.putExtra(Intent.EXTRA_BCC, bcc);

            // Subject
            EditText txtSubject = mRoot.findViewById(R.id.txt_gmail_subject);
            String subject = txtSubject.getText().toString();
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

            // feed.get(Selectedposition).DETAIL_OBJECT.IMG_URL
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Your tsxt here");

            // emailIntent.putExtra(Intent.EXTRA_STREAM, u);
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }
        catch (Exception e)
        {
            Log.e("SendMail", e.getMessage(), e);
        }
    }
}