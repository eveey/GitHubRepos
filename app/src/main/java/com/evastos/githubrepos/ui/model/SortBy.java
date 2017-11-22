/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.model;

import android.support.annotation.NonNull;

import com.evastos.githubrepos.data.model.request.Ordering;
import com.evastos.githubrepos.data.model.request.Sorting;

import java.util.Locale;

/**
 * UI model for sorting and ordering.
 */
public enum SortBy {

    BEST_MATCH(Sorting.NONE, Ordering.NONE),
    STARS_DESC(Sorting.STARS, Ordering.DESC),
    STARS_ASC(Sorting.STARS, Ordering.ASC),
    FORKS_DESC(Sorting.FORKS, Ordering.DESC),
    FORKS_ASC(Sorting.FORKS, Ordering.ASC),
    UPDATED_DESC(Sorting.UPDATED, Ordering.DESC),
    UPDATED_ASC(Sorting.UPDATED, Ordering.ASC);

    @NonNull
    private final Sorting sorting;

    @NonNull
    private final Ordering ordering;

    SortBy(@NonNull final Sorting sorting,
           @NonNull final Ordering ordering) {
        this.sorting = sorting;
        this.ordering = ordering;
    }

    @NonNull
    public Sorting getSorting() {
        return sorting;
    }

    @NonNull
    public Ordering getOrdering() {
        return ordering;
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.US).replace("_", " ");
    }
}
