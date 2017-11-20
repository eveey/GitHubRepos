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

    STARS, FORKS, UPDATED;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.US);
    }
}
