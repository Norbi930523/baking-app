package com.udacity.norbi930523.bakingapp.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.norbi930523.bakingapp.model.Recipe;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Norbert Boros on 2018. 03. 17..
 */

public class RecipeCacheManager {

    private static final String CACHE_FILE_NAME = "recipes.json";

    private RecipeCacheManager(){}

    public static void cacheRecipes(final Context context, String json){
        try {
            FileOutputStream fos = context.openFileOutput(CACHE_FILE_NAME, Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (Exception e) {
            Timber.e(e, "An error occurred while caching recipes.");
        }
    }


    public static List<Recipe> readRecipesFromCache(final Context context){
        try {
            FileInputStream fis = context.openFileInput(CACHE_FILE_NAME);
            String json = IOUtils.toString(fis, Charset.forName("UTF-8"));
            fis.close();

            return RecipeJsonParserUtil.parseJson(json);
        } catch (Exception e) {
            Timber.e(e, "An error occurred while reading recipes.");
        }

        return Collections.emptyList();
    }
}
