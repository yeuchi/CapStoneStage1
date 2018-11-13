package com.example.ctyeung.capstonestage1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import android.content.Intent;
import android.net.Uri;

/**
 * Implementation of App Widget functionality.
 * - base on Mark Murphy's example
 * https://github.com/commonsguy/cw-advandroid/tree/master/AppWidget/LoremWidget
 */
public class HomeScreenWidget extends AppWidgetProvider
{
    @Override
    public void onUpdate(Context ctxt,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int i=0; i<appWidgetIds.length; i++) {
            Intent svcIntent=new Intent(ctxt, HomeScreenService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget=new RemoteViews(ctxt.getPackageName(),
                    R.layout.home_screen_widget);

            widget.setRemoteAdapter(appWidgetIds[i], R.id.widgetList, svcIntent);

            Intent clickIntent=new Intent(ctxt, MainActivity.class);
            PendingIntent intent=PendingIntent.getActivity(ctxt, 0,
                                                            clickIntent,
                                                            PendingIntent.FLAG_UPDATE_CURRENT);

            widget.setPendingIntentTemplate(R.id.widgetList, intent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

