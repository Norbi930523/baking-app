package com.udacity.norbi930523.bakingapp.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.norbi930523.bakingapp.BuildConfig;
import com.udacity.norbi930523.bakingapp.model.Recipe;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by Norbert Boros on 2018. 03. 12..
 */

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private static final Type LIST_TYPE = new TypeToken<List<Recipe>>(){}.getType();

    private final OkHttpClient httpClient;

    private final Gson gson;

    public RecipeLoader(Context context) {
        super(context);

        httpClient = new OkHttpClient();
        gson = new Gson();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        Request request = new Request.Builder()
                .url(BuildConfig.RECIPE_SOURCE_URL)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();

            String json = response.body().string();

            return gson.fromJson(json, LIST_TYPE);
        } catch(Exception e){
            Timber.e(e, "Error parsing recipes.");
        }

        return Collections.emptyList();
    }
}
