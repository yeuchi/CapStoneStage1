package com.example.ctyeung.capstonestage1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ctyeung.capstonestage1.gmail.GMailSender;

/*
 * 1st fragment: handle submission - Gmail, Google Drive, Facebook, etc
 *
 * Set method of persistence (gmail, facebook, google-drive, etc)
 */
public class TabFragment1 extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_1, container, false);
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