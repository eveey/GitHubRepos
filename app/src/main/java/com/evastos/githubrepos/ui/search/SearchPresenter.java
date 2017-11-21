/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.search;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evastos.githubrepos.data.model.response.RepositoriesResponse;
import com.evastos.githubrepos.data.service.GitHubService;
import com.evastos.githubrepos.ui.model.Repository;
import com.evastos.githubrepos.ui.model.SortBy;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class SearchPresenter implements SearchContract.Presenter {

    private static final String BUNDLE_STATE_SORT_BY = "sortBy";

    private static final String BUNDLE_STATE_PAGE = "page";

    private static final String BUNDLE_STATE_SEARCH_QUERY = "searchQuery";

    private static final String BUNDLE_STATE_REPOSITORIES = "repositories";

    private static final int FIRST_PAGE = 1;

    private static final int REPOSITORIES_PER_PAGE = 2;

    @NonNull
    private final GitHubService gitHubService;

    @NonNull
    private SortBy sortBy = SortBy.BEST_MATCH;

    @Nullable
    private String searchQuery;

    private int page = FIRST_PAGE;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @NonNull
    private List<Repository> repositories = new ArrayList<>();

    @NonNull
    private final SearchContract.View view;

    SearchPresenter(@NonNull final SearchContract.View view,
                    @NonNull final GitHubService gitHubService) {
        this.view = view;
        this.gitHubService = gitHubService;
    }

    @Override
    public void onStart() {
        if (searchQuery != null) {
            view.showRepositories(repositories);
            view.showResultsTitle(searchQuery, sortBy.toString());
        }
    }

    @Override
    public void onStop() {
        disposables.dispose();
        disposables.clear();
    }

    @Override
    public void onSaveState(@NonNull final Bundle bundle) {
        bundle.putSerializable(BUNDLE_STATE_SORT_BY, sortBy);
        bundle.putInt(BUNDLE_STATE_PAGE, page);
        bundle.putString(BUNDLE_STATE_SEARCH_QUERY, searchQuery);
        bundle.putParcelableArrayList(BUNDLE_STATE_REPOSITORIES, (ArrayList<? extends Parcelable>) repositories);
    }

    @Override
    public void onRestoreState(@Nullable Bundle bundle) {
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey(BUNDLE_STATE_SORT_BY)) {
            final SortBy sortBy = (SortBy) bundle.getSerializable(BUNDLE_STATE_SORT_BY);
            if (sortBy != null) {
                this.sortBy = sortBy;
            }
        }
        page = bundle.getInt(BUNDLE_STATE_PAGE, FIRST_PAGE);
        searchQuery = bundle.getString(BUNDLE_STATE_SEARCH_QUERY, null);
        if (bundle.containsKey(BUNDLE_STATE_REPOSITORIES)) {
            final List<Repository> repositories = bundle.getParcelableArrayList(BUNDLE_STATE_REPOSITORIES);
            if (repositories != null) {
                this.repositories = repositories;
            }
        }
    }

    @Override
    public void onSearchRepositories(@NonNull final String searchQuery) {
        this.searchQuery = searchQuery;
        view.clearSearchFocus();
        sortBy = SortBy.BEST_MATCH;
        searchRepositories();
    }

    @Override
    public void onSortByBestMatch() {
        sortBy = SortBy.BEST_MATCH;
        searchRepositories();
    }

    @Override
    public void onSortByStarsAsc() {
        sortBy = SortBy.STARS_ASC;
        searchRepositories();
    }

    @Override
    public void onSortByStarsDesc() {
        sortBy = SortBy.STARS_DESC;
        searchRepositories();
    }

    @Override
    public void onSortByForksAsc() {
        sortBy = SortBy.FORKS_ASC;
        searchRepositories();
    }

    @Override
    public void onSortByForksDesc() {
        sortBy = SortBy.FORKS_DESC;
        searchRepositories();
    }

    @Override
    public void onSortByUpdatedAsc() {
        sortBy = SortBy.UPDATED_ASC;
        searchRepositories();
    }

    @Override
    public void onSortByUpdatedDesc() {
        sortBy = SortBy.UPDATED_DESC;
        searchRepositories();
    }

    @Override
    public void onLoadNextPage() {
        page++;
        searchRepositories();
    }

    private void searchRepositories() {
        if (searchQuery == null) {
            return;
        }
        if (page == FIRST_PAGE) {
            repositories = new ArrayList<>();
        }
        view.showProgress();
        final Disposable disposable = gitHubService.searchRepositories(
                searchQuery,
                sortBy.getSorting(),
                sortBy.getOrdering(),
                page,
                REPOSITORIES_PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RepositoriesResponse>() {
                               @Override
                               public void accept(RepositoriesResponse repositoriesResponse) throws Exception {
                                   if (repositoriesResponse != null && repositoriesResponse.getItems() != null) {
                                       onSuccess(repositoriesResponse.getItems(), searchQuery);
                                   } else {
                                       onError();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                onError();
                            }
                        });
        disposables.add(disposable);
    }

    @Override
    public void onErrorRefreshClicked() {
        searchRepositories();
    }

    private void onSuccess(@NonNull final List<com.evastos.githubrepos.data.model.response.Repository> data,
                           @NonNull final String searchQuery) {
        view.hideProgress();
        view.showResultsTitle(searchQuery, sortBy.toString());
        for (com.evastos.githubrepos.data.model.response.Repository repositoryItem : data) {
            repositories.add(new Repository(repositoryItem));
        }
        view.showRepositories(repositories);
    }

    private void onError() {
        view.hideProgress();
        view.showError(this);
    }
}
