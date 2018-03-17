package com.udacity.norbi930523.bakingapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.udacity.norbi930523.bakingapp.model.Recipe;

/**
 * Created by Norbert Boros on 2018. 03. 17..
 */

public class SharedPreferencesUtil {

    private static final String PINNED_RECIPE_KEY = "pinned_recipe";

    private SharedPreferencesUtil(){}

    public static void pinRecipe(Context context, Recipe recipe){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit()
                .putString(PINNED_RECIPE_KEY, RecipeJsonParserUtil.convertRecipeToJsonString(recipe))
                .commit();
    }

    public static void unpinRecipe(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit()
                .remove(PINNED_RECIPE_KEY)
                .commit();
    }

    public static Recipe getPinnedRecipe(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String pinnedRecipeJson = sharedPreferences.getString(PINNED_RECIPE_KEY, null);

        return pinnedRecipeJson == null ? null : RecipeJsonParserUtil.parseSingleRecipeJson(pinnedRecipeJson);
    }

}
