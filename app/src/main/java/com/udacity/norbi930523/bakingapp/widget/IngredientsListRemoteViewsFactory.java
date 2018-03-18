package com.udacity.norbi930523.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.model.RecipeIngredient;

import java.util.List;

/**
 * Created by Norbert Boros on 2018. 03. 18..
 * Based on https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget
 */

public class IngredientsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String INGREDIENTS_PARAM = "ingredients";

    private List<RecipeIngredient> ingredients;

    private Context context;

    public IngredientsListRemoteViewsFactory(Context context, Intent intent){
        this.context = context;

        List<Bundle> customExtras = intent.getParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);
        Bundle bundle = customExtras.get(0);

        this.ingredients = bundle.getParcelableArrayList(INGREDIENTS_PARAM);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        }

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_widget_item);
        rv.setTextViewText(R.id.ingredientName, ingredients.get(position).getAsFormattedString(context));

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
