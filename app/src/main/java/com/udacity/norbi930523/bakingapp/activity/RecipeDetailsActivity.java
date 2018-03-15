package com.udacity.norbi930523.bakingapp.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.model.Recipe;

/**
 * Created by Norbert Boros on 2018. 03. 15..
 */

public class RecipeDetailsActivity extends AppCompatActivity {

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

        setTitle(recipe.getName());
    }
}
