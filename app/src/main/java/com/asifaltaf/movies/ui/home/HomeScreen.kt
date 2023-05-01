package com.asifaltaf.movies.ui.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asifaltaf.movies.R
import com.asifaltaf.movies.ui.home.components.MainAppBar
import com.asifaltaf.movies.ui.util.Screen
import com.asifaltaf.movies.ui.util.Tags
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen(
    navController: NavController, viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState = viewModel.state.value
    val isLoading = viewModel.isLoading.value
    val toastMessage by viewModel.toastMessage.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(MaterialTheme.colorScheme.surface)

    val gridState = rememberLazyStaggeredGridState()
    val isScrolled = gridState.firstVisibleItemScrollOffset > 0

    Scaffold(
        topBar = {
            Column {
                MainAppBar(
                    searchQuery = uiState.searchQuery,
                    isSearchExpanded = viewModel.isSearchExpanded.value,
                    toggleShowDialog = { showDialog = !showDialog },
                    toggleSearchBar = viewModel::toggleSearchBar,
                    changeSearchQuery = viewModel::changeSearchQuery,
                    searchMovies = viewModel::searchMovies,
                )
                if (isScrolled && !viewModel.isSearchExpanded.value && !uiState.movies.isEmpty())
                    Divider(color = MaterialTheme.colorScheme.tertiary)
            }
        },
    ) {
        if (uiState.movies.isNotEmpty()) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 8.dp),
                columns = StaggeredGridCells.Adaptive(160.dp),
                state = gridState
            ) {
                itemsIndexed(uiState.movies) { _, movie ->
                    MovieCard(modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                        movie = movie,
                        onCardClick = {
                            navController.navigate(
                                Screen.DetailsScreen.route + "?imdbID=${movie.imdbID}"
                            )
                        })
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        modifier = Modifier.fillMaxWidth(), text = stringResource(
                            id = R.string.showing_entries,
                            uiState.movies.count(),
                            uiState.totalResults
                        ), textAlign = TextAlign.Center
                    )
                }
                if (uiState.movies.isNotEmpty() && uiState.totalResults > uiState.movies.count()) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Button(onClick = { viewModel.searchMoviesNextPage() }) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.load_more_entries),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.no_results),
                    contentDescription = stringResource(R.string.no_results)
                )
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (showDialog) {
        AlertDialog(
            modifier = Modifier.testTag(Tags.AlertDialog),
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(R.string.delete_all_loaded_movies)) },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteMovies()
                    showDialog = false
                    Toast.makeText(
                        context,
                        context.getString(R.string.all_loaded_movies_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text(text = stringResource(R.string.yes))
                }
            })
    }

    if (!toastMessage.isNullOrEmpty()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.clearToastMessage()
    }
}