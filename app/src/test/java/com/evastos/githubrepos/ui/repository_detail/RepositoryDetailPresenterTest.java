/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.repository_detail;

import com.evastos.githubrepos.ui.model.Repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RepositoryDetailPresenterTest {

    private static final long TEST_TIMESTAMP = 1510856625;

    private static final long TEST_TIMESTAMP2 = 99000900625L;

    private static final Date TEST_DATE = new Date(TEST_TIMESTAMP);

    private static final Date TEST_DATE2 = new Date(TEST_TIMESTAMP2);

    @Mock
    private Repository repository;

    @Mock
    private RepositoryDetailContract.View view;

    private RepositoryDetailContract.Presenter presenter;

    @Before
    public void setUp() {
        initMocks(this);
        when(repository.getRepositoryFullName()).thenReturn("fullName");
        when(repository.getDateCreated()).thenReturn(TEST_DATE);
        when(repository.getDateUpdated()).thenReturn(TEST_DATE2);
        when(repository.getDescription()).thenReturn("description");
        when(repository.getLanguage()).thenReturn("language");
        when(repository.getOwnerAvatarUrl()).thenReturn("ownerAvatarUrl");
        when(repository.getOwnerName()).thenReturn("ownerName");
        when(repository.getWatchersCount()).thenReturn(182);
        when(repository.getForksCount()).thenReturn(763);
        when(repository.getIssuesCount()).thenReturn(543);
        when(repository.getStargazersCount()).thenReturn(1000);
        when(repository.getRepositoryUrl()).thenReturn("repositoryUrl");

        presenter = new RepositoryDetailPresenter(view, repository);
    }

    @Test
    public void onStart_showsRepositoryName() {
        presenter.onStart();

        verify(view).showRepositoryName("fullName");
    }

    @Test
    public void onStart_showsDateCreated() {
        presenter.onStart();

        verify(view).showDateCreated("Jan 18, 1970");
    }

    @Test
    public void onStart_showsDateUpdated() {
        presenter.onStart();

        verify(view).showDateUpdated("Feb 19, 1973");
    }

    @Test
    public void onStart_showsDescription() {
        presenter.onStart();

        verify(view).showDescription("description");
    }

    @Test
    public void onStart_showsLanguage() {
        presenter.onStart();

        verify(view).showLanguage("language");
    }

    @Test
    public void onStart_showsOwnerImage() {
        presenter.onStart();

        verify(view).showOwnerImage("ownerAvatarUrl");
    }

    @Test
    public void onStart_showsOwnerName() {
        presenter.onStart();

        verify(view).showOwnerName("ownerName");
    }

    @Test
    public void onStart_showsWatchers() {
        presenter.onStart();

        verify(view).showWatchers(182);
    }

    @Test
    public void onStart_showsForks() {
        presenter.onStart();

        verify(view).showForks(763);
    }

    @Test
    public void onStart_showsIssues() {
        presenter.onStart();

        verify(view).showIssues(543);
    }

    @Test
    public void onStart_showsStargazers() {
        presenter.onStart();

        verify(view).showStargazers(1000);
    }

    @Test
    public void onSaveState_returnsRepository() {
        final Repository repository = presenter.onSaveState();

        assertEquals(this.repository, repository);
    }

    @Test
    public void onOpenRepositoryUrl_opensUrl() {
        presenter.onOpenRepositoryUrl();

        verify(view).openUrl("repositoryUrl");
    }
}