package com.udacity.norbi930523.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.util.SharedPreferencesUtil;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsListWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Recipe pinnedRecipe = SharedPreferencesUtil.getPinnedRecipe(context);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_widget);

        if(pinnedRecipe != null){
            Intent remoteViewsService = IngredientsWidgetRemoteViewsService.getIntent(context, pinnedRecipe.getIngredients());
            views.setRemoteAdapter(R.id.pinnedRecipeIngredients, remoteViewsService);

            views.setTextViewText(R.id.pinnedRecipeName, pinnedRecipe.getName());
            views.setViewVisibility(R.id.pinnedRecipeIngredients, View.VISIBLE);
        } else {
            views.setTextViewText(R.id.pinnedRecipeName, context.getString(R.string.widget_no_recipe_pinned));
            views.setViewVisibility(R.id.pinnedRecipeIngredients, View.GONE);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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

