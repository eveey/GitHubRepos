/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SortByTest {

    @Test
    public void toString_withSortByBestMatch_returnsCorrectString() {
        final SortBy sortBy = SortBy.BEST_MATCH;

        assertEquals("best match", sortBy.toString());
    }

    @Test
    public void toString_withSortByForksAsc_returnsCorrectString() {
        final SortBy sortBy = SortBy.FORKS_ASC;

        assertEquals("forks asc", sortBy.toString());
    }

    @Test
    public void toString_withSortByForksDesc_returnsCorrectString() {
        final SortBy sortBy = SortBy.FORKS_DESC;

        assertEquals("forks desc", sortBy.toString());
    }

    @Test
    public void toString_withSortByStarsAsc_returnsCorrectString() {
        final SortBy sortBy = SortBy.STARS_ASC;

        assertEquals("stars asc", sortBy.toString());
    }

    @Test
    public void toString_withSortByStarsDesc_returnsCorrectString() {
        final SortBy sortBy = SortBy.STARS_DESC;

        assertEquals("stars desc", sortBy.toString());
    }

    @Test
    public void toString_withSortByUpdatedAsc_returnsCorrectString() {
        final SortBy sortBy = SortBy.UPDATED_ASC;

        assertEquals("updated asc", sortBy.toString());
    }

    @Test
    public void toString_withSortByUpdatedDesc_returnsCorrectString() {
        final SortBy sortBy = SortBy.UPDATED_DESC;

        assertEquals("updated desc", sortBy.toString());
    }
}