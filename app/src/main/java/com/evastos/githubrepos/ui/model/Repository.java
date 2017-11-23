/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * UI model for GitHub repository;
 */
public class Repository implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };

    @NonNull
    private final String repositoryName;

    @NonNull
    private final String ownerAvatarUrl;

    @NonNull
    private final String ownerName;

    private int watchersCount;

    private int forksCount;

    private int issuesCount;

    public Repository(@NonNull final com.evastos.githubrepos.data.model.response.Repository repository) {
        repositoryName = repository.getName();
        ownerName = repository.getOwner().getLogin();
        ownerAvatarUrl = repository.getOwner().getAvatarUrl();
        watchersCount = repository.getWatchersCount();
        forksCount = repository.getForksCount();
        issuesCount = repository.getOpenIssuesCount();
    }

    private Repository(Parcel in) {
        repositoryName = in.readString();
        ownerAvatarUrl = in.readString();
        ownerName = in.readString();
        watchersCount = in.readInt();
        forksCount = in.readInt();
        issuesCount = in.readInt();
    }

    @NonNull
    public String getRepositoryName() {
        return repositoryName;
    }

    @NonNull
    public String getOwnerAvatarUrl() {
        return ownerAvatarUrl;
    }

    @NonNull
    public String getOwnerName() {
        return ownerName;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public int getIssuesCount() {
        return issuesCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(repositoryName);
        dest.writeString(ownerAvatarUrl);
        dest.writeString(ownerName);
        dest.writeInt(watchersCount);
        dest.writeInt(forksCount);
        dest.writeInt(issuesCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Repository that = (Repository) o;

        if (watchersCount != that.watchersCount) return false;
        if (forksCount != that.forksCount) return false;
        if (issuesCount != that.issuesCount) return false;
        if (!repositoryName.equals(that.repositoryName)) return false;
        if (!ownerAvatarUrl.equals(that.ownerAvatarUrl)) return false;
        return ownerName.equals(that.ownerName);
    }

    @Override
    public int hashCode() {
        int result = repositoryName.hashCode();
        result = 31 * result + ownerAvatarUrl.hashCode();
        result = 31 * result + ownerName.hashCode();
        result = 31 * result + watchersCount;
        result = 31 * result + forksCount;
        result = 31 * result + issuesCount;
        return result;
    }
}
