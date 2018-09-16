package com.example.ctyeung.capstonestage1.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

import top.defaults.colorpicker.ColorPickerPopup;

public class ColorPopup
{
    public static void launch(final Button button,
                              Context context)
    {
        new ColorPickerPopup.Builder(context)
                .initialColor(Color.RED) // Set initial color
                .enableAlpha(true) // Enable alpha slider or not
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(button, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        button.setBackgroundColor(color);
                    }

                    @Override
                    public void onColor(int color, boolean fromUser) {

                    }
                });
    }
}
