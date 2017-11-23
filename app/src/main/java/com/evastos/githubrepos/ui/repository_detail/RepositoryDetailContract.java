/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.repository_detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evastos.githubrepos.ui.model.Repository;

/**
 * This specifies the contract between the {@link RepositoryDetailFragment} and the presenter.
 */
interface RepositoryDetailContract {

    interface View {

        void showRepositoryName(@NonNull String repositoryName);

        void showOwnerImage(@NonNull String ownerAvatarUrl);

        void showOwnerName(@NonNull String ownerName);

        void showDescription(@NonNull String description);

        void showDateCreated(@NonNull String dateCreated);

        void showDateUpdated(@NonNull String dateUpdated);

        void showLanguage(@Nullable String language);

        void showWatchers(final int watchersCount);

        void showForks(final int forksCount);

        void showIssues(final int issuesCount);

        void showStargazers(final int stargazersCount);

        void openUrl(@NonNull final String url);
    }

    interface Presenter {

        void onStart();

        Repository onSaveState();

        void onOpenRepositoryUrl();
    }
}
