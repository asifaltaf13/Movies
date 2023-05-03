package com.asifaltaf.movies.domain

import com.asifaltaf.movies.domain.model.MovieDetailEntity
import com.asifaltaf.movies.domain.model.MovieEntity
import com.asifaltaf.movies.domain.model.OmdbSearch
import kotlinx.coroutines.flow.Flow

interface MovieRepositoryAbstract {

    // SELECT from database
    fun selectAllMoviesFlow(): Flow<List<MovieEntity>>
    suspend fun selectMovieByImdbID(imdbID: String): MovieEntity?
    suspend fun selectMovieDetailByImdbID(imdbID: String): MovieDetailEntity?
    suspend fun deleteAllMovies()
    // GET from api
    suspend fun loadMovies(searchQuery: String, page: Int): Result<OmdbSearch>
    suspend fun loadMovieDetail(imdbID: String): Result<MovieDetailEntity>
}