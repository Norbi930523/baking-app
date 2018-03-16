package com.udacity.norbi930523.bakingapp.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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

public class VideoRecipeStepFragment extends RecipeStepFragment implements Player.EventListener {

    private static final String VIDEO_PLAYBACK_STATE_KEY = "videoPlaybackState";
    private static final String VIDEO_POSITION_KEY = "videoPosition";

    @Nullable
    @BindView(R.id.recipeStepDescription)
    TextView stepDescription;

    @BindView(R.id.exoPlayerView)
    PlayerView exoPlayerView;

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;

    private SimpleExoPlayer exoPlayer;

    public VideoRecipeStepFragment() {
        // Required empty public constructor
    }

    public static VideoRecipeStepFragment newInstance(Recipe recipe, int stepIndex){
        VideoRecipeStepFragment videoRecipeStepFragment = new VideoRecipeStepFragment();

        return RecipeStepFragment.newInstance(videoRecipeStepFragment, recipe, stepIndex);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int playbackState = PlaybackStateCompat.STATE_PAUSED;
        long currentVideoPosition = 0L;

        if(savedInstanceState != null){
            playbackState = savedInstanceState.getInt(VIDEO_PLAYBACK_STATE_KEY);
            currentVideoPosition = savedInstanceState.getLong(VIDEO_POSITION_KEY);
        }

        initializeMediaSession(playbackState, currentVideoPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(VIDEO_PLAYBACK_STATE_KEY, getMediaSessionPlaybackState());
        outState.putLong(VIDEO_POSITION_KEY, exoPlayer.getCurrentPosition());
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

    @Override
    public void onStop() {
        super.onStop();

        /* Pause the video if the app is put into the background or a call comes in */
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /* Release ExoPlayer */
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    private int getMediaSessionPlaybackState(){
        return mediaSession.getController().getPlaybackState().getState();
    }

    private long getMediaSessionPlaybackPosition(){
        return mediaSession.getController().getPlaybackState().getPosition();
    }

    private void initializeExoPlayer(Uri videoUri) {
        if(exoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());

            exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);

            /* Based on https://google.github.io/ExoPlayer/guide.html#preparing-the-player */
            // Produces DataSource instances through which media data is loaded.
            String userAgent = Util.getUserAgent(getContext(), BuildConfig.APPLICATION_ID);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);

            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);

            // Prepare the player with the source.
            exoPlayer.prepare(videoSource);

            long currentPlaybackPosition = getMediaSessionPlaybackPosition();
            if(currentPlaybackPosition > 0){
                /* After rotation, restore the video position and
                continue playing if the video was playing before rotation */
                exoPlayer.seekTo(currentPlaybackPosition);
                exoPlayer.setPlayWhenReady(getMediaSessionPlaybackState() == PlaybackStateCompat.STATE_PLAYING);
            }
        }
    }

    private void initializeMediaSession(int playbackState, long currentVideoPosition) {

        // Create a MediaSessionCompat.
        mediaSession = new MediaSessionCompat(getContext(), this.getClass().getName());

        // Enable callbacks from MediaButtons and TransportControls.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        /* Initialize the state with the restored data */
        playbackStateBuilder.setState(playbackState, currentVideoPosition, 1f);

        mediaSession.setPlaybackState(playbackStateBuilder.build());

        // Start the Media Session since the activity is active.
        mediaSession.setActive(true);

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playbackState == Player.STATE_READY && playWhenReady){
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
        } else if(playbackState == Player.STATE_READY){
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
        } else if(playbackState == Player.STATE_ENDED){
            /* When the video ends, go back to the start */
            exoPlayer.seekTo(0);
            exoPlayer.setPlayWhenReady(false);
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
        }

        mediaSession.setPlaybackState(playbackStateBuilder.build());
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
