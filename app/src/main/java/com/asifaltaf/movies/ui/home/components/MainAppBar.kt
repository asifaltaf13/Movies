package com.asifaltaf.movies.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.asifaltaf.movies.R
import com.asifaltaf.movies.ui.theme.Phosphate
import com.asifaltaf.movies.ui.util.Tags

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainAppBar(
    searchQuery: String,
    isSearchExpanded: Boolean,
    toggleShowDialog: () -> Unit,
    toggleSearchBar: () -> Unit,
    changeSearchQuery: (searchQuery: String) -> Unit,
    searchMovies: () -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            if (!isSearchExpanded) {
                Text(
                    text = stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = Phosphate
                )
            }
        },
        actions = {
            if (!isSearchExpanded) {
                IconButton(
                    modifier = Modifier.testTag(Tags.AppBarSearch),
                    onClick = toggleSearchBar) {
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
                        contentDescription = stringResource(R.string.delete_all_records),
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
                        .focusRequester(focusRequester),
                    shape = RoundedCornerShape(64.dp),
                    value = searchQuery,
                    onValueChange = {
                        changeSearchQuery(it)
                    },
                    label = { Text(text = stringResource(R.string.omdb_search)) },
                    placeholder = { Text(text = stringResource(R.string.movie_title_placeholder)) },
                    leadingIcon = {
                        IconButton(
                            modifier = Modifier.testTag(Tags.SearchAppBarClose),
                            onClick = { toggleSearchBar() }) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = stringResource(R.string.clear_text),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier.testTag(Tags.SearchAppBarSearch),
                            onClick = { searchMovies() }) {
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