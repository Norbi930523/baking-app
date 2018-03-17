package com.udacity.norbi930523.bakingapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.model.RecipeIngredient;
import com.udacity.norbi930523.bakingapp.util.SharedPreferencesUtil;
import com.udacity.norbi930523.bakingapp.widget.IngredientsWidgetUpdateService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsStepFragment extends RecipeStepFragment {

    @BindView(R.id.ingredientsList)
    LinearLayout ingredientsList;

    @BindView(R.id.pinnedRecipeToggle)
    ImageButton pinnedRecipeToggle;

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

        bindPinnedRecipeToggle();

        return root;
    }

    private void bindPinnedRecipeToggle(){
        updatePinToggle(isRecipePinned());

        pinnedRecipeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isPinned = isRecipePinned();
                if(isPinned){
                    /* This is the currently pinned recipe, unpin */
                    SharedPreferencesUtil.unpinRecipe(getContext());
                    isPinned = false;
                } else {
                    /* An other recipe is pinned, pin this one */
                    SharedPreferencesUtil.pinRecipe(getContext(), recipe);
                    isPinned = true;
                }

                updatePinToggle(isPinned);

                IngredientsWidgetUpdateService.startActionUpdate(getContext());
            }
        });

    }

    private boolean isRecipePinned(){
        Recipe pinnedRecipe = SharedPreferencesUtil.getPinnedRecipe(getContext());

        return pinnedRecipe != null && pinnedRecipe.getId().equals(recipe.getId());
    }

    private void updatePinToggle(boolean isPinned){
        if(isPinned){
            pinnedRecipeToggle.setBackgroundResource(R.drawable.ic_unpin);
            pinnedRecipeToggle.setContentDescription(getContext().getString(R.string.content_description_unpin));
        } else {
            pinnedRecipeToggle.setBackgroundResource(R.drawable.ic_pin);
            pinnedRecipeToggle.setContentDescription(getContext().getString(R.string.content_description_pin));
        }
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
