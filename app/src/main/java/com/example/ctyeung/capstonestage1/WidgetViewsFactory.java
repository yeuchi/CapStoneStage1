package com.example.ctyeung.capstonestage1;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.ctyeung.capstonestage1.database.MsgData;
import com.example.ctyeung.capstonestage1.database.MsgTuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by ctyeung on 4/14/18.
 * - base on Mark Murphy's example
 * https://github.com/commonsguy/cw-advandroid/tree/master/AppWidget/LoremWidget
 */


public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static List<String> mItems=null;
    private Context mContext=null;
    public static final String KEY_EXTRA = "extra";

    public WidgetViewsFactory(Context context,
                              Intent intent) {
        mContext=context;
        setItems();
        int appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                        AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void setItems()
    {
        try {
            MsgData msgData = new MsgData(mContext);
            List<MsgTuple> tuples = msgData.query();

            if (null == tuples || 0 == tuples.size()) {
                mItems = new ArrayList<String>(Arrays.asList("Empty -- no entry available"));
            } else {
                mItems = new ArrayList<String>();
                for (MsgTuple tuple : tuples) {
                    mItems.add(tuple.id + " : " + tuple.subject + " : " + tuple.timeStamp);
                }
            }
        }
        catch (Exception ex)
        {
            mItems = new ArrayList<String>(Arrays.asList("setItems() failed: "+ex.toString()));
        }
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {

        return (null==mItems)?
                0:
                mItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews=new RemoteViews(mContext.getPackageName(), R.layout.row);
        remoteViews.setTextViewText(R.id.widgetTextView, mItems.get(position));

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putString(KEY_EXTRA, mItems.get(position));
        i.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widgetTextView, i);
        return(remoteViews);
    }

    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

    @Override
    public void onDataSetChanged() {}
}