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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.gmail.GMailSender;

/*
 * 1st fragment: handle submission - Gmail, Google Drive, Facebook, etc
 *
 * Set method of persistence (gmail, facebook, google-drive, etc)
 */
public class TabFragment1 extends Fragment
{
    private Context mContext;
    private View mRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.tab_fragment_1, container, false);
        mContext = mRoot.getContext();
        setParams();

        return mRoot;
    }

    public void setParams()
    {
        String title = "";
        int selectedLayout = -1;

        SharedPrefUtility.MediaTypeEnum mediaType = SharedPrefUtility.getMediaType(mContext);

        switch (mediaType)
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

    protected void onClickEmail()
    {
        /*
         * Need to attach image -- need to finish renderer
         */
        try
        {
            GMailSender sender = new GMailSender("username@gmail.com", "password");
            sender.sendMail("This is Subject",
                    "This is Body",
                    "user@gmail.com",
                    "user@yahoo.com");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }
}