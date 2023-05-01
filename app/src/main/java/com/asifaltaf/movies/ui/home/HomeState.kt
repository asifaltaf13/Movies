package com.asifaltaf.movies.ui.home

import com.asifaltaf.movies.domain.model.MovieEntity

data class HomeState(
    val movies: List<MovieEntity> = emptyList(),
    val page: Int = 1,
    val totalResults: Int = 0,
    val searchQuery: String = "",
)
