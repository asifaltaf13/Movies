package com.asifaltaf.movies.ui.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asifaltaf.movies.data.MovieRepository
import com.asifaltaf.movies.domain.model.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(DetailsState())
    val state: State<DetailsState> = _state

    init {
        val imdbID = savedStateHandle.get<String>("imdbID")
        if (!imdbID.isNullOrEmpty()) {
            viewModelScope.launch {
                val movie = repository.selectMovieByImdbID(imdbID)
                if (movie != null && movie.title.isNotBlank()) {
                    updateMovieState(movie)
                }
            }
        }
    }

    private fun updateMovieState(movie: MovieEntity) {
        _state.value = state.value.copy(movie = movie)
    }
}