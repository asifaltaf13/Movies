package com.asifaltaf.movies.data

import com.asifaltaf.movies.data.data_source.MovieApi
import com.asifaltaf.movies.data.data_source.MovieDao
import com.asifaltaf.movies.domain.MovieRepositoryAbstract
import com.asifaltaf.movies.domain.model.MovieEntity
import com.asifaltaf.movies.domain.model.OmdbSearch
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val dao: MovieDao,
    private val api: MovieApi
) : MovieRepositoryAbstract {

    override fun selectAllMoviesFlow(): Flow<List<MovieEntity>> = dao.selectAllMoviesFlow()

    override suspend fun selectMovieByImdbID(imdbID: String): MovieEntity? =
        dao.selectMovieByImdbID(imdbID)

    override suspend fun deleteAllMovies() = dao.deleteAllMovies()

    // GET from api

    override suspend fun loadMovies(searchQuery: String, page: Int): Result<OmdbSearch> {
        var errorMessage = "An unexpected error occurred"

        return try {
            val retrofitResponse = api.searchMovies(searchQuery = searchQuery, page = page.toString())
            val omdbSearch = retrofitResponse.body()
            when {
                retrofitResponse.isSuccessful && omdbSearch != null && omdbSearch.response == "True" -> {
                    dao.insertMovies(omdbSearch.movies)
                    Result.success(omdbSearch)
                }
                else -> {
                    if (omdbSearch != null)
                        errorMessage = omdbSearch.error
                    Result.failure(Exception(errorMessage))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: errorMessage))
        }
    }
}