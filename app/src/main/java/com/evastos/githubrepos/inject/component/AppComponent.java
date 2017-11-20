/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.inject.component;

import com.evastos.githubrepos.inject.module.AppModule;
import com.evastos.githubrepos.inject.module.GitHubModule;
import com.evastos.githubrepos.inject.module.NetworkModule;
import com.evastos.githubrepos.ui.SearchFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, GitHubModule.class})
public interface AppComponent {

    void inject(SearchFragment searchFragment);
}
