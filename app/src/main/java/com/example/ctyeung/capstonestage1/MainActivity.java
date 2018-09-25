package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import com.example.ctyeung.capstonestage1.ViewerActivity;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;
import com.example.ctyeung.capstonestage1.dialogs.NumberPickerFragment;
import com.example.ctyeung.capstonestage1.dialogs.ShareFragment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity
        implements ShareFragment.OnDialogOKListener{

    private FloatingActionButton btnShare;
    private Button btnViewer;
    private Context context;
    private ShareFragment mDlgShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // Launch Viewer -- consume
        btnViewer = (Button)findViewById(R.id.btnViewer);

        btnViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewerActivity.class);
                startActivity(intent);
            }
        });

        // Launch Tab pages -- Author + share
        btnShare = (FloatingActionButton)findViewById(R.id.share_fab);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laundShareViaDialog();
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
                intent = new Intent(context, ConfigActivity.class);
                startActivity(intent);
                return true;

            case R.id.share:
                // Share Via dialog
                laundShareViaDialog();
                return true;
        }
        return false;
    }

    protected void laundShareViaDialog()
    {
        mDlgShare = new ShareFragment();
        mDlgShare.setParams(this);
        mDlgShare.show(getSupportFragmentManager(), "Share Via");
    }

    /*
     * handle call back from ShareVia dialog
     */
    public void onShareViaDialogOKClick(SharedPrefUtility.MediaTypeEnum mediaType)
    {
        mDlgShare.dismiss();
        mDlgShare = null;

        String cancel = getResources().getString(R.string.btn_cancel);
        if(SharedPrefUtility.MediaTypeEnum.CANCEL != mediaType )
        {
            SharedPrefUtility.setMediaType(context, mediaType);
            Intent intent = new Intent(context, TabActivity.class);
            startActivity(intent);
        }
    }
}
