package com.udacity.norbi930523.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.RemoteViewsService;

import com.udacity.norbi930523.bakingapp.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Norbert Boros on 2018. 03. 18..
 */

public class IngredientsWidgetRemoteViewsService extends RemoteViewsService {

    static Intent getIntent(Context context, List<RecipeIngredient> ingredients){
        Intent intent = new Intent(context, IngredientsWidgetRemoteViewsService.class);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(
                IngredientsListRemoteViewsFactory.INGREDIENTS_PARAM,
                new ArrayList<Parcelable>(ingredients)
        );

        ArrayList<Bundle> customExtra = new ArrayList<>();
        customExtra.add(bundle);

        intent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtra);

        return intent;
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListRemoteViewsFactory(getApplicationContext(), intent);
    }
}
