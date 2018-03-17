package com.udacity.norbi930523.bakingapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Norbert Boros on 2018. 03. 17..
 */

public class SharedPreferencesUtil {

    private static final String PINNED_RECIPE_KEY = "pinned_recipe";

    private SharedPreferencesUtil(){}

    public static void pinRecipe(Context context, Long recipeId){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit()
                .putLong(PINNED_RECIPE_KEY, recipeId)
                .commit();
    }

    public static void unpinRecipe(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit()
                .remove(PINNED_RECIPE_KEY)
                .commit();
    }

    public static Long getPinnedRecipeId(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getLong(PINNED_RECIPE_KEY, -1L);
    }

}
