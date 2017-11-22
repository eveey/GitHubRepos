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

    private static final int REPOSITORIES_PER_PAGE = 50;

    @NonNull
    private final GitHubService gitHubService;

    @NonNull
    private SortBy sortBy = SortBy.BEST_MATCH;

    @Nullable
    private String searchQuery;

    private int page = FIRST_PAGE;

    private int totalItemCount;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private boolean isLoading = false;

    private boolean shouldLoadMore = false;

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
        page = FIRST_PAGE;
        searchRepositories();
    }

    @Override
    public void onSortByBestMatch() {
        sortBy = SortBy.BEST_MATCH;
        page = FIRST_PAGE;
        searchRepositories();
    }

    @Override
    public void onSortByStarsDesc() {
        sortBy = SortBy.STARS_DESC;
        page = FIRST_PAGE;
        searchRepositories();
    }

    @Override
    public void onSortByStarsAsc() {
        sortBy = SortBy.STARS_ASC;
        page = FIRST_PAGE;
        searchRepositories();
    }

    @Override
    public void onSortByForksDesc() {
        sortBy = SortBy.FORKS_DESC;
        page = FIRST_PAGE;
        searchRepositories();
    }

    @Override
    public void onSortByForksAsc() {
        sortBy = SortBy.FORKS_ASC;
        page = FIRST_PAGE;
        searchRepositories();
    }

    @Override
    public void onSortByUpdatedDesc() {
        sortBy = SortBy.UPDATED_DESC;
        page = FIRST_PAGE;
        searchRepositories();
    }

    @Override
    public void onSortByUpdatedAsc() {
        sortBy = SortBy.UPDATED_ASC;
        page = FIRST_PAGE;
        searchRepositories();
    }

    @Override
    public void onLoadNextPage() {
        if (!shouldLoadMore || isLoading) {
            return;
        }
        view.showLoadingMore();
        page++;
        searchRepositories();
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void onRepositoryClick(@NonNull Repository repository) {
        // todo: open detail screen
    }

    private void searchRepositories() {
        if (searchQuery == null) {
            return;
        }
        view.hideError();
        view.showProgress();
        isLoading = true;
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
                                       totalItemCount = repositoriesResponse.getTotalCount();
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
        isLoading = false;
        final List<Repository> repositories = new ArrayList<>();
        for (com.evastos.githubrepos.data.model.response.Repository repositoryItem : data) {
            repositories.add(new Repository(repositoryItem));
        }
        if (page == FIRST_PAGE) {
            this.repositories = repositories;
        } else {
            this.repositories.addAll(repositories);
        }
        if (!view.isAdded()) {
            return;
        }
        shouldLoadMore = this.repositories.size() < totalItemCount;
        view.hideProgress();
        view.hideLoadingMore();
        view.showResultsTitle(searchQuery, sortBy.toString());
        if (page == FIRST_PAGE) {
            view.showRepositories(repositories);
        } else {
            view.addRepositories(repositories);
        }
    }

    private void onError() {
        isLoading = false;
        if (!view.isAdded()) {
            return;
        }
        view.hideLoadingMore();
        view.hideProgress();
        view.showError(this);
    }
}
