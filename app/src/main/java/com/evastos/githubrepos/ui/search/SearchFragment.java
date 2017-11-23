/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evastos.githubrepos.GitHubReposApp;
import com.evastos.githubrepos.R;
import com.evastos.githubrepos.data.service.GitHubService;
import com.evastos.githubrepos.ui.base.BaseFragment;
import com.evastos.githubrepos.ui.model.Repository;
import com.evastos.githubrepos.ui.model.SearchState;
import com.evastos.githubrepos.ui.model.SortBy;
import com.evastos.githubrepos.ui.repository_detail.RepositoryDetailActivity;
import com.evastos.githubrepos.ui.search.adapter.RepositoryAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment for searching repositories on GitHub.
 */
public class SearchFragment extends BaseFragment implements SearchContract.View {

    private static final String BUNDLE_STATE_REPOSITORIES = "repositories";

    private static final String BUNDLE_STATE_SORT_BY = "sortBy";

    private static final String BUNDLE_STATE_PAGE = "page";

    private static final String BUNDLE_STATE_SEARCH_QUERY = "searchQuery";

    @BindView(R.id.fragment_search_search_view_repositories)
    SearchView searchView;

    @BindView(R.id.fragment_search_text_view_repositories_result)
    TextView resultsTextView;

    @BindView(R.id.fragment_search_recycler_view_repositories)
    RecyclerView repositoriesRecyclerView;

    @BindView(R.id.progress_bar_horizontal)
    ProgressBar progressBar;

    @Inject
    GitHubService gitHubService;

    @Nullable
    private RepositoryAdapter repositoryAdapter;

    @NonNull
    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            final int totalItemCount = linearLayoutManager.getItemCount();
            final int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            final int visibleThreshold = 5;
            if (!presenter.isLoading() && totalItemCount <= (lastVisibleItem + visibleThreshold) && dy > 0) {
                //End of the items
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        presenter.onLoadNextPage();
                    }
                });
            }
        }
    };

    private SearchContract.Presenter presenter;

    private Unbinder unbinder;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView.setOnQueryTextListener(this);
        setHasOptionsMenu(true);
        repositoriesRecyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_sort_best_match:
                presenter.onSortByBestMatch();
                return true;
            case R.id.menu_item_sort_stars_asc:
                presenter.onSortByStarsAsc();
                return true;
            case R.id.menu_item_sort_stars_desc:
                presenter.onSortByStarsDesc();
                return true;
            case R.id.menu_item_sort_forks_asc:
                presenter.onSortByForksAsc();
                return true;
            case R.id.menu_item_sort_forks_desc:
                presenter.onSortByForksDesc();
                return true;
            case R.id.menu_item_sort_updated_asc:
                presenter.onSortByUpdatedAsc();
                return true;
            case R.id.menu_item_sort_updated_desc:
                presenter.onSortByUpdatedDesc();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GitHubReposApp.getApp(getActivity()).getAppComponent().inject(this);
        presenter = new SearchPresenter(this, gitHubService);
        final ArrayList<Repository> repositories;
        final String searchQuery;
        final SortBy sortBy;
        final int page;
        if (savedInstanceState == null) {
            repositories = null;
            searchQuery = null;
            sortBy = SortBy.BEST_MATCH;
            page = 1;
        } else {
            repositories = savedInstanceState.containsKey(BUNDLE_STATE_REPOSITORIES)
                    ? savedInstanceState.<Repository>getParcelableArrayList(BUNDLE_STATE_REPOSITORIES) : null;
            searchQuery = savedInstanceState.getString(BUNDLE_STATE_SEARCH_QUERY, null);
            final SortBy sortBySaved = (SortBy) savedInstanceState.getSerializable(BUNDLE_STATE_SORT_BY);
            sortBy = savedInstanceState.containsKey(BUNDLE_STATE_SORT_BY) && sortBySaved != null ? sortBySaved : SortBy.BEST_MATCH;
            page = savedInstanceState.getInt(BUNDLE_STATE_PAGE, 1);
        }
        final SearchState searchState = new SearchState(repositories, searchQuery, sortBy, page);
        presenter.onStart(searchState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        final SearchState searchState = presenter.onSaveState();
        outState.putParcelableArrayList(BUNDLE_STATE_REPOSITORIES, searchState.getRepositories());
        outState.putString(BUNDLE_STATE_SEARCH_QUERY, searchState.getSearchQuery());
        outState.putInt(BUNDLE_STATE_PAGE, searchState.getPage());
        outState.putSerializable(BUNDLE_STATE_SORT_BY, searchState.getSortBy());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.onSearchRepositories(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showResultsTitle(@NonNull String searchQuery, @NonNull String sortedBy) {
        resultsTextView.setVisibility(View.VISIBLE);
        resultsTextView.setText(getString(R.string.search_results_format, searchQuery, sortedBy));
    }

    @Override
    public void clearSearchFocus() {
        searchView.clearFocus();
    }

    @Override
    public void showRepositories(@NonNull final List<Repository> repositories) {
        repositoryAdapter = new RepositoryAdapter(getActivity(), repositories, this);
        repositoriesRecyclerView.setAdapter(repositoryAdapter);
    }

    @Override
    public void showLoadingMore() {
        if (repositoryAdapter == null) {
            return;
        }
        repositoryAdapter.setLoading(true);
    }

    @Override
    public void hideLoadingMore() {
        if (repositoryAdapter == null) {
            return;
        }
        repositoryAdapter.setLoading(false);
    }

    @Override
    public void addRepositories(@NonNull List<Repository> repositories) {
        if (repositoryAdapter == null) {
            return;
        }
        repositoryAdapter.addRepositories(repositories);
    }

    @Override
    public void openRepositoryDetail(@NonNull final Repository repository) {
        final Intent intent = RepositoryDetailActivity.getIntent(getActivity(), repository);
        startActivity(intent);
    }

    @Override
    public void onClick(@NonNull Repository repository) {
        presenter.onRepositoryClick(repository);
    }
}
