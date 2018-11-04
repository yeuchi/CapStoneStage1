package com.example.ctyeung.capstonestage1.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

import com.example.ctyeung.capstonestage1.R;
import com.example.ctyeung.capstonestage1.data.SharedPrefUtility;

import top.defaults.colorpicker.ColorPickerPopup;

/*
 * Color Popup by duanhong
 *
 * https://github.com/duanhong169/ColorPicker/releases?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=7068
 */
public class ColorPopup
{
    /*
     * popup for dot color selection
     */
    public static void launch(final Button button,
                              final Context context,
                              final String key)
    {
        String choose = context.getResources().getString(R.string.choose);
        String cancel = context.getResources().getString(R.string.btn_cancel);

        new ColorPickerPopup.Builder(context)
                .initialColor(Color.RED) // Set initial color
                .enableAlpha(true) // Enable alpha slider or not
                .okTitle(choose)
                .cancelTitle(cancel)
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(button, new ColorPickerPopup.ColorPickerObserver()
                {
                    @Override
                    public void onColorPicked(int color)
                    {
                        button.setBackgroundColor(color);
                        SharedPrefUtility.setDimension(key, context, color);    // store in sharedPreference
                    }

                    @Override
                    public void onColor(int color, boolean fromUser)
                    {

                    }
                });
    }
}
