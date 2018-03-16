package com.udacity.norbi930523.bakingapp.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.fragment.RecipeStepFragment;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.model.RecipeStep;
import com.udacity.norbi930523.bakingapp.util.RecipeStepFragmentController;

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

        if(isLandscape()){
            goFullScreen();
        }

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

    /* Based on https://developer.android.com/training/system-ui/status.html and
     * https://developer.android.com/training/system-ui/navigation.html#40 */
    private void goFullScreen() {
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
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

        RecipeStepFragmentController.in(this)
                .loadStepOf(recipe)
                .at(stepIndex)
                .into(R.id.recipeStepContainer);
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

    private boolean isLandscape(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
