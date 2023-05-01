package com.asifaltaf.movies.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asifaltaf.movies.data.MovieRepository
import com.asifaltaf.movies.domain.model.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private var selectAllMoviesJob: Job? = null

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private val _isLoading = mutableStateOf(false)
    val isLoading = _isLoading

    private val _isSearchExpanded = mutableStateOf(false)
    val isSearchExpanded = _isSearchExpanded

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage = _toastMessage.asStateFlow()

    init {
        showLoadedMovies()
    }

    fun changeSearchQuery(searchQuery: String) = updateSearchQueryState(searchQuery = searchQuery)

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    fun searchMovies() {
        resetPageState()
        resetMoviesState()
        loadMovies()
    }

    fun searchMoviesNextPage() {
        updatePageState(state.value.page + 1)
        loadMovies()
    }

    fun deleteMovies() {
        viewModelScope.launch { repository.deleteAllMovies() }
        resetState()
    }

    fun toggleSearchBar() {
        _isSearchExpanded.value = !isSearchExpanded.value
    }

    private fun loadMovies() {
        selectAllMoviesJob?.cancel()
        val searchQuery = state.value.searchQuery
        val page = state.value.page

        if (searchQuery.isEmpty()) {
            setToastMessage("Movie title can not be empty")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val searchResult = repository.searchMovies(
                searchQuery = searchQuery,
                page = page
            )
            searchResult.fold(
                onSuccess = { omdbSearch ->
                    addToMoviesState(omdbSearch.movies)
                    updateTotalResultsState(omdbSearch.totalResults.toInt())
                },
                onFailure = { throwable ->
                    setToastMessage(throwable.message)
                }
            )
            _isLoading.value = false
        }
    }

    fun showLoadedMovies() {
        var firstValueReceived = false
        selectAllMoviesJob?.cancel()
        resetMoviesState()
        selectAllMoviesJob = viewModelScope.launch {
            repository.selectAllMoviesFlow()
                .filter { it.isNotEmpty() }
                .collectLatest { movies ->
                    run {
                        if(!firstValueReceived){
                            firstValueReceived = true
                            setToastMessage("Showing loaded movies.")
                        }
                        addToMoviesState(movies)
                        updateTotalResultsState(movies.size)
                    }
                }
        }
    }

    private fun resetState() {
        _state.value = state.value.copy(
            page = 1,
            movies = emptyList(),
            totalResults = 0,
            searchQuery = ""
        )
    }

    private fun updateSearchQueryState(searchQuery: String) {
        _state.value = state.value.copy(searchQuery = searchQuery)
    }

    private fun resetPageState() {
        _state.value = state.value.copy(page = 1)
    }

    private fun updatePageState(page: Int) {
        _state.value = state.value.copy(page = page)
    }

    private fun updateTotalResultsState(totalResults: Int) {
        _state.value = state.value.copy(totalResults = totalResults)
    }

    private fun resetMoviesState() {
        _state.value = state.value.copy(movies = emptyList())
    }

    private fun addToMoviesState(movies: List<MovieEntity>) {
        _state.value = state.value.copy(movies = state.value.movies + movies)
    }

    private fun setToastMessage(message: String?) {
        _toastMessage.value = message
    }
}