package com.asifaltaf.movies.ui.details

import com.asifaltaf.movies.domain.model.MovieDetailEntity

data class DetailsState(
    val movieDetail: MovieDetailEntity = MovieDetailEntity("", error = "False")
)
