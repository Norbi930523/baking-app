package com.udacity.norbi930523.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.model.RecipeStep;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnRecipeStepClickListener}
 * interface.
 */
public class RecipeStepListFragment extends Fragment {

    private static final String RECIPE_STEPS_PARAM = "recipeSteps";

    private List<RecipeStep> recipeSteps;
    private OnRecipeStepClickListener clickListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepListFragment() {
    }

    public static RecipeStepListFragment newInstance(List<RecipeStep> recipeSteps) {
        RecipeStepListFragment fragment = new RecipeStepListFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(RECIPE_STEPS_PARAM, new ArrayList<Parcelable>(recipeSteps));

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipeSteps = getArguments().getParcelableArrayList(RECIPE_STEPS_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new RecipeStepListAdapter(recipeSteps, clickListener));
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnRecipeStepClickListener) {
            clickListener = (OnRecipeStepClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRecipeStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clickListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecipeStepClickListener {
        void onRecipeStepClick(int stepIndex);
    }
}
