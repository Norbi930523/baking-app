package com.udacity.norbi930523.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.activity.RecipeDetailsActivity;
import com.udacity.norbi930523.bakingapp.model.Recipe;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Norbert Boros on 2018. 03. 12..
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private Context context;

    private List<Recipe> data;

    public RecipeListAdapter(Context context, List<Recipe> data){
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        CardView recipeCard = (CardView) layoutInflater.inflate(R.layout.recipe_card, parent, false);

        return new RecipeViewHolder(recipeCard);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = data.get(position);

        holder.name.setText(recipe.getName());

        if(StringUtils.isEmpty(recipe.getImage())){
            Picasso.with(context)
                    .load(R.drawable.recipe_default)
                    .placeholder(R.drawable.recipe_default)
                    .into(holder.image);
        } else {
            Picasso.with(context)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.recipe_default)
                    .error(R.drawable.recipe_default)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipeImage)
        ImageView image;

        @BindView(R.id.recipeName)
        TextView name;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Recipe recipe = data.get(getAdapterPosition());

            Intent recipeDetailsIntent = new Intent(context, RecipeDetailsActivity.class);
            recipeDetailsIntent.putExtra(RecipeDetailsActivity.RECIPE_PARAM, recipe);

            context.startActivity(recipeDetailsIntent);
        }
    }
}
