package com.udacity.norbi930523.bakingapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.model.RecipeIngredient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsStepFragment extends RecipeStepFragment {

    @BindView(R.id.ingredientsList)
    LinearLayout ingredientsList;

    public IngredientsStepFragment() {
        // Required empty public constructor
    }

    public static IngredientsStepFragment newInstance(Recipe recipe, int stepIndex){
        IngredientsStepFragment ingredientsStepFragment = new IngredientsStepFragment();

        return RecipeStepFragment.newInstance(ingredientsStepFragment, recipe, stepIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ingredients_step, container, false);

        ButterKnife.bind(this, root);

        setIngredientsList();

        bindNavigation(root);

        return root;
    }

    private void setIngredientsList(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        for(RecipeIngredient ingredient : recipe.getIngredients()){
            View ingredientItem = layoutInflater.inflate(R.layout.ingredient_list_item, ingredientsList, false);

            TextView ingredientName = ingredientItem.findViewById(R.id.ingredientName);
            ingredientName.setText(ingredient.getAsFormattedString(getContext()));

            ingredientsList.addView(ingredientItem);
        }
    }

}
