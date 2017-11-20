/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.data.model.response;

import java.util.List;

/**
 * Repositories response model.
 */
public class RepositoriesResponse {

    private int totalCount;

    private boolean incompleteResults;

    private List<Repository> items;

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public List<Repository> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "RepositoriesResponse{" +
                "totalCount=" + totalCount +
                ", incompleteResults=" + incompleteResults +
                ", items=" + items +
                '}';
    }
}
