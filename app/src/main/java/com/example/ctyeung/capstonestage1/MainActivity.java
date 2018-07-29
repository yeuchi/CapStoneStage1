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

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton btnShare;
    private Button btnViewer;
    private Context context;

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
                //startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                Intent intent = new Intent(context, TabActivity.class);
                startActivity(intent);
            }
        });
    }
}
