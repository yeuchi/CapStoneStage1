package com.example.ctyeung.capstonestage1.dialogs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ctyeung.capstonestage1.R;

/*
 * Share Via dialog
 * - gmail, drive, facebook, etc.
 */
public class ShareFragment extends DialogFragment
{
    private Context context;
    private View root;
    private OnDialogOKListener listener;

    /*
     * parent call back listener
     */
    public interface OnDialogOKListener
    {
        void onShareViaDialogOKClick(String selection);
    }

    public ShareFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_share, container, false);
        context = root.getContext();

        initializeButtonListeners();

        return root;
    }

    public void setParams(OnDialogOKListener listener) {
        this.listener = listener;
    }

    /*
     * Button click listeners here
     */
    private void initializeButtonListeners()
    {
        Button btnGMail = root.findViewById(R.id.btn_gmail);
        btnGMail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to MainActivity
                String str = root.getResources().getString(R.string.btn_gmail);
                listener.onShareViaDialogOKClick(str);
            }
        });

        Button btnFacebook = root.findViewById(R.id.btn_facebook);
        btnFacebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to MainActivity
                String str = root.getResources().getString(R.string.btn_facebook);
                listener.onShareViaDialogOKClick(str);
            }
        });

        Button btnDrive = root.findViewById(R.id.btn_drive);
        btnDrive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to MainActivity
                String str = root.getResources().getString(R.string.btn_google_drive);
                listener.onShareViaDialogOKClick(str);
            }
        });

        Button btnCancel = root.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to MainActivity
                String str = root.getResources().getString(R.string.btn_cancel);
                listener.onShareViaDialogOKClick(str);

            }
        });
    }
}
