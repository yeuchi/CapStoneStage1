package com.example.ctyeung.capstonestage1;

import android.app.Activity;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
import android.widget.Toast;

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
        getWidgetExtra();
    }

    /*
     * Widget selection -- if any
     */
    private void getWidgetExtra()
    {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {

            String str = extras.getString("id");
            Toast.makeText(mContext, "id:"+str, Toast.LENGTH_SHORT).show();

            // launch viewer
            String[] list = str.split(" : ");
            if(null!=list && list.length==3)
            {
                int id = Integer.parseInt(list[0]);
                launchViewer(id);
            }
        }
    }

    /*
     * launch viewer
     */
    private void launchViewer(int id)
    {
        Intent intent = new Intent(mContext, ViewerActivity.class);
        intent.putExtra("SELECT_ID", id);
        startActivity(intent);
    }

    private void initLocals()
    {
        SharedPrefUtility.setIsDirty(SharedPrefUtility.SHAPE_IS_DIRTY, mContext, false);
        SharedPrefUtility.setIsDirty(SharedPrefUtility.TEXT_IS_DIRTY, mContext, false);
    }

    private void initButtons()
    {
        // Launch Viewer -- consume
        FloatingActionButton btnViewer = (FloatingActionButton)findViewById(R.id.btnViewer);

        btnViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchViewer(-1);
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

    private void updateWidget()
    {
        Application app = ((Activity)mContext).getApplication();
        /*
         * Ravi Rupareliya's solution to update widget
         * https://stackoverflow.com/questions/28941472/update-listview-widget-with-application
         */
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] appWidgetIds= appWidgetManager.getAppWidgetIds(new ComponentName(app, HomeScreenWidget.class));

        //Toast.makeText(this,"ids:size:"+appWidgetIds.length,Toast.LENGTH_SHORT).show();
        HomeScreenWidget myWidget = new HomeScreenWidget();
        myWidget.onUpdate(mContext, AppWidgetManager.getInstance(mContext),appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetList);
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
        updateWidget();

        Intent intent = new Intent(mContext, TabActivity.class);
        startActivity(intent);
    }
}
