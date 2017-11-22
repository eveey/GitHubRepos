/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.search.adapter;

import android.support.annotation.NonNull;

import com.evastos.githubrepos.ui.model.Repository;

/**
 * Listener for user clicks on {@link Repository}
 */
public interface RepositoryClickListener {

    /**
     * When user clicks on repository item
     *
     * @param repository the repository
     */
    void onClick(@NonNull Repository repository);
}
