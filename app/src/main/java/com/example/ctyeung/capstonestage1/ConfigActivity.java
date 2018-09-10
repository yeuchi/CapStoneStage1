package com.example.ctyeung.capstonestage1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        initializeButtons();
    }

    private void initializeButtons()
    {
        // add a dialog box for interlace width

        // add a dialog box for image height

        // add a dialog for font selection

        // add a dialog box for border offset length

        // add a dialog for for parallax distance
        TextView txtParallax = findViewById(R.id.txt_parallax);
        txtParallax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int min = 0;
                int max = 10;
                int value = 5;
                showNumberPickerDlg(min, max, value);
            }
        });
    }

    private void showNumberPickerDlg(int min, int max, int value)
    {
        NumberPickerFragment dlg = new NumberPickerFragment();
        dlg.setParams(NumListener);
        dlg.show(getSupportFragmentManager(), "NumberPicker");
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
