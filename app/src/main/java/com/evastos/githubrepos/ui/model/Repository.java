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

    private final long repositoryId;

    public Repository(@NonNull final com.evastos.githubrepos.data.model.response.Repository repository) {
        this.repositoryId = repository.getId();
    }

    @Override
    public String toString() {
        return "Repository{" +
                "repositoryId=" + repositoryId +
                '}';
    }

    private Repository(Parcel in) {
        this.repositoryId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(repositoryId);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };
}
