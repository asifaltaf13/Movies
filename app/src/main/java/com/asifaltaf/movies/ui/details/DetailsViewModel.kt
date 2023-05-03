package com.asifaltaf.movies.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asifaltaf.movies.data.MovieRepository
import com.asifaltaf.movies.domain.model.MovieDetailEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailState = MutableStateFlow(DetailsState())
    val detailState = _detailState.asStateFlow()

    private val _toastMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(true)

    val toastMessage = _toastMessage.asStateFlow()
    val isLoading = _isLoading.asStateFlow()

    init {
        val imdbID = savedStateHandle.get<String>("imdbID")
        imdbID?.let { loadMovieDetail(imdbID = imdbID) }
    }

    private fun loadMovieDetail(imdbID: String) {
        if (imdbID.isNotEmpty()) {
            viewModelScope.launch {
                //show local movie
                repository.selectMovieByImdbID(imdbID = imdbID)?.let {
                    val movieDetail = MovieDetailEntity.fromMovie(it)
                    updateMovieState(movieDetail = movieDetail)
                }

                //load remote details
                val searchResult = repository.loadMovieDetail(imdbID = imdbID)
                searchResult.fold(
                    onSuccess = {
                        _isLoading.value = false
                    },
                    onFailure = { throwable ->
                        setToastMessage(throwable.message)
                    }
                )

                //show local details
                repository.selectMovieDetailByImdbID(imdbID = imdbID)?.let { updateMovieState(it) }
            }
        }
    }

    private fun updateMovieState(movieDetail: MovieDetailEntity) {
        _detailState.value = detailState.value.copy(movieDetail = movieDetail)
    }

    private fun setToastMessage(message: String?) {
        _toastMessage.value = message
    }

    fun resetToastMessage() {
        _toastMessage.value = null
    }
}