package com.asifaltaf.movies.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.asifaltaf.movies.ui.home.HomeViewModel
import com.asifaltaf.movies.ui.theme.Phosphate
import com.asifaltaf.movies.ui.util.Tags
import com.asifaltaf.movies.R

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun MainAppBar(
    isSearchBarVisible: Boolean,
    toggleShowDialog: () -> Unit,
    showSearchBar: () -> Unit,
    hideSearchBar: () -> Unit,
    searchMovies: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        colors = mainAppBarColors(),
        title = {
            if (!isSearchBarVisible) {
                ShowTitle()
            }
        },
        navigationIcon = {
            if (!isSearchBarVisible) {
                ShowDeleteAction(toggleShowDialog)
            }
        },
        actions = {
            if (isSearchBarVisible) {
                ShowSearchBar(viewModel, hideSearchBar, searchMovies)
            } else {
                ShowSearchAction(showSearchBar)
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
private fun ShowSearchBar(
    viewModel: HomeViewModel,
    hideSearchBar: () -> Unit,
    searchMovies: () -> Unit
) {
    val textFieldEntry by viewModel.textFieldEntry.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }
    // LaunchedEffect prevents endless focus requests upon each recomposition
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        modifier = Modifier
            .testTag(Tags.SearchAppBarText)
            .fillMaxWidth()
            .focusRequester(focusRequester),
        shape = MaterialTheme.shapes.extraLarge,
        value = textFieldEntry,
        onValueChange = {
            viewModel.setTextFieldEntry(it)
        },
        label = { Text(text = stringResource(R.string.omdb_search)) },
        placeholder = { Text(text = stringResource(R.string.movie_title_placeholder)) },
        leadingIcon = {
            IconButton(
                modifier = Modifier.testTag(Tags.SearchAppBarClose),
                onClick = hideSearchBar
            ) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.search_app_bar_close),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            IconButton(
                modifier = Modifier.testTag(Tags.SearchAppBarSearch),
                onClick = {
                    searchMovies()
                }) {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
            searchMovies()
        })
    )
}

@Composable
fun ShowTitle() {
    Text(
        text = stringResource(R.string.app_name),
        color = MaterialTheme.colorScheme.primary,
        fontFamily = Phosphate
    )
}

@Composable
fun ShowDeleteAction(
    toggleShowDialog: () -> Unit
) {
    IconButton(
        modifier = Modifier.testTag(Tags.AppBarDelete),
        onClick = toggleShowDialog
    ) {
        Icon(
            Icons.Outlined.Delete,
            contentDescription = stringResource(R.string.delete_all_loaded_movies),
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun ShowSearchAction(
    showSearchBar: () -> Unit
) {
    IconButton(
        modifier = Modifier.testTag(Tags.AppBarSearch),
        onClick = showSearchBar
    ) {
        Icon(
            Icons.Outlined.Search,
            contentDescription = stringResource(R.string.search),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun mainAppBarColors() = TopAppBarDefaults.smallTopAppBarColors(
    containerColor = MaterialTheme.colorScheme.surface.copy(0.95f)
)
