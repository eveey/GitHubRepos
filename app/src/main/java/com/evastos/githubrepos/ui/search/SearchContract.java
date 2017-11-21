/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;

import com.evastos.githubrepos.ui.base.BaseView;
import com.evastos.githubrepos.ui.base.ErrorViewListener;
import com.evastos.githubrepos.ui.model.Repository;

import java.util.List;

/**
 * This specifies the contract between the {@link SearchFragment} and the presenter.
 */
interface SearchContract {

    interface View extends BaseView, SearchView.OnQueryTextListener {

        void showProgress();

        void hideProgress();

        void showResultsTitle(@NonNull String searchQuery, @NonNull String sortedBy);

        void clearSearchFocus();

        void showRepositories(@NonNull List<Repository> repositories);
    }

    interface Presenter extends ErrorViewListener {

        void onStart();

        void onStop();

        void onSaveState(@NonNull Bundle bundle);

        void onRestoreState(@Nullable Bundle bundle);

        void onSearchRepositories(@NonNull String searchQuery);

        void onSortByBestMatch();

        void onSortByStarsAsc();

        void onSortByStarsDesc();

        void onSortByForksAsc();

        void onSortByForksDesc();

        void onSortByUpdatedAsc();

        void onSortByUpdatedDesc();

        void onLoadNextPage();
    }
}
