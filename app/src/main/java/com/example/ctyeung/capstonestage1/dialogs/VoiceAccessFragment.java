package com.example.ctyeung.capstonestage1.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ctyeung.capstonestage1.R;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;

/*
 * Accessibility Dialog box
 * -> confirm voice command and value change
 *
 * !!! Only works for 4 numeric settings (below) for now !!!
 * 1. Parallax distance
 * 2. Image Height
 * 3. Interlace width
 * 4. Border Offset
 *
 * Tutorial:
 * 1. click on floating-action-button for accessibility
 * 2. speak one of the command (above list of 4) follow by a numeric value.
 * 3. Example "Image" "Height" "143"
 * 4. Dialog box with ok, cancel allow you to confirm action.
 */
public class VoiceAccessFragment extends DialogFragment {

    private OnDialogListener mListener;
    private TextView txtDictation;

    private Button btnOK;
    private Button btnCancel;

    private View mRoot;
    private Context mContext;

    private String mMessage;
    private String mCommand;
    private int mValue = -1;

    public interface OnDialogListener
    {
        void onDialogClick(String key, int value);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){

        mRoot = inflater.inflate(R.layout.fragment_voice_access, container, false);
        mContext = mRoot.getContext();

        txtDictation = mRoot.findViewById(R.id.txt_dictation);
        txtDictation.setText(mMessage);
        initButtons();
        parseMessage();

        return mRoot;
    }

    protected void initButtons()
    {
        btnOK = mRoot.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogClick(mCommand, mValue);
            }
        });

        btnCancel = mRoot.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // end this dialog
                mListener.onDialogClick("", -1);
            }
        });
    }

    public void setParams(VoiceAccessFragment.OnDialogListener listener,
                          String msg)
    {
        mListener = listener;
        mMessage = msg;

        if(null!=txtDictation)
            parseMessage();
    }

    protected void parseMessage()
    {
        if(null!=mMessage) {
            String[] list = mMessage.split(" ");
            if (null != list &&
                list.length >= 2 &&         // 2 or more words
            list.length < 4)                // at most 3 words
            {
                boolean isCmdOK = setCommand(list[0]);
                boolean isValueOK = setValue(list[list.length-1]);

                if(!isCmdOK || !isValueOK)
                    btnOK.setEnabled(false);
            }
        }
    }

    protected boolean setCommand(String key)
    {
        key = key.toLowerCase();

        if(key.contains("inter") ||
            key.contains("width") ||
            key.contains("with"))
        {
            mCommand = SharedPrefUtility.INTERLACE_WIDTH;
        }
        else if(key.contains("image") ||
                key.contains("height"))
        {
            mCommand = SharedPrefUtility.IMAGE_HEIGHT;
        }
        else if(key.contains("border") ||
                key.contains("offset"))
        {
            mCommand = SharedPrefUtility.BORDER_OFFSET;
        }
        else if(key.contains("para") ||
                key.contains("dis"))
        {
            mCommand = SharedPrefUtility.PARALLAX_DIS;
        }
        else {
            // default is unknown
            return false;
        }
        TextView text = mRoot.findViewById(R.id.txt_command);
        text.setText(mCommand);
        return true;
    }

    protected boolean setValue(String value)
    {
        try
        {
            mValue = Integer.parseInt(value);
            TextView textView = (TextView)mRoot.findViewById(R.id.txt_value);
            textView.setText(value);
            return true;
        }
        catch (Exception ex)
        {
            // default is unknown
            return false;
        }
    }
}
