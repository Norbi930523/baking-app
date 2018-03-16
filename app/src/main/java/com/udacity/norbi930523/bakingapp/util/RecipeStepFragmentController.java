package com.udacity.norbi930523.bakingapp.util;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.udacity.norbi930523.bakingapp.fragment.IngredientsStepFragment;
import com.udacity.norbi930523.bakingapp.fragment.RecipeStepFragment;
import com.udacity.norbi930523.bakingapp.fragment.SimpleRecipeStepFragment;
import com.udacity.norbi930523.bakingapp.fragment.VideoRecipeStepFragment;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.model.RecipeStep;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Norbert Boros on 2018. 03. 16..
 */

public class RecipeStepFragmentController {

    private AppCompatActivity activity;

    private Recipe recipe;

    private int stepIndex = 0;

    private int fragmentContainerResId;

    private RecipeStepFragmentController(){}

    public static RecipeStepFragmentController in(AppCompatActivity activity){
        RecipeStepFragmentController fragmentController = new RecipeStepFragmentController();
        fragmentController.activity = activity;

        return fragmentController;
    }

    public RecipeStepFragmentController loadStepOf(Recipe recipe){
        this.recipe = recipe;

        return this;
    }

    public RecipeStepFragmentController at(int stepIndex){
        this.stepIndex = stepIndex;

        return this;
    }

    public void into(int fragmentContainerResId){
        this.fragmentContainerResId = fragmentContainerResId;

        RecipeStep recipeStep = recipe.getSteps().get(stepIndex);

        if(stepIndex == 0){
            showIngredientsStepFragment();
        } else {
            String videoUrl = StringUtils.defaultString(recipeStep.getVideoURL(), recipeStep.getThumbnailURL());

            if(StringUtils.isNotEmpty(videoUrl)){
                showVideoRecipeStepFragment();
            } else {
                showSimpleRecipeStepFragment();
            }
        }
    }

    private void showIngredientsStepFragment() {
        IngredientsStepFragment ingredientsStepFragment = IngredientsStepFragment.newInstance(recipe, stepIndex);

        showRecipeStepFragment(ingredientsStepFragment);
    }

    private void showSimpleRecipeStepFragment(){
        SimpleRecipeStepFragment simpleRecipeStepFragment = SimpleRecipeStepFragment.newInstance(recipe, stepIndex);

        showRecipeStepFragment(simpleRecipeStepFragment);
    }

    private void showVideoRecipeStepFragment(){
        VideoRecipeStepFragment videoRecipeStepFragment = VideoRecipeStepFragment.newInstance(recipe, stepIndex);

        showRecipeStepFragment(videoRecipeStepFragment);
    }

    private void showRecipeStepFragment(RecipeStepFragment recipeStepFragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(fragmentContainerResId, recipeStepFragment)
                .commit();
    }
}
