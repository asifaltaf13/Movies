package com.asifaltaf.movies.ui.details

import com.asifaltaf.movies.domain.model.MovieEntity

data class DetailsState(
    val movie: MovieEntity = MovieEntity(title = "", imdbID = "", posterUrl = "", type = "", year = "")
)
