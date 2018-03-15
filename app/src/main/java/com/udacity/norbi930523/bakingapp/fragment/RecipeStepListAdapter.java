package com.udacity.norbi930523.bakingapp.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.fragment.RecipeStepListFragment.OnRecipeStepClickListener;
import com.udacity.norbi930523.bakingapp.model.RecipeStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RecipeStep} and makes a call to the
 * specified {@link OnRecipeStepClickListener}.
 */
public class RecipeStepListAdapter extends RecyclerView.Adapter<RecipeStepListAdapter.RecipeStepViewHolder> {

    private final List<RecipeStep> data;
    private final OnRecipeStepClickListener clickListener;

    public RecipeStepListAdapter(List<RecipeStep> data, OnRecipeStepClickListener listener) {
        this.data = data;
        clickListener = listener;
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.recipe_step, parent, false);

        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeStepViewHolder holder, int position) {
        RecipeStep recipeStep = data.get(position);

        holder.recipeStepName.setText(recipeStep.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipeStepName)
        TextView recipeStepName;

        RecipeStepViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null){
                clickListener.onRecipeStepClick(getAdapterPosition());
            }
        }
    }
}
