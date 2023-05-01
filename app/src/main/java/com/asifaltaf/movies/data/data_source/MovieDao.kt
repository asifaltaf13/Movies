package com.asifaltaf.movies.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.asifaltaf.movies.domain.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao { // dao: data access object

    @Query("SELECT * FROM movie ORDER BY title")
    fun selectAllMoviesFlow(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE imdbID = :imdbID")
    suspend fun selectMovieByImdbID(imdbID: String): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("DELETE FROM movie")
    suspend fun deleteAllMovies()
}