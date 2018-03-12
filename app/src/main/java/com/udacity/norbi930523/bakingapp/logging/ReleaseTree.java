package com.udacity.norbi930523.bakingapp.logging;

import android.util.Log;

import timber.log.Timber;

/**
 * Created by Norbert Boros on 2018. 03. 12..
 */

public class ReleaseTree extends Timber.DebugTree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if(priority > Log.DEBUG){
            super.log(priority, tag, message, t);
        }
    }
}
