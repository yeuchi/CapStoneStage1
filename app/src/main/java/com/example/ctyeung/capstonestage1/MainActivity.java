package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/*
 * https://stackoverflow.com/questions/8854359/exception-open-failed-eacces-permission-denied-on-android
 */
public class MainActivity extends AppCompatActivity{

    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initButtons();
        initLocals();
    }

    private void initLocals()
    {
        SharedPrefUtility.setIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext, false);
        SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext, false);

    }

    private void initButtons()
    {
        // Launch Viewer -- consume
        Button btnViewer = (Button)findViewById(R.id.btnViewer);

        btnViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewerActivity.class);
                startActivity(intent);
            }
        });

        // Launch Tab pages -- Author + share
        FloatingActionButton btnShare = (FloatingActionButton)findViewById(R.id.share_fab);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickShare();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        Intent intent=null;

        switch (itemId) {

            case R.id.configuration:
                // load configuration activity
                intent = new Intent(mContext, ConfigActivity.class);
                startActivity(intent);
                return true;

            case R.id.share:
                // Share Via dialog
                onButtonClickShare();
                return true;
        }
        return false;
    }

    protected void onButtonClickShare()
    {
        Intent intent = new Intent(mContext, TabActivity.class);
        startActivity(intent);
    }
}
