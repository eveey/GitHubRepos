/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.search;

import com.evastos.githubrepos.data.model.request.Ordering;
import com.evastos.githubrepos.data.model.request.Sorting;
import com.evastos.githubrepos.data.model.response.RepositoriesResponse;
import com.evastos.githubrepos.data.model.response.User;
import com.evastos.githubrepos.data.service.GitHubService;
import com.evastos.githubrepos.ui.model.Repository;
import com.evastos.githubrepos.ui.model.SearchState;
import com.evastos.githubrepos.ui.model.SortBy;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchPresenterTest {

    private static final int THREAD_SLEEP_MILLIS = 20;

    @Mock
    private SearchContract.View view;

    @Mock
    private GitHubService gitHubService;

    private final RepositoriesResponse testResponse = getTestResponse();

    private final ArrayList<Repository> testRepositories = getTestRepositories();

    private SearchContract.Presenter presenter;

    @Before
    public void setUp() {
        initMocks(this);
        presenter = new SearchPresenter(view, gitHubService);
        when(gitHubService.searchRepositories(anyString(), any(Sorting.class), any(Ordering.class), anyInt(), anyInt()))
                .thenReturn(Single.just(testResponse));
    }

    @BeforeClass
    public static void setUpClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @AfterClass
    public static void tearDownClass() {
        RxAndroidPlugins.reset();
    }

    @Test
    public void onStart_withSearchState_withSearchQuery_showsRepositories() {
        final SearchState searchState = new SearchState(testRepositories, "qwerty", SortBy.STARS_ASC, 5);

        presenter.onStart(searchState);

        verify(view).showRepositories(testRepositories);
    }

    @Test
    public void onStart_withSavedState_withSearchQuery_showsResultsTitle() {
        final SearchState searchState = new SearchState(testRepositories, "qwerty", SortBy.UPDATED_DESC, 5);

        presenter.onStart(searchState);

        verify(view).showResultsTitle("qwerty", "updated desc");
    }

    @Test
    public void onStart_withSearchState_withoutSearchQuery_doesNothing() {
        final SearchState searchState = new SearchState(testRepositories, null, SortBy.UPDATED_DESC, 5);

        presenter.onStart(searchState);

        verify(view, never()).showRepositories(ArgumentMatchers.<Repository>anyList());
        verify(view, never()).showResultsTitle(anyString(), anyString());
    }

    @Test
    public void onSaveState_defaultState_returnsDefaultState() {
        final SearchState expectedState = new SearchState(new ArrayList<Repository>(), null, SortBy.BEST_MATCH, 1);

        final SearchState searchState = presenter.onSaveState();

        assertEquals(expectedState, searchState);
    }

    @Test
    public void onSaveState_changedState_returnsChangedState() throws InterruptedException {
        ArrayList<Repository> allRepositories = new ArrayList<>();
        allRepositories.addAll(testRepositories);
        allRepositories.addAll(testRepositories);
        allRepositories.addAll(testRepositories);
        final SearchState expectedState = new SearchState(allRepositories, "qwe", SortBy.FORKS_DESC, 3);
        presenter.onSearchRepositories("qwe");
        sleep(THREAD_SLEEP_MILLIS);
        presenter.onSortByForksDesc();
        sleep(THREAD_SLEEP_MILLIS);
        presenter.onLoadNextPage();
        sleep(THREAD_SLEEP_MILLIS);
        presenter.onLoadNextPage();
        sleep(THREAD_SLEEP_MILLIS);

        final SearchState searchState = presenter.onSaveState();

        assertEquals(expectedState.getRepositories(), searchState.getRepositories());
    }

    @Test
    public void onSearchRepositories_changedState_returnsChangedState() throws InterruptedException {
        ArrayList<Repository> allRepositories = new ArrayList<>();
        allRepositories.addAll(testRepositories);
        allRepositories.addAll(testRepositories);
        allRepositories.addAll(testRepositories);
        final SearchState expectedState = new SearchState(allRepositories, "qwe", SortBy.FORKS_DESC, 3);
        presenter.onSearchRepositories("qwe");
        sleep(THREAD_SLEEP_MILLIS);
        presenter.onSortByForksDesc();
        sleep(THREAD_SLEEP_MILLIS);
        presenter.onLoadNextPage();
        sleep(THREAD_SLEEP_MILLIS);
        presenter.onLoadNextPage();
        sleep(THREAD_SLEEP_MILLIS);

        final SearchState searchState = presenter.onSaveState();

        assertEquals(expectedState.getRepositories(), searchState.getRepositories());
    }

    @Test
    public void onSearchRepositories_clearsSearchFocus() {
        presenter.onSearchRepositories("wrt");

        verify(view).clearSearchFocus();
    }

    @Test
    public void onSearchRepositories_callsGitHubServiceWithCorrectParameters() {
        presenter.onSearchRepositories("wrt");

        verify(gitHubService).searchRepositories("wrt", Sorting.NONE, Ordering.NONE, 1, 50);
    }

    @Test
    public void onSearchRepositories_hidesError() {
        presenter.onSearchRepositories("wrt");

        verify(view).hideError();
    }

    @Test
    public void onSearchRepositories_showsProgress() {
        presenter.onSearchRepositories("wrt");

        verify(view).showProgress();
    }

    @Test
    public void onSearchRepositories_onRepositoriesSuccess_withViewAdded_hidesProgress() throws InterruptedException {
        when(view.isAdded()).thenReturn(true);

        presenter.onSearchRepositories("wrt");
        sleep(THREAD_SLEEP_MILLIS);

        verify(view).hideProgress();
    }

    @Test
    public void onSearchRepositories_onRepositoriesSuccess_withViewAdded_hidesLoadingMore() throws InterruptedException {
        when(view.isAdded()).thenReturn(true);

        presenter.onSearchRepositories("wrt");
        sleep(THREAD_SLEEP_MILLIS);

        verify(view).hideLoadingMore();
    }

    @Test
    public void onSearchRepositories_onRepositoriesSuccess_withViewAdded_showsResultsTitle() throws InterruptedException {
        when(view.isAdded()).thenReturn(true);

        presenter.onSearchRepositories("wrt");
        sleep(THREAD_SLEEP_MILLIS);

        verify(view).showResultsTitle("wrt", "best match");
    }

    @Test
    public void onSearchRepositories_onRepositoriesSuccess_withViewAdded_showsRepositories() throws InterruptedException {
        when(view.isAdded()).thenReturn(true);

        presenter.onSearchRepositories("wrt");
        sleep(THREAD_SLEEP_MILLIS);

        verify(view).showRepositories(testRepositories);
    }

    @Test
    public void onSearchRepositories_onRepositoriesSuccess_withViewNotAdded_doesNothing() throws InterruptedException {
        when(view.isAdded()).thenReturn(false);

        presenter.onSearchRepositories("wrt");
        sleep(THREAD_SLEEP_MILLIS);

        verify(view, never()).hideLoadingMore();
        verify(view, never()).hideProgress();
        verify(view, never()).showResultsTitle(anyString(), anyString());
        verify(view, never()).showRepositories(ArgumentMatchers.<Repository>anyList());
    }

    @Test
    public void onSearchRepositories_onRepositoriesError_withViewAdded_hidesProgress() throws InterruptedException {
        when(view.isAdded()).thenReturn(true);
        when(gitHubService.searchRepositories(anyString(), any(Sorting.class), any(Ordering.class), anyInt(), anyInt()))
                .thenReturn(Single.<RepositoriesResponse>error(new Throwable("")));

        presenter.onSearchRepositories("wrt");
        sleep(THREAD_SLEEP_MILLIS);

        verify(view).hideProgress();
    }

    @Test
    public void onSearchRepositories_onRepositoriesError_withViewAdded_hidesLoadingMore() throws InterruptedException {
        when(view.isAdded()).thenReturn(true);
        when(gitHubService.searchRepositories(anyString(), any(Sorting.class), any(Ordering.class), anyInt(), anyInt()))
                .thenReturn(Single.<RepositoriesResponse>error(new Throwable("")));

        presenter.onSearchRepositories("wrt");
        sleep(THREAD_SLEEP_MILLIS);

        verify(view).hideLoadingMore();
    }

    @Test
    public void onSearchRepositories_onRepositoriesError_withViewAdded_showsError() throws InterruptedException {
        when(view.isAdded()).thenReturn(true);
        when(gitHubService.searchRepositories(anyString(), any(Sorting.class), any(Ordering.class), anyInt(), anyInt()))
                .thenReturn(Single.<RepositoriesResponse>error(new Throwable("")));

        presenter.onSearchRepositories("wrt");
        sleep(THREAD_SLEEP_MILLIS);

        verify(view).showError(presenter);
    }

    @Test
    public void onSearchRepositories_onRepositoriesError_withViewNotAdded_doesNothing() throws InterruptedException {
        when(view.isAdded()).thenReturn(false);
        when(gitHubService.searchRepositories(anyString(), any(Sorting.class), any(Ordering.class), anyInt(), anyInt()))
                .thenReturn(Single.<RepositoriesResponse>error(new Throwable("")));

        presenter.onSearchRepositories("wrt");
        sleep(THREAD_SLEEP_MILLIS);

        verify(view, never()).hideLoadingMore();
        verify(view, never()).hideProgress();
        verify(view, never()).showError(any(SearchPresenter.class));
    }

    @Test
    public void onSortByBestMatch_callsGitHubServiceWithNoSortingNoOrdering() {
        presenter.onSearchRepositories("yeey");

        verify(gitHubService).searchRepositories("yeey", Sorting.NONE, Ordering.NONE, 1, 50);
    }

    @Test
    public void onSortByStarsDesc_callsGitHubServiceWithSortingStarsOrderingDesc() {
        presenter.onSearchRepositories("yeey");

        presenter.onSortByStarsDesc();

        verify(gitHubService).searchRepositories("yeey", Sorting.STARS, Ordering.DESC, 1, 50);
    }

    @Test
    public void onSortByStarsAsc_callsGitHubServiceWithSortingStarsOrderingAsc() {
        presenter.onSearchRepositories("yeey");

        presenter.onSortByStarsAsc();

        verify(gitHubService).searchRepositories("yeey", Sorting.STARS, Ordering.ASC, 1, 50);
    }

    @Test
    public void onSortByForksDesc_callsGitHubServiceWithSortingForksOrderingDesc() {
        presenter.onSearchRepositories("yeey");

        presenter.onSortByForksDesc();

        verify(gitHubService).searchRepositories("yeey", Sorting.FORKS, Ordering.DESC, 1, 50);
    }

    @Test
    public void onSortByForksAsc_callsGitHubServiceWithSortingForksOrderingAsc() {
        presenter.onSearchRepositories("yeey");

        presenter.onSortByForksAsc();

        verify(gitHubService).searchRepositories("yeey", Sorting.FORKS, Ordering.ASC, 1, 50);
    }

    @Test
    public void onSortByStarsDesc_callsGitHubServiceWithSortingUpdatedOrderingDesc() {
        presenter.onSearchRepositories("yeey");

        presenter.onSortByUpdatedDesc();

        verify(gitHubService).searchRepositories("yeey", Sorting.UPDATED, Ordering.DESC, 1, 50);
    }

    @Test
    public void onSortByStarsAsc_callsGitHubServiceWithSortingUpdatedOrderingAsc() {
        presenter.onSearchRepositories("yeey");

        presenter.onSortByUpdatedAsc();

        verify(gitHubService).searchRepositories("yeey", Sorting.UPDATED, Ordering.ASC, 1, 50);
    }

    @Test
    public void onLoadNextPage_shouldLoadMore_showsLoadingMore() throws InterruptedException {
        presenter.onSearchRepositories("something");
        sleep(THREAD_SLEEP_MILLIS);
        reset(view);

        presenter.onLoadNextPage();

        verify(view).showLoadingMore();
    }

    @Test
    public void onLoadNextPage_shouldLoadMore_callsGitHubServiceWithCorrectPage() throws InterruptedException {
        presenter.onSearchRepositories("something");
        sleep(THREAD_SLEEP_MILLIS);
        reset(gitHubService);
        when(gitHubService.searchRepositories(anyString(), any(Sorting.class), any(Ordering.class), anyInt(), anyInt()))
                .thenReturn(Single.just(testResponse));

        presenter.onLoadNextPage();

        verify(gitHubService).searchRepositories("something", Sorting.NONE, Ordering.NONE, 2, 50);

        presenter.onLoadNextPage();

        verify(gitHubService).searchRepositories("something", Sorting.NONE, Ordering.NONE, 3, 50);
    }

    @Test
    public void onLoadNextPage_shouldNotLoadMore_doesNothing() throws InterruptedException {
        when(testResponse.getTotalCount()).thenReturn(2);
        presenter.onSearchRepositories("something");
        sleep(THREAD_SLEEP_MILLIS);
        reset(view);
        reset(gitHubService);
        when(gitHubService.searchRepositories(anyString(), any(Sorting.class), any(Ordering.class), anyInt(), anyInt()))
                .thenReturn(Single.just(testResponse));

        presenter.onLoadNextPage();

        verify(view, never()).showLoadingMore();
        verify(gitHubService, never()).searchRepositories(anyString(), any(Sorting.class), any(Ordering.class), anyInt(), anyInt());
    }

    @Test
    public void onErrorRefreshClicked_withSearchQueryAndSortByAndPage_repeatsCallToGitHubService() throws InterruptedException {
        presenter.onSearchRepositories("smhw");
        sleep(THREAD_SLEEP_MILLIS);
        presenter.onSortByForksAsc();
        sleep(THREAD_SLEEP_MILLIS);
        presenter.onLoadNextPage();
        sleep(THREAD_SLEEP_MILLIS);

        presenter.onErrorRefreshClicked();

        verify(gitHubService, times(2)).searchRepositories("smhw", Sorting.FORKS, Ordering.ASC, 2, 50);
    }

    private static RepositoriesResponse getTestResponse() {
        final com.evastos.githubrepos.data.model.response.Repository repository1 =
                mock(com.evastos.githubrepos.data.model.response.Repository.class);
        when(repository1.getName()).thenReturn("RepositoryName1");
        final User owner1 = mock(User.class);
        when(owner1.getLogin()).thenReturn("OwnerName1");
        when(owner1.getAvatarUrl()).thenReturn("AvatarUrl1");
        when(repository1.getOwner()).thenReturn(owner1);
        when(repository1.getWatchersCount()).thenReturn(23);
        when(repository1.getForksCount()).thenReturn(5767);
        when(repository1.getOpenIssuesCount()).thenReturn(9000);

        final com.evastos.githubrepos.data.model.response.Repository repository2 =
                mock(com.evastos.githubrepos.data.model.response.Repository.class);
        when(repository2.getName()).thenReturn("RepositoryName2");
        final User owner2 = mock(User.class);
        when(owner2.getLogin()).thenReturn("OwnerName2");
        when(owner2.getAvatarUrl()).thenReturn("AvatarUrl2");
        when(repository2.getOwner()).thenReturn(owner2);
        when(repository2.getWatchersCount()).thenReturn(656335);
        when(repository2.getForksCount()).thenReturn(111);
        when(repository2.getOpenIssuesCount()).thenReturn(80533);

        final RepositoriesResponse response = mock(RepositoriesResponse.class);
        when(response.getTotalCount()).thenReturn(120);
        when(response.getItems()).thenReturn(Arrays.asList(repository1, repository2));
        return response;
    }

    private ArrayList<Repository> getTestRepositories() {
        final ArrayList<Repository> repositories = new ArrayList<>();
        for (com.evastos.githubrepos.data.model.response.Repository item : testResponse.getItems()) {
            repositories.add(new Repository(item));
        }
        return repositories;
    }
}