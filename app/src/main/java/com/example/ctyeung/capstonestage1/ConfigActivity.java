package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.dialogs.ColorPopup;
import com.example.ctyeung.capstonestage1.dialogs.NumberPickerFragment;

import top.defaults.colorpicker.ColorPickerPopup;

public class ConfigActivity extends AppCompatActivity
            implements NumberPickerFragment.OnDialogOKListener,
            CompoundButton.OnCheckedChangeListener
        {
    private NumberPickerFragment.OnDialogOKListener numListener;
    private String clickId;
    private Context mContext;
    private Activity activity;

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

    private void launchDialog(String id, int min, int max)
    {
        clickId = id;
        int value = SharedPrefUtility.getDimension(id, mContext);
        NumberPickerFragment dlg = new NumberPickerFragment();
        dlg.setParams(numListener, min, max, value);
        dlg.show(getSupportFragmentManager(), "NumberPicker");
    }

    private void initializeButtons()
    {
        RadioGroup radioGroup = findViewById(R.id.radio_group);
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

        // click handler for font selection

        // click handler for interlace width
        Button btnWidth = findViewById(R.id.btn_interlace_width);
        btnWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                launchDialog(SharedPrefUtility.INTERLACE_WIDTH, 50, 600);
            }
        });

        // click handler for image height
        Button btnHeight = findViewById(R.id.btn_image_height);
        btnHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               launchDialog(SharedPrefUtility.IMAGE_HEIGHT, 200, 800);
            }
        });

        // click handler for border offset length
        Button btnBorder = findViewById(R.id.btn_border_offset);
        btnBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                launchDialog(SharedPrefUtility.BORDER_OFFSET, 50, 200);
            }
        });

        // click handler for parallax distance
        Button btnParallax = findViewById(R.id.btn_parallax);
        btnParallax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                launchDialog(SharedPrefUtility.PARALLAX_DIS, 0, 40);
            }
        });

        // click handler for color1 selection
        final Button btnColor1 = findViewById(R.id.btnColor1);
        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ColorPopup.launch(btnColor1, mContext, SharedPrefUtility.COLOR1);
            }
        });

        // click handler for color2 selection
        final Button btnColor2 = findViewById(R.id.btnColor2);
        btnColor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ColorPopup.launch(btnColor1, mContext, SharedPrefUtility.COLOR2);
            }
        });

        // click handler for color3 selection
        final Button btnColor3 = findViewById(R.id.btnColor3);
        btnColor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ColorPopup.launch(btnColor3, mContext, SharedPrefUtility.COLOR3);
            }
        });
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
                prefix = "Border Offset: ";
                id = R.id.btn_border_offset;
                break;

            case SharedPrefUtility.IMAGE_HEIGHT:
                prefix = "Image Height: ";
                id = R.id.btn_image_height;
                break;

            case SharedPrefUtility.INTERLACE_WIDTH:
                prefix = "Interlace width: ";
                id = R.id.btn_interlace_width;
                break;

            case SharedPrefUtility.PARALLAX_DIS:
                prefix = "Parallax dis: ";
                id = R.id.btn_parallax;
                break;
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
