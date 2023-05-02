@file:OptIn(ExperimentalAnimationApi::class)

package com.asifaltaf.movies.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asifaltaf.movies.R
import com.asifaltaf.movies.ui.home.components.DeleteAllAlertDialog
import com.asifaltaf.movies.ui.home.components.MainAppBar
import com.asifaltaf.movies.ui.home.components.NoResults
import com.asifaltaf.movies.ui.util.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val searchState by viewModel.searchState.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val isSearchBarVisible by viewModel.isSearchBarVisible.collectAsState()
    val movies by viewModel.moviesFlow.collectAsState()

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(MaterialTheme.colorScheme.surface)

    val gridState = rememberLazyStaggeredGridState()
    val isScrolled = gridState.firstVisibleItemScrollOffset > 0

    Scaffold(
        topBar = {
            Column {
                MainAppBar(
                    isSearchBarVisible = isSearchBarVisible,
                    toggleShowDialog = viewModel::toggleShowDialog,
                    showSearchBar = viewModel::showSearchBar,
                    hideSearchBar = viewModel::hideSearchBar,
                    searchMovies = viewModel::searchMovies
                )

                if (isScrolled) Divider(color = MaterialTheme.colorScheme.tertiary)
            }
        },
    ) {
        val moviesPresent = movies.isNotEmpty()
        val moviesCount = movies.count()

        if (moviesPresent) {

            AnimatedVisibility(
                visibleState = viewModel.gridVisibleState,
                enter = fadeIn() +
                        slideInVertically(
                            animationSpec = spring(
                                stiffness = StiffnessVeryLow,
                                dampingRatio = DampingRatioMediumBouncy
                            )
                        )
            ) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .padding(it)
                        .padding(horizontal = 16.dp),
                    columns = StaggeredGridCells.Adaptive(160.dp),
                    state = gridState,
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {

                    if(searchState.totalResults == 0) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            ShowingLoadedResults()
                        }
                    }

                    itemsIndexed(movies) { _, movie ->
                        MovieCard(
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 4.dp),
                            movie = movie,
                            onCardClick = {
                                navController.navigate(
                                    Screen.DetailsScreen.route + "?imdbID=${movie.imdbID}"
                                )
                                viewModel.hideSearchBar()
                            })
                    }

                    if (searchState.totalResults > moviesCount) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            LoadMoreEntries(
                                enabled = !isLoading,
                                onClick = viewModel::searchMoviesNextPage
                            )
                        }
                    }

                    if (searchState.totalResults > 0) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            ShowingEntries(moviesCount, searchState.totalResults)
                        }
                    }
                }
            }

        } else if (!isLoading) {
            NoResults()
        }
    }

    if (isLoading) {
        ShowLoading()
    }

    if (showDialog) {
        DeleteAllAlertDialog(
            context = context,
            onConfirm = viewModel::deleteMovies,
            onDismiss = viewModel::toggleShowDialog
        )
    }

    if (!toastMessage.isNullOrEmpty()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.resetToastMessage()
    }
}

@Composable
fun LoadMoreEntries(enabled: Boolean, onClick: () -> Unit) {
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
fun ShowingLoadedResults() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.showing_loaded_results),
        textAlign = TextAlign.Center
    )
}

@Composable
fun ShowingEntries(results: Int, totalResults: Int) {
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
fun ShowLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Preview
@Composable
fun ShowLoadingPreview() {
    ShowLoading()
}