package com.example.ctyeung.capstonestage1;

import android.widget.RemoteViewsService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;

/**
 * Created by ctyeung on 4/8/18.
 * - base on Mark Murphy's example
 * https://github.com/commonsguy/cw-advandroid/tree/master/AppWidget/LoremWidget
 */

public class HomeScreenService extends RemoteViewsService
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return(new WidgetViewsFactory(this.getApplicationContext(), intent));
    }
}
