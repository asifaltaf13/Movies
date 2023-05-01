package com.asifaltaf.movies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asifaltaf.movies.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    private var loadMoviesJob: Job? = null

    val moviesFlow = repository.selectAllMoviesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchState = MutableStateFlow(SearchState())
    private val _searchQuery = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _isSearchExpanded = MutableStateFlow(false)
    private val _toastMessage: MutableStateFlow<String?> = MutableStateFlow(null)

    val searchState = _searchState.asStateFlow()
    val searchQuery = _searchQuery.asStateFlow()
    val isLoading = _isLoading.asStateFlow()
    val isSearchExpanded = _isSearchExpanded.asStateFlow()
    val toastMessage = _toastMessage.asStateFlow()

    fun changeSearchQuery(value: String) {
        _searchQuery.value = value
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    fun deleteMovies() {
        viewModelScope.launch { repository.deleteAllMovies() }
    }

    fun searchMovies() {
        deleteMovies()
        updateSearchState(page = 1, totalResults = 0)
        loadMovies()
    }

    fun searchMoviesNextPage() {
        updateSearchState(page = searchState.value.page + 1)
        loadMovies()
    }

    fun toggleSearchBar() {
        _isSearchExpanded.value = !isSearchExpanded.value
    }

    private fun loadMovies() {
        cancelPreviousLoadingJob()

        if (searchQuery.value.isEmpty()) {
            setToastMessage("Movie title can not be empty")
            return
        }

        loadMoviesJob = viewModelScope.launch {
            _isLoading.value = true

            val searchResult = repository.loadMovies(
                searchQuery = searchQuery.value,
                page = searchState.value.page
            )
            searchResult.fold(
                onSuccess = { omdbSearch -> updateSearchState(totalResults = omdbSearch.totalResults.toInt()) },
                onFailure = { throwable -> setToastMessage(throwable.message) }
            )

            _isLoading.value = false
        }
    }

    private fun cancelPreviousLoadingJob() {
        loadMoviesJob?.cancel()
    }

    private fun setToastMessage(message: String?) {
        _toastMessage.value = message
    }

    private fun updateSearchState(
        page: Int = searchState.value.page,
        totalResults: Int = searchState.value.totalResults
    ) {
        _searchState.value = searchState.value.copy(
            page = page,
            totalResults = totalResults
        )
    }
}