/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.data.model.request;

import java.util.Locale;

/**
 * The sort order request field if sort parameter is provided. One of asc or desc. Default: desc
 */
public enum Ordering {

    ASC, DESC, NONE;

    @Override
    public String toString() {
        if (this == NONE) {
            return null;
        }
        return name().toLowerCase(Locale.US);
    }
}
