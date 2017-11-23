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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchState that = (SearchState) o;

        if (page != that.page) return false;
        if (repositories != null ? !repositories.equals(that.repositories) : that.repositories != null)
            return false;
        if (searchQuery != null ? !searchQuery.equals(that.searchQuery) : that.searchQuery != null)
            return false;
        return sortBy == that.sortBy;
    }

    @Override
    public int hashCode() {
        int result = repositories != null ? repositories.hashCode() : 0;
        result = 31 * result + (searchQuery != null ? searchQuery.hashCode() : 0);
        result = 31 * result + sortBy.hashCode();
        result = 31 * result + page;
        return result;
    }
}
