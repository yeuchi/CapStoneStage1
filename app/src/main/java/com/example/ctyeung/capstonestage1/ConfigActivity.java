package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.dialogs.ColorPopup;
import com.example.ctyeung.capstonestage1.dialogs.NumberPickerFragment;
import com.example.ctyeung.capstonestage1.dialogs.VoiceAccessFragment;
import com.example.ctyeung.capstonestage1.utilities.SpeechRecognitionHelper;

import java.util.ArrayList;

import top.defaults.colorpicker.ColorPickerPopup;

public class ConfigActivity extends AppCompatActivity
            implements  NumberPickerFragment.OnDialogOKListener,
                        CompoundButton.OnCheckedChangeListener,
                        VoiceAccessFragment.OnDialogListener
        {
    private String clickId;
    private Context mContext;
    private Activity activity;
    private NumberPickerFragment.OnDialogOKListener numListener;
    protected SpeechRecognitionHelper mSpeechHelper;
    private VoiceAccessFragment mVoiceDlg;
    private VoiceAccessFragment.OnDialogListener mDlgListener;

    protected RadioGroup radioGroup;
    protected Button btnWidth;
    protected Button btnHeight;
    protected Button btnBorder;
    protected Button btnParallax;
    protected Button btnColor1;
    protected Button btnColor2;
    protected Button btnColor3;

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {

        CheckBox chkIsDebug = findViewById(R.id.chk_debug);
        boolean isDebug = chkIsDebug.isChecked()? true:false;
        SharedPrefUtility.setBoolean(SharedPrefUtility.IS_DEBUG, mContext, isDebug);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ActionBar ab = getSupportActionBar();
        mContext = this.getApplicationContext();
        numListener = this;
        mDlgListener = this;
        activity = this;

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        initializeButtons();
        initLocals();
    }

    private void initLocals()
    {
        setButtonColor(R.id.btnColor1, SharedPrefUtility.COLOR1);
        setButtonColor(R.id.btnColor2, SharedPrefUtility.COLOR2);
        setButtonColor(R.id.btnColor3, SharedPrefUtility.COLOR3);

        // is debug
        boolean isDebug = SharedPrefUtility.getBoolean(SharedPrefUtility.IS_DEBUG, mContext);
        CheckBox chkIsDebug = findViewById(R.id.chk_debug);
        chkIsDebug.setChecked(isDebug);
        chkIsDebug.setOnCheckedChangeListener(this);
    }

    private void setButtonColor(int id,
                                 String key)
    {
        int color = SharedPrefUtility.getDimension(key, mContext);
        Button btnColor = findViewById(id);
        btnColor.setBackgroundColor(color);
    }

    /*
     * Button click handlers
     */
    private void initializeButtons()
    {
        radioGroup = findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // save the change !
                SharedPrefUtility.DotModeEnum mode = (0==i)?
                        SharedPrefUtility.DotModeEnum.STEREO_PAIR:
                        SharedPrefUtility.DotModeEnum.INTERLACED;
                SharedPrefUtility.setDotMode(mContext, mode);
            }
        });

        // voice recogniztion to set values
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // record and perform dictation
                if(null==mSpeechHelper)
                    mSpeechHelper = new SpeechRecognitionHelper();

                mSpeechHelper.run(activity);
            }
        });

        // click handler for interlace width
        clickId = SharedPrefUtility.INTERLACE_WIDTH;
        int w = SharedPrefUtility.getDimension(clickId, mContext);
        onNumberDialogOKClick(w);

        btnWidth = findViewById(R.id.btn_interlace_width);
        btnWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                launchNumPickerDialog(SharedPrefUtility.INTERLACE_WIDTH, 50, 600);
            }
        });

        // click handler for image height
        clickId = SharedPrefUtility.IMAGE_HEIGHT;
        int h = SharedPrefUtility.getDimension(clickId, mContext);
        onNumberDialogOKClick(h);

        btnHeight = findViewById(R.id.btn_image_height);
        btnHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                launchNumPickerDialog(SharedPrefUtility.IMAGE_HEIGHT, 200, 800);
            }
        });

        // click handler for border offset length
        clickId = SharedPrefUtility.BORDER_OFFSET;
        int offset = SharedPrefUtility.getDimension(clickId, mContext);
        onNumberDialogOKClick(offset);

        btnBorder = findViewById(R.id.btn_border_offset);
        btnBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                launchNumPickerDialog(SharedPrefUtility.BORDER_OFFSET, 50, 200);
            }
        });

        // click handler for parallax distance
        clickId = SharedPrefUtility.PARALLAX_DIS;
        int dis = SharedPrefUtility.getDimension(clickId, mContext);
        onNumberDialogOKClick(dis);

        btnParallax = findViewById(R.id.btn_parallax);
        btnParallax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                launchNumPickerDialog(SharedPrefUtility.PARALLAX_DIS, 0, 40);
            }
        });

        // click handler for color1 selection
        btnColor1 = findViewById(R.id.btnColor1);
        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ColorPopup.launch(btnColor1, mContext, SharedPrefUtility.COLOR1);
            }
        });

        // click handler for color2 selection
        btnColor2 = findViewById(R.id.btnColor2);
        btnColor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ColorPopup.launch(btnColor1, mContext, SharedPrefUtility.COLOR2);
            }
        });

        // click handler for color3 selection
        btnColor3 = findViewById(R.id.btnColor3);
        btnColor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ColorPopup.launch(btnColor3, mContext, SharedPrefUtility.COLOR3);
            }
        });
    }

    /*
     * Accessibility voice command dialog callback
     * -> close dialog
     */
    public void onDialogClick(String id,
                              int value)
    {
        if(null!=mVoiceDlg)
        {
            mVoiceDlg.dismiss();
            mVoiceDlg = null;
        }

        /*
         * handle command
         */
        if(value > 0) {
            clickId = id;
            onNumberDialogOKClick(value);
        }
    }

    /*
     * Handle SpeechRecognition
     * -> dictation result -> launch dialog for confirmation
     */
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {

        // if it�s speech recognition results
        // and process finished ok
        if (requestCode == 1234 && resultCode == RESULT_OK) {

            // receiving a result in string array
            // there can be some strings because sometimes speech recognizing inaccurate
            // more relevant results in the beginning of the list
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            CharSequence chars ="";

            // in �matches� array we holding a results... let�s show the most relevant
            if (matches.size() > 0) {
                String msg = matches.get(0).toString();

                /*
                 * dialog for user to confirm command + value
                 */
                mVoiceDlg = new VoiceAccessFragment();
                mVoiceDlg.setParams(mDlgListener, msg);
                mVoiceDlg.show(getSupportFragmentManager(), "About");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * Number picker dialog
     */
    private void launchNumPickerDialog(String id,
                                       int min,
                                       int max)
    {
        String numPicker = getResources().getString(R.string.numberpicker);
        clickId = id;
        int value = SharedPrefUtility.getDimension(id, mContext);
        NumberPickerFragment dlg = new NumberPickerFragment();
        dlg.setParams(numListener, min, max, value);
        dlg.show(getSupportFragmentManager(), numPicker);
    }

    /*
     * call back from NumberPickerFragment dialog box
     */
    public void onNumberDialogOKClick(int value)
    {
        String prefix=null;
        int id=0;

        switch(clickId)
        {
            case SharedPrefUtility.BORDER_OFFSET:
                prefix = getResources().getString(R.string.border_offset_pixels);
                id = R.id.btn_border_offset;
                break;

            case SharedPrefUtility.IMAGE_HEIGHT:
                prefix = getResources().getString(R.string.image_height_pixels);
                id = R.id.btn_image_height;
                break;

            case SharedPrefUtility.INTERLACE_WIDTH:
                prefix = getResources().getString(R.string.interlace_width_pixels);
                id = R.id.btn_interlace_width;
                break;

            case SharedPrefUtility.PARALLAX_DIS:
                prefix = getResources().getString(R.string.parallax_dis_pixels);
                id = R.id.btn_parallax;
                break;

            default:
                return;
        }

        Button button = this.findViewById(id);
        button.setText(prefix + value);
        SharedPrefUtility.setDimension(clickId, mContext, value);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
