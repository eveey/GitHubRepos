/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.model;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SearchState {

    @Nullable
    private final ArrayList<Repository> repositories;

    @Nullable
    private final String searchQuery;

    @NonNull
    private final SortBy sortBy;

    @IntRange(from = 1)
    private final int page;

    public SearchState(@Nullable ArrayList<Repository> repositories,
                       @Nullable String searchQuery,
                       @NonNull SortBy sortBy,
                       @IntRange(from = 1) int page) {
        this.repositories = repositories;
        this.searchQuery = searchQuery;
        this.sortBy = sortBy;
        this.page = page;
    }

    @Nullable
    public ArrayList<Repository> getRepositories() {
        return repositories;
    }

    @Nullable
    public String getSearchQuery() {
        return searchQuery;
    }

    @NonNull
    public SortBy getSortBy() {
        return sortBy;
    }

    public int getPage() {
        return page;
    }
}
