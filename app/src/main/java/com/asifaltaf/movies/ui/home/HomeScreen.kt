@file:OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)

package com.asifaltaf.movies.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.asifaltaf.movies.R
import com.asifaltaf.movies.domain.model.MovieEntity
import com.asifaltaf.movies.ui.home.components.DeleteAllAlertDialog
import com.asifaltaf.movies.ui.home.components.MainAppBar
import com.asifaltaf.movies.ui.home.components.MovieCard
import com.asifaltaf.movies.ui.home.components.NoResults
import com.asifaltaf.movies.ui.util.Screen

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val searchState by viewModel.searchState.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val isSearchBarVisible by viewModel.isSearchBarVisible.collectAsState()
    val movies by viewModel.moviesFlow.collectAsState()

    val gridState = rememberLazyStaggeredGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    gridState.firstVisibleItemScrollOffset > 0 || gridState.firstVisibleItemIndex > 0

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MainAppBar(
                isSearchBarVisible = isSearchBarVisible,
                toggleShowDialog = viewModel::toggleShowDialog,
                showSearchBar = viewModel::showSearchBar,
                hideSearchBar = viewModel::hideSearchBar,
                searchMovies = viewModel::searchMovies,
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        if (movies.isNotEmpty()) {
            AnimatedVisibility(
                visibleState = viewModel.gridVisibleState,
                enter = fadeIn() + slideInVertically(
                    animationSpec = spring(
                        stiffness = StiffnessVeryLow, dampingRatio = DampingRatioMediumBouncy
                    )
                )
            ) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.padding(horizontal = 16.dp).imePadding(),
                    columns = StaggeredGridCells.Adaptive(minSize = 180.dp),
                    state = gridState,
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    gridTopMargin(it)
                    showingLoadedResults(searchState)
                    showItems(movies, navController, viewModel)
                    loadMoreEntries(searchState, movies.count(), isLoading, viewModel)
                    showingEntries(searchState, movies.count(), searchState.totalResults)
                    gridBottomMargin()
                }
            }

        } else if (isLoading) {
            ShowLoading()
        } else {
            NoResults()
        }
    }

    ShowDialog(showDialog, context, viewModel)
    ShowToast(toastMessage, context, viewModel)
}

@Composable
private fun ShowToast(toastMessage: String?, context: Context, viewModel: HomeViewModel) {
    if (!toastMessage.isNullOrEmpty()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.resetToastMessage()
    }
}

@Composable
private fun ShowDialog(showDialog: Boolean, context: Context, viewModel: HomeViewModel) {
    if (showDialog) {
        DeleteAllAlertDialog(
            context = context,
            onConfirm = viewModel::deleteMovies,
            onDismiss = viewModel::toggleShowDialog
        )
    }
}

private fun LazyStaggeredGridScope.showItems(
    movies: List<MovieEntity>,
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    itemsIndexed(movies) { _, movie ->
        MovieCard(
            movie = movie,
            onCardClick = {
                navController.navigate(
                    route = Screen.DetailsScreen.route + "?imdbID=${movie.imdbID}"
                )
                viewModel.hideSearchBar()
            })
    }
}

private fun LazyStaggeredGridScope.gridBottomMargin() {
    item(span = StaggeredGridItemSpan.FullLine) {
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

private fun LazyStaggeredGridScope.showingEntries(
    searchState: SearchState,
    moviesCount: Int,
    totalResults: Int
) {
    if (searchState.totalResults > 0) {
        item(span = StaggeredGridItemSpan.FullLine) {
            ShowingEntries(moviesCount, totalResults)
        }
    }
}

private fun LazyStaggeredGridScope.loadMoreEntries(
    searchState: SearchState,
    moviesCount: Int,
    isLoading: Boolean,
    viewModel: HomeViewModel
) {
    if (searchState.totalResults > moviesCount) {
        item(span = StaggeredGridItemSpan.FullLine) {
            LoadMoreEntries(
                enabled = !isLoading,
                onClick = viewModel::searchMoviesNextPage
            )
        }
    }
}

private fun LazyStaggeredGridScope.gridTopMargin(paddingValues: PaddingValues) {
    item(span = StaggeredGridItemSpan.FullLine) {
        Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
    }
}

private fun LazyStaggeredGridScope.showingLoadedResults(
    searchState: SearchState
) {
    if (searchState.totalResults == 0) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.showing_loaded_results),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LoadMoreEntries(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.load_more_entries),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ShowingEntries(results: Int, totalResults: Int) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(
            id = R.string.showing_entries,
            results,
            totalResults
        ), textAlign = TextAlign.Center
    )
}

@Composable
private fun ShowLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}