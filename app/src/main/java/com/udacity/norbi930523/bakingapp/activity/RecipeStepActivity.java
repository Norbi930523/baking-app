package com.udacity.norbi930523.bakingapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.fragment.IngredientsStepFragment;
import com.udacity.norbi930523.bakingapp.fragment.RecipeStepFragment;
import com.udacity.norbi930523.bakingapp.fragment.SimpleRecipeStepFragment;
import com.udacity.norbi930523.bakingapp.fragment.VideoRecipeStepFragment;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.model.RecipeStep;

import org.apache.commons.lang3.StringUtils;

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepFragment.OnRecipeStepNavigationClickListener {

    public static final Long INGREDIENTS_STEP_ID = -1L;

    public static final String RECIPE_PARAM = "recipe";

    public static final String SELECTED_STEP_INDEX_PARAM = "selectedStepIndex";

    private static final String TITLE_KEY = "title";

    private Recipe recipe;

    private int stepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        stepIndex = 0;

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            recipe = extras.getParcelable(RECIPE_PARAM);
            stepIndex = extras.getInt(SELECTED_STEP_INDEX_PARAM);
        }

        if(savedInstanceState == null){
            updateUI();
        } else {
            setTitle(savedInstanceState.getString(TITLE_KEY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TITLE_KEY, getTitle().toString());
    }

    private void updateUI(){
        RecipeStep recipeStep = recipe.getSteps().get(stepIndex);
        setTitle(String.format("%s | %s", recipe.getName(), recipeStep.getShortDescription()));

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
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.recipeStepContainer, recipeStepFragment)
                .commit();
    }

    @Override
    public void onPreviousButtonClick() {
        stepIndex--;
        updateUI();
    }

    @Override
    public void onNextButtonClick() {
        stepIndex++;
        updateUI();
    }
}
