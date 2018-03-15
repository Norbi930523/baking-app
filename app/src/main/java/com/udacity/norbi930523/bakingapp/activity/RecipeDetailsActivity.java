package com.udacity.norbi930523.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.fragment.RecipeStepListFragment;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.model.RecipeStep;

/**
 * Created by Norbert Boros on 2018. 03. 15..
 */

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeStepListFragment.OnRecipeStepClickListener {

    public static final String RECIPE_PARAM_KEY = "recipe";

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            recipe = extras.getParcelable(RECIPE_PARAM_KEY);
        }

        if(recipe == null){
            finish();
            Toast.makeText(this, R.string.error_recipe_required, Toast.LENGTH_LONG).show();
            return;
        }

        if(savedInstanceState == null){
            setTitle(recipe.getName());

            loadRecipeSteps();
        }

    }

    private void loadRecipeSteps(){
        /* Prepend a fake ingredients step to the steps list to be able to display it
         * in the recycler view */
        prependIngredientsStep();

        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeStepListFragment recipeStepListFragment = RecipeStepListFragment.newInstance(recipe.getSteps());

        fragmentManager.beginTransaction()
                .add(R.id.recipeStepListContainer, recipeStepListFragment)
                .commit();
    }

    private void prependIngredientsStep(){
        RecipeStep ingredientsStep = new RecipeStep();
        ingredientsStep.setId(RecipeStepActivity.INGREDIENTS_STEP_ID);
        ingredientsStep.setShortDescription(getString(R.string.ingredients));

        recipe.getSteps().add(0, ingredientsStep);
    }

    @Override
    public void onRecipeStepClick(int stepIndex) {
        Intent recipeStepIntent = new Intent(this, RecipeStepActivity.class);
        recipeStepIntent.putExtra(RecipeStepActivity.RECIPE_PARAM, recipe);
        recipeStepIntent.putExtra(RecipeStepActivity.SELECTED_STEP_INDEX_PARAM, stepIndex);

        startActivity(recipeStepIntent);
    }
}
