package com.udacity.norbi930523.bakingapp.application;

import android.app.Application;

import com.udacity.norbi930523.bakingapp.BuildConfig;
import com.udacity.norbi930523.bakingapp.logging.ReleaseTree;

import timber.log.Timber;

/**
 * Created by Norbert Boros on 2018. 03. 12..
 */

public class BakingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
    }
}
