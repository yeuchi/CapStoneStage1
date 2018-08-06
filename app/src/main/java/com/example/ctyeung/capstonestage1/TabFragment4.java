package com.example.ctyeung.capstonestage1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TabFragment4 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.tab_fragment_4, container, false);

        // Render Preview here ...


        Button btnSend = (Button)rootView.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Send message
            }
        });

        Button btnCancel = (Button)rootView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to MainActivity
            }
        });

        return rootView;
    }
}