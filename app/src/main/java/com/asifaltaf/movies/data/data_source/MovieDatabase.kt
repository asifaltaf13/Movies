package com.asifaltaf.movies.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asifaltaf.movies.domain.model.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao

    companion object {
        const val DATABASE_NAME = "movies_db"
        const val MOVIE_TABLE_NAME = "movie"
    }
}