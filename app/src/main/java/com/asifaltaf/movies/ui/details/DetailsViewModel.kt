package com.asifaltaf.movies.ui.details

import android.util.Log
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
    private val repository: MovieRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailState = MutableStateFlow(DetailsState())
    val detailState = _detailState.asStateFlow()

    private val _toastMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(true)

    val toastMessage = _toastMessage.asStateFlow()
    val isLoading = _isLoading.asStateFlow()

    init {
        val imdbID = savedStateHandle.get<String>("imdbID")
        imdbID?.let { showMovieDetail(imdbID = imdbID) }
    }

    private fun showMovieDetail(imdbID: String) {
        if (imdbID.isNotEmpty()) {
            viewModelScope.launch {
                showLocalMovieDetail(imdbID = imdbID)
                if (isLoading()) {
                    showLocalMoviePartial(imdbID = imdbID)
                    loadRemoteMovieDetailAndShow(imdbID = imdbID)
                }
            }
        }
    }

    private suspend fun showLocalMovieDetail(imdbID: String) {
        repository.selectMovieDetailByImdbID(imdbID = imdbID)?.let {
            updateMovieState(it)
            loaded()
            Log.d("DetailsViewModel", "showing details locally")
        }
    }

    private suspend fun showLocalMoviePartial(imdbID: String) {
        repository.selectMovieByImdbID(imdbID = imdbID)?.let {
            updateMovieState(MovieDetailEntity.fromMovie(it))
            Log.d("DetailsViewModel", "showing partial movie locally")
        }
    }

    private suspend fun loadRemoteMovieDetailAndShow(imdbID: String) {
        val searchResult = repository.loadMovieDetail(imdbID = imdbID)
        Log.d("DetailsViewModel", "loading details remotely")
        searchResult.fold(
            onSuccess = {
                //show loaded details
                repository.selectMovieDetailByImdbID(imdbID = imdbID)?.let {
                    updateMovieState(it)
                    loaded()
                    Log.d("DetailsViewModel", "showing details locally")
                }
            },
            onFailure = { throwable -> setToastMessage(throwable.message) }
        )
    }

    private fun isLoading(): Boolean {
        return isLoading.value
    }

    private fun loaded() {
        _isLoading.value = false
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