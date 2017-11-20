/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.search;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.evastos.githubrepos.GitHubReposApp;
import com.evastos.githubrepos.R;
import com.evastos.githubrepos.data.service.GitHubService;
import com.evastos.githubrepos.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends BaseFragment implements SearchView.OnQueryTextListener {

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GitHubReposApp.getApp(getActivity()).getAppComponent().inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        resultsTextView.setVisibility(View.VISIBLE);
        resultsTextView.setText(getString(R.string.search_results_format, query));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
