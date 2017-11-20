/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.inject.module;

import android.support.annotation.NonNull;

import com.evastos.githubrepos.data.service.GitHubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class GitHubModule {

    @Provides
    @Singleton
    GitHubService providesGithubService(@NonNull final Retrofit retrofit) {
        return retrofit.create(GitHubService.class);
    }
}
