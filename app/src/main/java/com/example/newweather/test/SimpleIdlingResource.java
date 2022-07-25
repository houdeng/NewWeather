package com.example.newweather.test;

import android.app.Activity;

import androidx.test.espresso.IdlingResource;

import com.example.newweather.WeatherActivity;

public class SimpleIdlingResource implements IdlingResource {
    private volatile ResourceCallback mCallback;
//    private WeatherActivity weatherActivity;
    private WeatherActivity mActivity;

    public SimpleIdlingResource(Activity activity) {
        mActivity = (WeatherActivity) activity;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        if (mActivity != null && mActivity.isLoadFinished()) {
            if (mCallback != null) {
                mCallback.onTransitionToIdle();
            }
            return true;
        }
        return false;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }
}




