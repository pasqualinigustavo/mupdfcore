package com.foobnix.ui2;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.foobnix.android.utils.LOG;
import com.foobnix.tts.TTSEngine;
import com.foobnix.tts.TTSNotification;

import fi.iki.elonen.SampleServer;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public abstract class AdsFragmentActivity extends FragmentActivity {

    public abstract void onFinishActivity();

    protected int intetrstialTimeoutSec = 0;

    SampleServer sampleServer;

    Runnable onFinish = new Runnable() {

        @Override
        public void run() {
            onFinishActivity();
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }


    protected boolean withInterstitial = true;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // try {
        // sampleServer = new SampleServer(this);
        // } catch (IOException e) {
        // LOG.e(e);
        // }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // sampleServer.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // sampleServer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // myAds.activate(this, onFinish);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LOG.d("AdsFragmentActivity onSaveInstanceState before", outState);

        if (outState != null) {
            outState.clear();
        }
        LOG.d("AdsFragmentActivity onSaveInstanceState after", outState);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        LOG.d("AdsFragmentActivity onRestoreInstanceState before", savedInstanceState);

        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
        LOG.d("AdsFragmentActivity onRestoreInstanceState after", savedInstanceState);

        super.onRestoreInstanceState(savedInstanceState);
    }

    public void showInterstial() {
        TTSNotification.hideNotification();
        TTSEngine.get().shutdown();
        onFinish.run();
    }

    public boolean isInterstialShown() {
        return false;
    }


}
