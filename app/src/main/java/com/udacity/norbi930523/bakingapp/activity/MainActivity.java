package com.udacity.norbi930523.bakingapp.activity;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.adapter.RecipeListAdapter;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.network.RecipeLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final int RECIPE_LOADER_ID = 100;

    @BindView(R.id.recipeListRecyclerView)
    RecyclerView recipeListRecyclerView;

    @BindView(R.id.loadingIndicator)
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recipeListRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));

        /* Load recipes */
        getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);

    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int cardWidth = 300;
        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;

        return (int) (screenWidth / cardWidth);
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new RecipeLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        loadingIndicator.setVisibility(View.GONE);

        if(recipeListRecyclerView.getAdapter() == null){
            recipeListRecyclerView.setAdapter(new RecipeListAdapter(this, data));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }
}
