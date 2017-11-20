/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.base;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.evastos.githubrepos.R;

public abstract class BaseFragment extends Fragment implements BaseView {

    @Nullable
    private Snackbar errorSnackbar;

    @Override
    public void showError(@NonNull final ErrorViewListener errorViewListener) {
        final View view = getView();
        if (view == null) {
            return;
        }
        errorSnackbar = Snackbar.make(getView(), getString(R.string.error_message), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.error_button_refresh),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorViewListener.onErrorRefreshClicked();
                            }
                        });
        errorSnackbar.show();
    }

    @Override
    public void hideError() {
        if (errorSnackbar != null && errorSnackbar.isShown()) {
            errorSnackbar.dismiss();
        }
    }
}
