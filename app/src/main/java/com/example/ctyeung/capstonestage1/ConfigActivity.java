package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.dialogs.NumberPickerFragment;

public class ConfigActivity extends AppCompatActivity
            implements NumberPickerFragment.OnDialogOKListener
{
    private NumberPickerFragment.OnDialogOKListener numListener;
    private String clickId;
    private Context mContext;
    private Activity activity;

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
