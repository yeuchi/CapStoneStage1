package com.example.ctyeung.capstonestage1.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.NumberPicker;

import com.example.ctyeung.capstonestage1.R;

/*
 * Number picker dialog
 * http://www.zoftino.com/android-numberpicker-dialog-example
 */
public class NumberPickerFragment extends DialogFragment {
    private NumberPicker.OnValueChangeListener valueChangeListener;

    private OnDialogOKListener listener;
    private NumberPicker numberPicker;
    private int min = 20;
    private int max = 60;
    private int value = 40;

    /*
     * parent call back listener
     */
    public interface OnDialogOKListener
    {
        void onNumberDialogOKClick(int value);
    }

    private void setNumberValues()
    {
        if(null!=numberPicker) {
            numberPicker.setMinValue(min);
            numberPicker.setMaxValue(max);
            numberPicker.setValue(value);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Activity activity = getActivity();
        Context context = activity.getBaseContext();

        String ok = context.getResources().getString(R.string.ok);
        String cancel = context.getResources().getString(R.string.btn_cancel);
        String chooseValue = context.getResources().getString(R.string.choose_value);
        String chooseNumber = context.getResources().getString(R.string.choose_number);

        numberPicker = new NumberPicker(activity);
        setNumberValues();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(chooseValue);
        builder.setMessage(chooseNumber);

        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int value = numberPicker.getValue();
                listener.onNumberDialogOKClick(value);

               // valueChangeListener.onValueChange(numberPicker,
                //        numberPicker.getValue(), numberPicker.getValue());
            }
        });


        builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setView(numberPicker);
        return builder.create();
    }

    public void setParams(OnDialogOKListener listener,
                          int min,
                          int max,
                          int value)
    {
        this.listener = listener;

        this.min = min;
        this.max = max;
        this.value = value;

        setNumberValues();
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener()
    {
        listener.onNumberDialogOKClick(numberPicker.getValue());
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}