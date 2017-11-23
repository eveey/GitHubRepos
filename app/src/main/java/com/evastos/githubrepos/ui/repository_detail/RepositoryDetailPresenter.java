/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.repository_detail;

import android.support.annotation.NonNull;

import com.evastos.githubrepos.ui.model.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Presenter for {@link RepositoryDetailFragment}
 */
class RepositoryDetailPresenter implements RepositoryDetailContract.Presenter {

    private static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance();

    @NonNull
    private final RepositoryDetailContract.View view;

    @NonNull
    private final Repository repository;

    RepositoryDetailPresenter(@NonNull final RepositoryDetailContract.View view,
                              @NonNull final Repository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void onStart() {
        view.showRepositoryName(repository.getRepositoryFullName());
        view.showDateCreated(DATE_FORMAT.format(repository.getDateCreated()));
        view.showDateUpdated(DATE_FORMAT.format(repository.getDateUpdated()));
        view.showDescription(repository.getDescription());
        view.showLanguage(repository.getLanguage());
        view.showOwnerImage(repository.getOwnerAvatarUrl());
        view.showOwnerName(repository.getOwnerName());
        view.showWatchers(repository.getWatchersCount());
        view.showForks(repository.getForksCount());
        view.showIssues(repository.getIssuesCount());
        view.showStargazers(repository.getStargazersCount());
    }

    @Override
    public Repository onSaveState() {
        return repository;
    }

    @Override
    public void onOpenRepositoryUrl() {
        view.openUrl(repository.getRepositoryUrl());
    }
}
