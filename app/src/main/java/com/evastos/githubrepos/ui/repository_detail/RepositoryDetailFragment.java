/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.repository_detail;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.evastos.githubrepos.GitHubReposApp;
import com.evastos.githubrepos.R;
import com.evastos.githubrepos.ui.model.Repository;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Fragment for showing GitHub repository details.
 */
public class RepositoryDetailFragment extends Fragment implements RepositoryDetailContract.View {

    public static final String TAG = RepositoryDetailFragment.class.getName();

    private static final String BUNDLE_REPOSITORY = "repository";

    @BindView(R.id.fragment_repository_detail_text_view_repository_name)
    TextView repositoryNameTextView;

    @BindView(R.id.fragment_repository_detail_image_view_owner)
    ImageView ownerImageView;

    @BindView(R.id.fragment_repository_detail_text_view_owner_name)
    TextView ownerNameTextView;

    @BindView(R.id.fragment_repository_detail_text_view_date_created)
    TextView dateCreatedTextView;

    @BindView(R.id.fragment_repository_detail_text_view_date_updated)
    TextView dateUpdatedTextView;

    @BindView(R.id.fragment_repository_detail_text_view_language)
    TextView languageTextView;

    @BindView(R.id.fragment_repository_detail_text_view_description)
    TextView descriptionTextView;

    @BindView(R.id.fragment_repository_detail_text_view_forks)
    TextView forksTextView;

    @BindView(R.id.fragment_repository_detail_text_view_watchers)
    TextView watchersTextView;

    @BindView(R.id.fragment_repository_detail_text_view_issues)
    TextView issuesTextView;

    @BindView(R.id.fragment_repository_detail_text_view_stargazers)
    TextView stargazersTextView;

    @NonNull
    private String ownerFormat;

    @NonNull
    private String watchersFormat;

    @NonNull
    private String forksFormat;

    @NonNull
    private String issuesFormat;

    @NonNull
    private String stargazersFormat;

    @NonNull
    private String languageFormat;

    @NonNull
    private String dateCreatedFormat;

    @NonNull
    private String dateUpdatedFormat;

    @NonNull
    private RequestManager imageManager;

    @NonNull
    private RequestOptions imageOptions;

    private RepositoryDetailContract.Presenter presenter;

    private Unbinder unbinder;

    /**
     * Returns instance of {@link RepositoryDetailFragment} with {@link Repository} as argument
     *
     * @param repository the repository
     * @return instance of {@link RepositoryDetailFragment}
     */
    @NonNull
    public static RepositoryDetailFragment getInstance(@NonNull final Repository repository) {
        final Bundle args = new Bundle();
        args.putParcelable(BUNDLE_REPOSITORY, repository);
        final RepositoryDetailFragment repositoryDetailFragment = new RepositoryDetailFragment();
        repositoryDetailFragment.setArguments(args);
        return repositoryDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_repository_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();
        GitHubReposApp.getApp(activity).getAppComponent().inject(this);
        imageManager = Glide.with(activity);
        final Drawable placeholderOwnerImage = activity.getResources().getDrawable(R.drawable.ic_account_circle_black_48px);
        imageOptions = new RequestOptions().fitCenter().placeholder(placeholderOwnerImage).fallback(placeholderOwnerImage);
        ownerFormat = activity.getString(R.string.owner_format);
        watchersFormat = activity.getString(R.string.watchers_format);
        forksFormat = activity.getString(R.string.forks_format);
        issuesFormat = activity.getString(R.string.issues_format);
        stargazersFormat = activity.getString(R.string.stargazers_format);
        languageFormat = activity.getString(R.string.language_format);
        dateCreatedFormat = activity.getString(R.string.date_created_format);
        dateUpdatedFormat = activity.getString(R.string.date_updated_format);
        presenter = new RepositoryDetailPresenter(this, getRepository(savedInstanceState));
        presenter.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_REPOSITORY, presenter.onSaveState());
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.fragment_repository_detail_button_open)
    public void onButtonOpenRepositoryClick() {
        presenter.onOpenRepositoryUrl();
    }

    @Override
    public void showRepositoryName(@NonNull final String repositoryName) {
        repositoryNameTextView.setText(repositoryName);
    }

    @Override
    public void showOwnerImage(@NonNull final String ownerAvatarUrl) {
        imageManager
                .load(ownerAvatarUrl)
                .apply(imageOptions)
                .into(ownerImageView);
    }

    @Override
    public void showOwnerName(@NonNull final String ownerName) {
        ownerNameTextView.setText(String.format(ownerFormat, ownerName));
    }

    @Override
    public void showDescription(@NonNull final String description) {
        if (TextUtils.isEmpty(description)) {
            descriptionTextView.setVisibility(View.GONE);
        } else {
            descriptionTextView.setVisibility(View.VISIBLE);
            descriptionTextView.setText(description);
        }
    }

    @Override
    public void showDateCreated(@NonNull final String dateCreated) {
        dateCreatedTextView.setText(String.format(dateCreatedFormat, dateCreated));
    }

    @Override
    public void showDateUpdated(@NonNull final String dateUpdated) {
        dateUpdatedTextView.setText(String.format(dateUpdatedFormat, dateUpdated));
    }

    @Override
    public void showLanguage(@Nullable final String language) {
        languageTextView.setText(String.format(languageFormat, language == null ? "-" : language));
    }

    @Override
    public void showWatchers(final int watchersCount) {
        watchersTextView.setText(String.format(watchersFormat, watchersCount));
    }

    @Override
    public void showForks(final int forksCount) {
        forksTextView.setText(String.format(forksFormat, forksCount));
    }

    @Override
    public void showIssues(final int issuesCount) {
        issuesTextView.setText(String.format(issuesFormat, issuesCount));
    }

    @Override
    public void showStargazers(final int stargazersCount) {
        stargazersTextView.setText(String.format(stargazersFormat, stargazersCount));
    }

    @Override
    public void openUrl(@NonNull final String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @NonNull
    private Repository getRepository(@Nullable Bundle savedInstanceState) {
        Repository repository = null;
        if (savedInstanceState == null) {
            final Bundle args = getArguments();
            if (args != null && args.containsKey(BUNDLE_REPOSITORY)) {
                repository = args.getParcelable(BUNDLE_REPOSITORY);
            }
        } else if (savedInstanceState.containsKey(BUNDLE_REPOSITORY)) {
            repository = savedInstanceState.getParcelable(BUNDLE_REPOSITORY);
        }
        if (repository == null) {
            throw new IllegalArgumentException("Fragment started without repository extra");
        }
        return repository;
    }
}
