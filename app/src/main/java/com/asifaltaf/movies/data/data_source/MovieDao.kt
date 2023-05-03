package com.asifaltaf.movies.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.asifaltaf.movies.domain.model.MovieDetailEntity
import com.asifaltaf.movies.domain.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao { // dao: data access object

    // movies
    @Query("SELECT * FROM movie")
    fun selectAllMoviesFlow(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE imdbID = :imdbID")
    suspend fun selectMovieByImdbID(imdbID: String): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM movie")
    suspend fun deleteAllMovies()


    // movie details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetail: MovieDetailEntity)

    @Query("SELECT * FROM movie_detail WHERE imdbID = :imdbID")
    suspend fun selectMovieDetailByImdbID(imdbID: String): MovieDetailEntity?

    @Query("DELETE FROM movie_detail")
    suspend fun deleteAllMovieDetails()
}