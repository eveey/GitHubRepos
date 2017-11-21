/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.data.model.request;

import java.util.Locale;

/**
 * The sort request field. One of stars, forks, or updated. Default: results are sorted by best match.
 */
public enum Sorting {

    STARS, FORKS, UPDATED, NONE;

    @Override
    public String toString() {
        if (this == NONE) {
            return null;
        }
        return name().toLowerCase(Locale.US);
    }
}
