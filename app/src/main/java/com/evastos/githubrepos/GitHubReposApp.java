/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.evastos.githubrepos.inject.component.AppComponent;
import com.evastos.githubrepos.inject.component.DaggerAppComponent;
import com.evastos.githubrepos.inject.module.AppModule;
import com.evastos.githubrepos.inject.module.NetworkModule;

import static com.evastos.githubrepos.util.Constants.API_BASE_URL;

public class GitHubReposApp extends Application {

    private AppComponent appComponent;

    @NonNull
    public static GitHubReposApp getApp(@NonNull final Context context) {
        return (GitHubReposApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(API_BASE_URL))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
