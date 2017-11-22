/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;

import com.evastos.githubrepos.ui.base.BaseView;
import com.evastos.githubrepos.ui.base.ErrorViewListener;
import com.evastos.githubrepos.ui.model.Repository;
import com.evastos.githubrepos.ui.model.SearchState;
import com.evastos.githubrepos.ui.search.adapter.RepositoryClickListener;

import java.util.List;

/**
 * This specifies the contract between the {@link SearchFragment} and the presenter.
 */
interface SearchContract {

    interface View extends BaseView, SearchView.OnQueryTextListener, RepositoryClickListener {

        void showProgress();

        void hideProgress();

        void showResultsTitle(@NonNull String searchQuery, @NonNull String sortedBy);

        void clearSearchFocus();

        void showRepositories(@NonNull List<Repository> repositories);

        void showLoadingMore();

        void hideLoadingMore();

        void addRepositories(@NonNull List<Repository> repositories);
    }

    interface Presenter extends ErrorViewListener {

        void onStart(@NonNull SearchState searchState);

        void onStop();

        SearchState onSaveState();

        void onSearchRepositories(@NonNull String searchQuery);

        void onSortByBestMatch();

        void onSortByStarsDesc();

        void onSortByStarsAsc();

        void onSortByForksDesc();

        void onSortByForksAsc();

        void onSortByUpdatedDesc();

        void onSortByUpdatedAsc();

        void onLoadNextPage();

        boolean isLoading();

        void onRepositoryClick(@NonNull Repository repository);
    }
}
