package com.udacity.norbi930523.bakingapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.adapter.RecipeListAdapter;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.network.RecipeLoader;
import com.udacity.norbi930523.bakingapp.testutil.SimpleIdlingResource;
import com.udacity.norbi930523.bakingapp.util.NetworkUtil;
import com.udacity.norbi930523.bakingapp.util.RecipeCacheManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final int RECIPE_LOADER_ID = 100;

    private SimpleIdlingResource simpleIdlingResource;

    @BindView(R.id.emptyCacheMessage)
    TextView emptyCacheMessage;

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

        if(NetworkUtil.isOnline(this)){
            /* The user is online, load recipes from the net */
            getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);

            if(simpleIdlingResource != null){
                simpleIdlingResource.setIdleState(false);
            }
        } else {
            loadRecipesFromCache();
        }

    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int cardWidth = 300;
        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;

        return (int) (screenWidth / cardWidth);
    }

    private void loadRecipesFromCache(){
        List<Recipe> recipes = RecipeCacheManager.readRecipesFromCache(this);

        if(!recipes.isEmpty()){
            onLoadFinished(null, recipes);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            recipeListRecyclerView.setVisibility(View.GONE);
            emptyCacheMessage.setVisibility(View.VISIBLE);
        }
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

        if(simpleIdlingResource != null){
            simpleIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (simpleIdlingResource == null) {
            simpleIdlingResource = new SimpleIdlingResource();
        }
        return simpleIdlingResource;
    }
}
