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
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;

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
        void onShareViaDialogOKClick(SharedPrefUtility.MediaTypeEnum selectedMedia);
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
                listener.onShareViaDialogOKClick(SharedPrefUtility.MediaTypeEnum.GMAIL);
            }
        });

        Button btnFacebook = root.findViewById(R.id.btn_facebook);
        btnFacebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to MainActivity
                listener.onShareViaDialogOKClick(SharedPrefUtility.MediaTypeEnum.FACEBOOK);
            }
        });

        Button btnDrive = root.findViewById(R.id.btn_drive);
        btnDrive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to MainActivity
                listener.onShareViaDialogOKClick(SharedPrefUtility.MediaTypeEnum.GOOGLE_DRIVE);
            }
        });

        Button btnCancel = root.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to MainActivity
                listener.onShareViaDialogOKClick(SharedPrefUtility.MediaTypeEnum.CANCEL);
            }
        });
    }
}
