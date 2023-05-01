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

    // SELECT from database
    override fun selectAllMoviesFlow(): Flow<List<MovieEntity>> = dao.selectAllMoviesFlow()
    override suspend fun selectMovieByImdbID(imdbID: String): MovieEntity? = dao.selectMovieByImdbID(imdbID)
    override suspend fun deleteAllMovies() = dao.deleteAllMovies()

    // GET from api
    override suspend fun searchMovies(searchQuery: String, page: Int): Result<OmdbSearch> {
        var errorMessage = "An unexpected error occurred"
        return try {
            val response = api.searchMovies(searchQuery = searchQuery, page = page.toString())
            val omdbSearch = response.body()
            when {
                response.isSuccessful && omdbSearch != null && omdbSearch.response == "True" -> {
                    val movies = omdbSearch.movies
                    // update database
                    for (movie in movies)
                        dao.insertMovie(movie)
                    Result.success(omdbSearch)
                }
                else -> {
                    if(omdbSearch!=null)
                        errorMessage = omdbSearch.error
                    Result.failure(Exception(errorMessage))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: errorMessage))
        }
    }
}