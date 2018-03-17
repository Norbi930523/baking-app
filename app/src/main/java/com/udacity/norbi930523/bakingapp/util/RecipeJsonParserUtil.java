package com.udacity.norbi930523.bakingapp.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.norbi930523.bakingapp.model.Recipe;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Norbert Boros on 2018. 03. 17..
 */

public class RecipeJsonParserUtil {

    private static final Type LIST_TYPE = new TypeToken<List<Recipe>>(){}.getType();

    private static final Gson GSON = new Gson();

    private RecipeJsonParserUtil(){}

    public static List<Recipe> parseRecipeListJson(String json){
        return GSON.fromJson(json, LIST_TYPE);
    }

    public static Recipe parseSingleRecipeJson(String json){
        return GSON.fromJson(json, Recipe.class);
    }

    public static String convertRecipeToJsonString(Recipe recipe){
        return GSON.toJson(recipe);
    }

}
