package com.example.ctyeung.capstonestage1.utilities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.speech.RecognizerIntent;


public class SpeechRecognitionHelper {

    public  void run(Activity ownerActivity) {
        if (isSpeechRecognitionActivityPresented(ownerActivity) == true) {
            // if yes � running recognition
            startRecognitionActivity(ownerActivity);
        } else {
            // start installing process
            installGoogleVoiceSearch(ownerActivity);
        }
    }


    private  boolean isSpeechRecognitionActivityPresented(Activity ownerActivity) {
        try {
            // getting an instance of package manager
            PackageManager pm = ownerActivity.getPackageManager();
            // a list of activities, which can process speech recognition Intent
            List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

            if (activities.size() != 0) {    // if list not empty
                return true;                // then we can recognize the speech
            }
        } catch (Exception e) {

        }

        return false; // we have no activities to recognize the speech
    }

    /**
     * Send an Intent with request on speech
     * @param callerActivity  - Activity, that initiated a request
     */
    private  void startRecognitionActivity(Activity ownerActivity) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // giving additional parameters:
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Select an application");    // user hint
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);    // setting recognition model, optimized for short phrases � search queries
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);    // quantity of results we want to receive
        //choosing only 1st -  the most relevant

        // start Activity ant waiting the result
        ownerActivity.startActivityForResult(intent, 1234);
    }

    /**
     * Asking the permission for installing Google Voice Search.
     * If permission granted � sent user to Google Play
     * @param callerActivity � Activity, that initialized installing
     */
    private  void installGoogleVoiceSearch(final Activity ownerActivity) {

        // creating a dialog asking user if he want
        // to install the Voice Search
        Dialog dialog = new AlertDialog.Builder(ownerActivity)
                .setMessage("For recognition it�s necessary to install \"Google Voice Search\"")    // dialog message
                .setTitle("Install Voice Search from Google Play?")    // dialog header
                .setPositiveButton("Install", new DialogInterface.OnClickListener() {    // confirm button

                    // Install Button click handler
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // creating an Intent for opening applications page in Google Play
                            // Voice Search package name: com.google.android.voicesearch
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.voicesearch"));
                            // setting flags to avoid going in application history (Activity call stack)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            // sending an Intent
                            ownerActivity.startActivity(intent);
                        } catch (Exception ex) {
                            // if something going wrong
                            // doing nothing
                        }
                    }})

                .setNegativeButton("Cancel", null)    // cancel button
                .create();

        dialog.show();    // showing dialog
    }
}


