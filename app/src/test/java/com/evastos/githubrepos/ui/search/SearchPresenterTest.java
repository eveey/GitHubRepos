/*
 * Copyright (c) 2017 Eva Štos
 * All rights reserved.
 */

package com.evastos.githubrepos.ui.search;

import android.os.Bundle;
import android.os.Parcelable;

import com.evastos.githubrepos.data.model.request.Ordering;
import com.evastos.githubrepos.data.model.request.Sorting;
import com.evastos.githubrepos.data.model.response.RepositoriesResponse;
import com.evastos.githubrepos.data.model.response.User;
import com.evastos.githubrepos.data.service.GitHubService;
import com.evastos.githubrepos.ui.model.Repository;
import com.evastos.githubrepos.ui.model.SortBy;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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

    private final List<Repository> testRepositories = getTestRepositories();

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

        /*

        void onStart();

        void onStop();

        void onSaveState(@NonNull Bundle bundle);

        void onRestoreState(@Nullable Bundle bundle);

        void onSearchRepositories(@NonNull String searchQuery);

        void onSortByBestMatch();

        void onSortByStarsDesc();

        void onSortByStarsAsc();

        void onSortByForksDesc();

        void onSortByForksAsc();

        void onSortByUpdatedDesc();

        void onSortByUpdatedAsc();

        void onLoadNextPage();

        boolean isLoading();

        void onRepositoryClick(@NonNull Repository repository);
     */

    @Test
    public void onStart_withSavedStateNull_doesNothing() {
        final Bundle savedState = null;

        presenter.onStart(null);

        verify(view, never()).showRepositories(ArgumentMatchers.<Repository>anyList());
        verify(view, never()).showResultsTitle(anyString(), anyString());
    }

    @Test
    public void onStart_withSavedState_showsRepositories() {
        final Bundle savedState = new Bundle();
        savedState.putParcelableArrayList("repositories", (ArrayList<? extends Parcelable>) testRepositories);
        savedState.putString("searchQuery", "qwerty");
        savedState.putSerializable("sortBy", SortBy.STARS_ASC);
        savedState.putInt("page", 5);

        presenter.onStart(null);

        verify(view).showRepositories(testRepositories);
    }

    @Test
    public void onStart_withSavedState_showsResultsTitle() {
        final Bundle savedState = new Bundle();
        savedState.putParcelableArrayList("repositories", (ArrayList<? extends Parcelable>) testRepositories);
        savedState.putString("searchQuery", "qwerty");
        savedState.putSerializable("sortBy", SortBy.UPDATED_DESC);
        savedState.putInt("page", 5);

        presenter.onStart(null);

        verify(view).showResultsTitle("qwerty", "updated desc");
    }

    /*
        @Override
    public void onStart(@Nullable Bundle savedState) {
        if (savedState == null) {
            return;
        }
        if (savedState.containsKey(BUNDLE_STATE_SORT_BY)) {
            final SortBy sortBy = (SortBy) savedState.getSerializable(BUNDLE_STATE_SORT_BY);
            if (sortBy != null) {
                this.sortBy = sortBy;
            }
        }
        page = savedState.getInt(BUNDLE_STATE_PAGE, FIRST_PAGE);
        searchQuery = savedState.getString(BUNDLE_STATE_SEARCH_QUERY, null);
        if (savedState.containsKey(BUNDLE_STATE_REPOSITORIES)) {
            final List<Repository> repositories = savedState.getParcelableArrayList(BUNDLE_STATE_REPOSITORIES);
            if (repositories != null) {
                this.repositories = repositories;
            }
        }
        if (searchQuery != null) {
            view.showRepositories(repositories);
            view.showResultsTitle(searchQuery, sortBy.toString());
        }
    }
     */

//    @Test
//    public void onStart_viewIsAdded_onExchangeRatesSuccess_showsExchangeRates() throws InterruptedException {
//        Single<List<ExchangeRate>> response = Single.just(testExchangeRates);
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//        when(view.isAdded()).thenReturn(true);
//
//        presenter.onStart();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view).showExchangeRates(testExchangeRates);
//    }
//
//    @Test
//    public void onStart_viewIsAdded_onExchangeRatesSuccess_showsTodaysDate() throws InterruptedException {
//        Single<List<ExchangeRate>> response = Single.just(testExchangeRates);
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//        when(view.isAdded()).thenReturn(true);
//
//        presenter.onStart();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view).showExchangeRatesDate("Jan 18, 1970");
//    }
//
//    @Test
//    public void onStart_onExchangeRatesSuccess_putsExchangeRatesToCache() throws InterruptedException {
//        Single<List<ExchangeRate>> response = Single.just(testExchangeRates);
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//
//        presenter.onStart();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(exchangeRateManager).putExchangeRates(testExchangeRates);
//    }
//
//    @Test
//    public void onStart_showsProgress() {
//        Single<List<ExchangeRate>> response = Single.just(Collections.<ExchangeRate>emptyList());
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//
//        presenter.onStart();
//
//        verify(view).showProgress();
//    }
//
//    @Test
//    public void onStart_viewIsAdded_onExchangeRatesSuccess_hidesProgress() throws InterruptedException {
//        Single<List<ExchangeRate>> response = Single.just(testExchangeRates);
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//        when(view.isAdded()).thenReturn(true);
//
//        presenter.onStart();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view).hideProgress();
//    }
//
//    @Test
//    public void onStart_viewIsNotAdded_doesNothing() throws InterruptedException {
//        Single<List<ExchangeRate>> response = Single.just(testExchangeRates);
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//        when(view.isAdded()).thenReturn(false);
//
//        presenter.onStart();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view, never()).hideProgress();
//        verify(view, never()).showExchangeRates(ArgumentMatchers.<ExchangeRate>anyList());
//        verify(view, never()).showExchangeRatesDate(anyString());
//    }
//
//    @Test
//    public void onStart_viewIsAdded_onExchangeRatesError_hidesProgress() throws InterruptedException {
//        Single<List<ExchangeRate>> errorResponse = Single.error(new Throwable(""));
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(errorResponse);
//        when(view.isAdded()).thenReturn(true);
//
//        presenter.onStart();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view).hideProgress();
//    }
//
//    @Test
//    public void onStart_viewIsAdded_onExchangeRatesError_showsError() throws InterruptedException {
//        Single<List<ExchangeRate>> errorResponse = Single.error(new Throwable(""));
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(errorResponse);
//        when(view.isAdded()).thenReturn(true);
//
//        presenter.onStart();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view).showError(presenter);
//    }
//
//    @Test
//    public void onStart_viewIsNotAdded_onExchangeRatesError_doesNothing() throws InterruptedException {
//        Single<List<ExchangeRate>> errorResponse = Single.error(new Throwable(""));
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(errorResponse);
//        when(view.isAdded()).thenReturn(false);
//
//        presenter.onStart();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view, never()).hideProgress();
//        verify(view, never()).showError(presenter);
//    }
//
//    @Test
//    public void onErrorRefreshClicked_viewIsAdded_onExchangeRatesSuccess_showsExchangeRates() throws InterruptedException {
//        Single<List<ExchangeRate>> response = Single.just(testExchangeRates);
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//        when(view.isAdded()).thenReturn(true);
//
//        presenter.onErrorRefreshClicked();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view).showExchangeRates(testExchangeRates);
//    }
//
//    @Test
//    public void onErrorRefreshClicked_viewIsAdded_onExchangeRatesSuccess_showsTodaysDate() throws InterruptedException {
//        Single<List<ExchangeRate>> response = Single.just(testExchangeRates);
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//        when(view.isAdded()).thenReturn(true);
//
//        presenter.onErrorRefreshClicked();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view).showExchangeRatesDate("Jan 18, 1970");
//    }
//
//    @Test
//    public void onErrorRefreshClicked_onExchangeRatesSuccess_putsExchangeRatesToCache() throws InterruptedException {
//        Single<List<ExchangeRate>> response = Single.just(testExchangeRates);
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//
//        presenter.onErrorRefreshClicked();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(exchangeRateManager).putExchangeRates(testExchangeRates);
//    }
//
//    @Test
//    public void onErrorRefreshClicked_showsProgress() {
//        Single<List<ExchangeRate>> response = Single.just(Collections.<ExchangeRate>emptyList());
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//
//        presenter.onErrorRefreshClicked();
//
//        verify(view).showProgress();
//    }
//
//    @Test
//    public void onErrorRefreshClicked_viewIsAdded_onExchangeRatesSuccess_hidesProgress() throws InterruptedException {
//        Single<List<ExchangeRate>> response = Single.just(testExchangeRates);
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//        when(view.isAdded()).thenReturn(true);
//
//        presenter.onErrorRefreshClicked();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view).hideProgress();
//    }
//
//    @Test
//    public void onErrorRefreshClicked_viewIsNotAdded_doesNothing() throws InterruptedException {
//        Single<List<ExchangeRate>> response = Single.just(testExchangeRates);
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(response);
//        when(view.isAdded()).thenReturn(false);
//
//        presenter.onErrorRefreshClicked();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view, never()).hideProgress();
//        verify(view, never()).showExchangeRates(ArgumentMatchers.<ExchangeRate>anyList());
//        verify(view, never()).showExchangeRatesDate(anyString());
//    }
//
//    @Test
//    public void onErrorRefreshClicked_viewIsAdded_onExchangeRatesError_hidesProgress() throws InterruptedException {
//        Single<List<ExchangeRate>> errorResponse = Single.error(new Throwable(""));
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(errorResponse);
//        when(view.isAdded()).thenReturn(true);
//
//        presenter.onErrorRefreshClicked();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view).hideProgress();
//    }
//
//    @Test
//    public void onErrorRefreshClicked_viewIsAdded_onExchangeRatesError_showsError() throws InterruptedException {
//        Single<List<ExchangeRate>> errorResponse = Single.error(new Throwable(""));
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(errorResponse);
//        when(view.isAdded()).thenReturn(true);
//
//        presenter.onErrorRefreshClicked();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view).showError(presenter);
//    }
//
//    @Test
//    public void onErrorRefreshClicked_viewIsNotAdded_onExchangeRatesError_doesNothing() throws InterruptedException {
//        Single<List<ExchangeRate>> errorResponse = Single.error(new Throwable(""));
//        when(exchangeRateManager.getDailyRates(any(CacheOption.class))).thenReturn(errorResponse);
//        when(view.isAdded()).thenReturn(false);
//
//        presenter.onErrorRefreshClicked();
//        sleep(THREAD_SLEEP_MILLIS);
//
//        verify(view, never()).hideProgress();
//        verify(view, never()).showError(presenter);
//    }

    private static RepositoriesResponse getTestResponse() {
        final com.evastos.githubrepos.data.model.response.Repository repository1 =
                mock(com.evastos.githubrepos.data.model.response.Repository .class);
        when(repository1.getName()).thenReturn("RepositoryName1");
        final User owner1 = mock(User.class);
        when(owner1.getLogin()).thenReturn("OwnerName1");
        when(owner1.getAvatarUrl()).thenReturn("AvatarUrl1");
        when(repository1.getOwner()).thenReturn(owner1);
        when(repository1.getWatchersCount()).thenReturn(23);
        when(repository1.getForksCount()).thenReturn(5767);
        when(repository1.getOpenIssuesCount()).thenReturn(9000);

        final com.evastos.githubrepos.data.model.response.Repository repository2 =
                mock(com.evastos.githubrepos.data.model.response.Repository .class);
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

    private List<Repository> getTestRepositories() {
        final List<Repository> repositories = new ArrayList<>();
        for (com.evastos.githubrepos.data.model.response.Repository item : testResponse.getItems()) {
            repositories.add(new Repository(item));
        }
        return repositories;
    }
}