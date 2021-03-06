package com.example.ctyeung.capstonestage1;

import android.app.Activity;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.database.MsgContract;
import com.example.ctyeung.capstonestage1.database.MsgData;
import com.example.ctyeung.capstonestage1.database.MsgTuple;
import com.example.ctyeung.capstonestage1.utilities.JSONhelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.Arrays;
import java.util.List;

/*
 * https://stackoverflow.com/questions/8854359/exception-open-failed-eacces-permission-denied-on-android
 */
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks
{

    private Context mContext;
    private static final int RC_SIGN_IN = 123;
    private MsgData mMsgData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mMsgData = new MsgData(mContext);

        mContext = this;
        initButtons();
        initLocals();
        getWidgetExtra();
        signIn();

        getSupportLoaderManager().initLoader(1, null, this);

    }

    /*
     * firebase login
     */
    private void signIn()
    {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    /*
     * firebase sign-in result
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    /*
     * firebase logout
     */
    protected void onLogout()
    {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    /*
     * Widget selection -- if any
     */
    private void getWidgetExtra()
    {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {

            String str = extras.getString(WidgetViewsFactory.KEY_EXTRA);
            Toast.makeText(mContext, "id:" + str, Toast.LENGTH_SHORT).show();

            // launch viewer
            String[] list = str.split(" : ");
            if (null != list && list.length == 3) {
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

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] args = {MsgTuple.BLANK};
        CursorLoader cursorLoader = new CursorLoader(mContext,
                MsgContract.CONTENT_URI,
                null,
                MsgContract.Columns.COL_TIME_STAMP+"!=?",
                args,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object o) {

        List<MsgTuple> tuples = mMsgData.parseResult((Cursor) o);

        /*
         * Update widget when data changes
         */
        if(null!=tuples && tuples.size()>0) {

            String[] list = new String[tuples.size()];
            int i=0;
            for(MsgTuple tuple : tuples) {
                list[i++] = tuple.id + " : " + tuple.subject + " : " + tuple.timeStamp;
            }
            updateWidget(list);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    private void updateWidget(String[] tuples)
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
        myWidget.onUpdate(mContext, AppWidgetManager.getInstance(mContext),appWidgetIds, tuples);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_tab, menu);
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

            case R.id.logout:
                // Share Via dialog
                onLogout();
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
