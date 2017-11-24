/*
 * Copyright (c) 2017 Eva Štos.
 * All rights reserved.
 */

package com.evastos.githubrepos.data.service;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Size;

import com.evastos.githubrepos.data.model.request.Ordering;
import com.evastos.githubrepos.data.model.request.Sorting;
import com.evastos.githubrepos.data.model.response.RepositoriesResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Service for fetching GitHub repositories data.
 */
public interface GitHubService {

    @GET("search/repositories")
    Single<RepositoriesResponse> searchRepositories(@Query("q") @NonNull @Size(min = 1) String searchQuery,
                                                    @Query("sort") @NonNull Sorting sorting,
                                                    @Query("order") @NonNull Ordering ordering,
                                                    @Query("page") @IntRange(from = 1) int page,
                                                    @Query("per_page") @IntRange(from = 1) int perPage);
}
