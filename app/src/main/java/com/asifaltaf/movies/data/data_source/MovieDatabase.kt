package com.asifaltaf.movies.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.asifaltaf.movies.domain.model.MovieDetailEntity
import com.asifaltaf.movies.domain.model.MovieEntity
import com.asifaltaf.movies.domain.model.Rating
import com.google.gson.Gson

@Database(
    entities = [MovieEntity::class, MovieDetailEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao

    companion object {
        const val DATABASE_NAME = "movies_db"
        const val MOVIE_TABLE_NAME = "movie"
        const val MOVIE_DETAIL_TABLE_NAME = "movie_detail"
    }
}

class Converters {
    @TypeConverter
    fun listToJson(value: List<Rating>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Rating>::class.java).toList()
}