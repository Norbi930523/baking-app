package com.udacity.norbi930523.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class IngredientsWidgetUpdateService extends IntentService {

    private static final String ACTION_UPDATE = "com.udacity.norbi930523.bakingapp.widget.action.UPDATE";

    public IngredientsWidgetUpdateService() {
        super(IngredientsWidgetUpdateService.class.getSimpleName());
    }

    public static void startActionUpdate(Context context) {
        Intent intent = new Intent(context, IngredientsWidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE.equals(action)) {
                handleActionUpdate();
            }
        }
    }

    private void handleActionUpdate() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsListWidget.class));
        IngredientsListWidget.updateAllWidgets(this, appWidgetManager, appWidgetIds);
    }

}
