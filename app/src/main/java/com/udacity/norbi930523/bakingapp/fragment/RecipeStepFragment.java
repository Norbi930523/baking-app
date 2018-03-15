package com.udacity.norbi930523.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.model.Recipe;

/**
 * Created by Norbert Boros on 2018. 03. 15..
 */

public abstract class RecipeStepFragment extends Fragment {

    public static final String RECIPE_PARAM = "recipe";
    public static final String STEP_INDEX_PARAM = "stepIndex";

    protected Recipe recipe;
    protected int stepIndex;

    private OnRecipeStepNavigationClickListener navigationListener;

    public static <T extends RecipeStepFragment> T newInstance(T recipeStepFragment, Recipe recipe, int stepIndex) {
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_PARAM, recipe);
        args.putInt(STEP_INDEX_PARAM, stepIndex);
        recipeStepFragment.setArguments(args);

        return recipeStepFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnRecipeStepNavigationClickListener){
            navigationListener = (OnRecipeStepNavigationClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRecipeStepNavigationClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(RECIPE_PARAM);
            stepIndex = getArguments().getInt(STEP_INDEX_PARAM);
        }
    }

    protected void bindNavigation(View view){
        /* Previous step button */
        Button previousButton = view.findViewById(R.id.previousStepButton);

        if(stepIndex == 0){
            /* There is no previous step, hide the button */
            previousButton.setVisibility(View.GONE);
        } else {
            previousButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    navigationListener.onPreviousButtonClick();
                }
            });
        }

        /* Next step button */
        Button nextButton = view.findViewById(R.id.nextStepButton);

        if(stepIndex == recipe.getSteps().size() - 1){
            /* There is no next step, hide the button */
            nextButton.setVisibility(View.GONE);
        } else {
            nextButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    navigationListener.onNextButtonClick();
                }
            });
        }

    }

    public interface OnRecipeStepNavigationClickListener {
        void onPreviousButtonClick();
        void onNextButtonClick();
    }
}
