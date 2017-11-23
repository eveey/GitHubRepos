/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.repository_detail;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.evastos.githubrepos.R;
import com.evastos.githubrepos.ui.model.Repository;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RepositoryDetailActivity extends AppCompatActivity {

    private static final String BUNDLE_REPOSITORY = "repository";

    @BindView(R.id.activity_repository_detail_toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;

    /**
     * Gets intent to start repository detail activity with repository argument
     *
     * @param context    the context
     * @param repository the repository
     * @return the intent for starting repository detail activity
     */
    @NonNull
    public static Intent getIntent(@NonNull final Context context, @NonNull final Repository repository) {
        final Intent intent = new Intent(context, RepositoryDetailActivity.class);
        intent.putExtra(BUNDLE_REPOSITORY, repository);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_detail);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final FragmentManager fragmentManager = getFragmentManager();
        RepositoryDetailFragment fragment =
                (RepositoryDetailFragment) fragmentManager.findFragmentByTag(RepositoryDetailFragment.TAG);
        if (fragment == null) {
            fragment = RepositoryDetailFragment.getInstance(getRepository());
        }
        fragmentManager
                .beginTransaction()
                .replace(R.id.activity_repository_detail_fragment_container, fragment, RepositoryDetailFragment.TAG)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @NonNull
    private Repository getRepository() {
        final Bundle bundle = getIntent().getExtras();
        Repository repository = null;
        if (bundle != null && bundle.containsKey(BUNDLE_REPOSITORY)) {
            repository = bundle.getParcelable(BUNDLE_REPOSITORY);
        }
        if (repository == null) {
            throw new IllegalArgumentException("Activity started without repository extra");
        }
        return repository;
    }
}
