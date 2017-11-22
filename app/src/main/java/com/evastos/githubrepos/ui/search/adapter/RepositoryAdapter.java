/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.search.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.evastos.githubrepos.R;
import com.evastos.githubrepos.ui.model.Repository;

import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_REPOSITORY = 0;

    private static final int VIEW_TYPE_LOADING = 1;

    @NonNull
    private final List<Repository> repositories;

    @NonNull
    private final LayoutInflater layoutInflater;

    @NonNull
    private final String ownerFormat;

    @NonNull
    private final String watchersFormat;

    @NonNull
    private final String forksFormat;

    @NonNull
    private final String issuesFormat;

    @NonNull
    private final RepositoryClickListener repositoryClickListener;

    @NonNull
    private final RequestManager imageManager;

    @NonNull
    private final Drawable placeholderOwnerImage;

    @NonNull
    private final RequestOptions imageOptions;

    private boolean isLoading = false;

    public RepositoryAdapter(@NonNull final Context context,
                             @NonNull final List<Repository> repositories,
                             @NonNull final RepositoryClickListener repositoryClickListener) {
        this.repositories = new ArrayList<>(repositories);
        this.repositoryClickListener = repositoryClickListener;
        layoutInflater = LayoutInflater.from(context);
        imageManager = Glide.with(context);
        placeholderOwnerImage = context.getResources().getDrawable(R.drawable.ic_account_circle_black_36px);
        imageOptions = new RequestOptions().fitCenter().placeholder(placeholderOwnerImage).fallback(placeholderOwnerImage);
        ownerFormat = context.getString(R.string.repository_item_owner_format);
        watchersFormat = context.getString(R.string.repository_item_watchers_format);
        forksFormat = context.getString(R.string.repository_item_forks_format);
        issuesFormat = context.getString(R.string.repository_item_issues_format);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_REPOSITORY) {
            final View repositoryView = layoutInflater
                    .inflate(R.layout.recycler_view_item_repository, parent, false);
            return new RepositoryViewHolder(repositoryView);
        } else {
            final View loadingView = layoutInflater
                    .inflate(R.layout.recycler_view_item_loading, parent, false);
            return new LoadingViewHolder(loadingView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RepositoryViewHolder) {
            final RepositoryViewHolder repositoryViewHolder = (RepositoryViewHolder) holder;
            final Repository repository = repositories.get(position);
            repositoryViewHolder.updateView(repository);
        } else if (holder instanceof LoadingViewHolder) {
            final LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.updateView(isLoading);
        }
    }

    @Override
    public int getItemCount() {
        return repositories.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position < repositories.size() ? VIEW_TYPE_REPOSITORY : VIEW_TYPE_LOADING;
    }

    /**
     * Sets loading flag
     *
     * @param isLoading {@code true} if it's loading, {@code false} otherwise
     */
    public void setLoading(final boolean isLoading) {
        this.isLoading = isLoading;
        notifyDataSetChanged();
    }

    /**
     * Adds repositories to the list
     *
     * @param repositories the repositories to add
     */
    public void addRepositories(@NonNull final List<Repository> repositories) {
        this.repositories.addAll(repositories);
        notifyDataSetChanged();
    }

    private class RepositoryViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ImageView ownerImageView;

        @NonNull
        private final TextView repositoryTextView;

        @NonNull
        private final TextView ownerTextView;

        @NonNull
        private final TextView watchersTextView;

        @NonNull
        private final TextView forksTextView;

        @NonNull
        private final TextView issuesTextView;

        @NonNull
        private final View clickableView;

        RepositoryViewHolder(final View itemView) {
            super(itemView);
            ownerImageView = (ImageView) itemView.findViewById(R.id.recycler_view_item_repository_image_view_owner);
            repositoryTextView = (TextView) itemView.findViewById(R.id.recycler_view_item_repository_text_view_repository);
            ownerTextView = (TextView) itemView.findViewById(R.id.recycler_view_item_repository_text_view_owner);
            watchersTextView = (TextView) itemView.findViewById(R.id.recycler_view_item_repository_text_view_watchers);
            forksTextView = (TextView) itemView.findViewById(R.id.recycler_view_item_repository_text_view_forks);
            issuesTextView = (TextView) itemView.findViewById(R.id.recycler_view_item_repository_text_view_issues);
            clickableView = itemView.findViewById(R.id.recycler_view_item_repository_clickable_view);
        }

        void updateView(@NonNull final Repository repository) {
            repositoryTextView.setText(String.valueOf(repository.getRepositoryName()));
            ownerImageView.setImageDrawable(placeholderOwnerImage);
            ownerTextView.setText(String.format(ownerFormat, repository.getOwnerName()));
            watchersTextView.setText(String.format(watchersFormat, repository.getWatchersCount()));
            forksTextView.setText(String.format(forksFormat, repository.getForksCount()));
            issuesTextView.setText(String.format(issuesFormat, repository.getIssuesCount()));
            clickableView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    repositoryClickListener.onClick(repository);
                }
            });
            imageManager
                    .load(repository.getOwnerAvatarUrl())
                    .apply(imageOptions)
                    .into(ownerImageView);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ProgressBar loadingProgressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            loadingProgressBar = (ProgressBar) itemView.findViewById(R.id.recycler_view_item_loading_progress_bar);
        }

        void updateView(final boolean isLoading) {
            loadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }
}
