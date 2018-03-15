package com.udacity.norbi930523.bakingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.model.RecipeIngredient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity {

    public static final Long INGREDIENTS_STEP_ID = -1L;

    public static final String RECIPE_PARAM = "recipe";

    public static final String SELECTED_STEP_INDEX_PARAM = "selectedStepIndex";

    @BindView(R.id.ingredientsList)
    LinearLayout ingredientsList;

    private Recipe recipe;

    private int stepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stepIndex = 0;

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            recipe = extras.getParcelable(RECIPE_PARAM);
            stepIndex = extras.getInt(SELECTED_STEP_INDEX_PARAM);
        }

        setTitle(recipe.getName());

        if(isIngredientsStep()){
            setContentView(R.layout.activity_recipe_step_ingredients);

            ButterKnife.bind(this);

            setIngredientsList();
        } else {
            setContentView(R.layout.activity_recipe_step);
        }
    }

    private void setIngredientsList(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        for(RecipeIngredient ingredient : recipe.getIngredients()){
            View ingredientItem = layoutInflater.inflate(R.layout.ingredient_item, ingredientsList, false);

            TextView ingredientName = ingredientItem.findViewById(R.id.ingredientName);
            ingredientName.setText(ingredient.getAsFormattedString(this));

            ingredientsList.addView(ingredientItem);
        }
    }

    private boolean isIngredientsStep(){
        return stepIndex == 0;
    }
}
