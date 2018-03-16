package com.udacity.norbi930523.bakingapp.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.norbi930523.bakingapp.BuildConfig;
import com.udacity.norbi930523.bakingapp.R;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.model.RecipeStep;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoRecipeStepFragment extends RecipeStepFragment {

    @Nullable
    @BindView(R.id.recipeStepDescription)
    TextView stepDescription;

    @BindView(R.id.exoPlayerView)
    PlayerView exoPlayerView;

    SimpleExoPlayer exoPlayer;

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

        if(stepDescription != null){
            stepDescription.setText(recipeStep.getDescription());
        }

        bindNavigation(root);

        /* ExoPlayer setup */
        String videoUri = StringUtils.defaultString(recipeStep.getVideoURL(), recipeStep.getThumbnailURL());
        initializeExoPlayer(Uri.parse(videoUri));

        return root;
    }

    private void initializeExoPlayer(Uri videoUri) {
        if(exoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());

            exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            exoPlayerView.setPlayer(exoPlayer);

            /* Based on https://google.github.io/ExoPlayer/guide.html#preparing-the-player */
            // Produces DataSource instances through which media data is loaded.
            String userAgent = Util.getUserAgent(getContext(), BuildConfig.APPLICATION_ID);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);

            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);

            // Prepare the player with the source.
            exoPlayer.prepare(videoSource);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /* Release ExoPlayer */
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

}
