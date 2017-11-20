/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.base;

import android.support.annotation.NonNull;

public interface BaseView {

    /**
     * Returns {@code true} if view is added to the hierarchy.
     *
     * @return true is view is added, false otherwise
     */
    boolean isAdded();

    /**
     * Shows error {@link android.support.design.widget.Snackbar} with refresh option
     *
     * @param errorViewListener listener for user click on refresh option
     */
    void showError(@NonNull ErrorViewListener errorViewListener);

    /**
     * Hides error {@link android.support.design.widget.Snackbar}
     */
    void hideError();
}
