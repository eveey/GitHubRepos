/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

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
    private final String repositoryFullName;

    @NonNull
    private final String ownerAvatarUrl;

    @NonNull
    private final String ownerName;

    @NonNull
    private final String description;

    @Nullable
    private final String language;

    private final int watchersCount;

    private final int forksCount;

    private final int issuesCount;

    private final int stargazersCount;

    @NonNull
    private final String repositoryUrl;

    @NonNull
    private final Date dateCreated;

    @NonNull
    private final Date dateUpdated;

    public Repository(@NonNull final com.evastos.githubrepos.data.model.response.Repository repository) {
        repositoryName = repository.getName();
        repositoryFullName = repository.getFullName();
        ownerAvatarUrl = repository.getOwner().getAvatarUrl();
        ownerName = repository.getOwner().getLogin();
        description = repository.getDescription();
        language = repository.getLanguage();
        watchersCount = repository.getWatchersCount();
        forksCount = repository.getForksCount();
        issuesCount = repository.getOpenIssuesCount();
        stargazersCount = repository.getStargazersCount();
        repositoryUrl = repository.getHtmlUrl();
        dateCreated = repository.getCreatedAt();
        dateUpdated = repository.getUpdatedAt();
    }

    private Repository(Parcel in) {
        repositoryName = in.readString();
        repositoryFullName = in.readString();
        ownerAvatarUrl = in.readString();
        ownerName = in.readString();
        description = in.readString();
        language = in.readString();
        watchersCount = in.readInt();
        forksCount = in.readInt();
        issuesCount = in.readInt();
        stargazersCount = in.readInt();
        repositoryUrl = in.readString();
        dateCreated = new Date(in.readLong());
        dateUpdated = new Date(in.readLong());
    }

    @NonNull
    public String getRepositoryName() {
        return repositoryName;
    }

    @NonNull
    public String getRepositoryFullName() {
        return repositoryFullName;
    }

    @NonNull
    public String getOwnerAvatarUrl() {
        return ownerAvatarUrl;
    }

    @NonNull
    public String getOwnerName() {
        return ownerName;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getLanguage() {
        return language;
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

    public int getStargazersCount() {
        return stargazersCount;
    }

    @NonNull
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    @NonNull
    public Date getDateCreated() {
        return dateCreated;
    }

    @NonNull
    public Date getDateUpdated() {
        return dateUpdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(repositoryName);
        dest.writeString(repositoryFullName);
        dest.writeString(ownerAvatarUrl);
        dest.writeString(ownerName);
        dest.writeString(description);
        dest.writeString(language);
        dest.writeInt(watchersCount);
        dest.writeInt(forksCount);
        dest.writeInt(issuesCount);
        dest.writeInt(stargazersCount);
        dest.writeString(repositoryUrl);
        dest.writeLong(dateCreated.getTime());
        dest.writeLong(dateUpdated.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Repository that = (Repository) o;

        if (watchersCount != that.watchersCount) return false;
        if (forksCount != that.forksCount) return false;
        if (issuesCount != that.issuesCount) return false;
        if (stargazersCount != that.stargazersCount) return false;
        if (!repositoryName.equals(that.repositoryName)) return false;
        if (!repositoryFullName.equals(that.repositoryFullName)) return false;
        if (!ownerAvatarUrl.equals(that.ownerAvatarUrl)) return false;
        if (!ownerName.equals(that.ownerName)) return false;
        if (!description.equals(that.description)) return false;
        if (language != null ? !language.equals(that.language) : that.language != null)
            return false;
        if (!repositoryUrl.equals(that.repositoryUrl)) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return dateUpdated.equals(that.dateUpdated);
    }

    @Override
    public int hashCode() {
        int result = repositoryName.hashCode();
        result = 31 * result + repositoryFullName.hashCode();
        result = 31 * result + ownerAvatarUrl.hashCode();
        result = 31 * result + ownerName.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + watchersCount;
        result = 31 * result + forksCount;
        result = 31 * result + issuesCount;
        result = 31 * result + stargazersCount;
        result = 31 * result + repositoryUrl.hashCode();
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + dateUpdated.hashCode();
        return result;
    }
}
