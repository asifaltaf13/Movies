package com.asifaltaf.movies.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asifaltaf.movies.R
import com.asifaltaf.movies.ui.home.HomeViewModel
import com.asifaltaf.movies.ui.theme.Phosphate
import com.asifaltaf.movies.ui.util.Tags

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainAppBar(
    isSearchBarVisible: Boolean,
    toggleShowDialog: () -> Unit,
    showSearchBar: () -> Unit,
    hideSearchBar: () -> Unit,
    searchMovies: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val textFieldEntry by viewModel.textFieldEntry.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            if (!isSearchBarVisible) {
                Text(
                    text = stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = Phosphate
                )
            }
        },
        actions = {
            if (!isSearchBarVisible) {
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
                IconButton(
                    modifier = Modifier.testTag(Tags.AppBarDelete),
                    onClick = toggleShowDialog) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.delete_all_loaded_movies),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else {

                // LaunchedEffect prevents endless focus requests upon each recomposition
                LaunchedEffect(focusRequester) {
                    focusRequester.requestFocus()
                }

                OutlinedTextField(
                    modifier = Modifier
                        .testTag(Tags.SearchAppBarText)
                        .fillMaxWidth()
                        .padding(16.dp)
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
        }
    )
}