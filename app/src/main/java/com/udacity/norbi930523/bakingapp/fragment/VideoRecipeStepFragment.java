package com.udacity.norbi930523.bakingapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.model.RecipeStep;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoRecipeStepFragment extends RecipeStepFragment {

    @BindView(R.id.recipeStepDescription)
    TextView stepDescription;

    public VideoRecipeStepFragment() {
        // Required empty public constructor
    }

    public static VideoRecipeStepFragment newInstance(Recipe recipe, int stepIndex){
        VideoRecipeStepFragment videoRecipeStepFragment = new VideoRecipeStepFragment();

        return RecipeStepFragment.newInstance(videoRecipeStepFragment, recipe, stepIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_video_recipe_step, container, false);

        ButterKnife.bind(this, root);

        RecipeStep recipeStep = recipe.getSteps().get(stepIndex);
        stepDescription.setText(recipeStep.getDescription());

        bindNavigation(root);

        return root;
    }

}
