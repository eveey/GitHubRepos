/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */
package com.evastos.githubrepos.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Repository model.
 */
public class Repository {

    private long id;

    private String name;

    private String fullName;

    private User owner;

    @SerializedName("private")
    private boolean isPrivate;

    private String htmlUrl;

    private String description;

    private boolean fork;

    private String url;

    private Date createdAt;

    private Date updatedAt;

    private Date pushedAt;

    private String homepage;

    private int size;

    private int stargazersCount;

    private int watchersCount;

    private String language;

    private int forksCount;

    private int openIssuesCount;

    private String masterBranch;

    private String defaultBranch;

    private float score;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFork() {
        return fork;
    }

    public String getUrl() {
        return url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getPushedAt() {
        return pushedAt;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getSize() {
        return size;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public String getLanguage() {
        return language;
    }

    public int getForksCount() {
        return forksCount;
    }

    public int getOpenIssuesCount() {
        return openIssuesCount;
    }

    public String getMasterBranch() {
        return masterBranch;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public float getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", owner=" + owner +
                ", isPrivate=" + isPrivate +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", description='" + description + '\'' +
                ", fork=" + fork +
                ", url='" + url + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", pushedAt=" + pushedAt +
                ", homepage='" + homepage + '\'' +
                ", size=" + size +
                ", stargazersCount=" + stargazersCount +
                ", watchersCount=" + watchersCount +
                ", language='" + language + '\'' +
                ", forksCount=" + forksCount +
                ", openIssuesCount=" + openIssuesCount +
                ", masterBranch='" + masterBranch + '\'' +
                ", defaultBranch='" + defaultBranch + '\'' +
                ", score=" + score +
                '}';
    }
}
