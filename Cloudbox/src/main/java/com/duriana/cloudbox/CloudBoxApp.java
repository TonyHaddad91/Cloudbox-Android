package com.duriana.cloudbox;

import android.app.Application;

import com.lifeofcoding.cacheutlislibrary.CacheUtils;

/**
 * Created by tonyhaddad on 01/08/2016.
 */
public class CloudBoxApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CacheUtils.configureCache(this);
    }
}
