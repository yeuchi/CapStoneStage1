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

import com.example.ctyeung.capstonestage1.dialogs.NumberPickerFragment;

public class ConfigActivity extends AppCompatActivity
            implements NumberPickerFragment.OnDialogOKListener
{
    private NumberPickerFragment.OnDialogOKListener NumListener;
    private int clickId;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ActionBar ab = getSupportActionBar();
        mContext = this.getApplicationContext();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        initializeButtons();
    }

    private void launchDialog(int id, int min, int max, int value)
    {
        clickId = id;
        NumberPickerFragment dlg = new NumberPickerFragment();
        dlg.setParams(NumListener, min, max, value);
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
                launchDialog(R.dimen.interlace_width, 50, 600, 200);
            }
        });

        // click handler for image height
        Button btnHeight = findViewById(R.id.btn_image_height);
        btnHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               launchDialog(R.dimen.image_height, 200, 800, 400);
            }
        });

        // click handler for border offset length
        Button btnBorder = findViewById(R.id.btn_border_offset);
        btnBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                launchDialog(R.dimen.border_offset, 50, 200, 100);
            }
        });

        // click handler for parallax distance
        Button btnParallax = findViewById(R.id.btn_parallax);
        btnParallax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                launchDialog(R.dimen.parallax_distance, 0, 40, 10);
            }
        });
    }

    /*
     * call back from NumberPickerFragment dialog box
     */
    public void onNumberDialogOKClick(int value)
    {

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
