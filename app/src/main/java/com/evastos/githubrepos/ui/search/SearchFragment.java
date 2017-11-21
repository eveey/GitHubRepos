/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.evastos.githubrepos.GitHubReposApp;
import com.evastos.githubrepos.R;
import com.evastos.githubrepos.data.service.GitHubService;
import com.evastos.githubrepos.ui.base.BaseFragment;
import com.evastos.githubrepos.ui.model.Repository;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment for searching repositories on GitHub.
 */
public class SearchFragment extends BaseFragment implements SearchContract.View {

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
        presenter.onRestoreState(savedInstanceState);
        presenter.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveState(outState);
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
        Toast.makeText(getActivity(), String.valueOf(repositories), Toast.LENGTH_LONG).show();
    }
}
