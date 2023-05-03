package com.asifaltaf.movies.domain.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.asifaltaf.movies.data.data_source.MovieDatabase
import com.google.gson.annotations.SerializedName

@Entity(tableName = MovieDatabase.MOVIE_DETAIL_TABLE_NAME)
data class MovieDetailEntity(
    @PrimaryKey val imdbID: String,
    @SerializedName("Poster") @ColumnInfo(defaultValue = "") val poster: String = "",
    @SerializedName("Title") @ColumnInfo(defaultValue = "") val title: String = "",
    @SerializedName("Type") @ColumnInfo(defaultValue = "") val type: String = "",
    @SerializedName("Year") @ColumnInfo(defaultValue = "") val year: String = "",
    @SerializedName("imdbRating") @ColumnInfo(defaultValue = "") val imdbRating: String = "",
    @SerializedName("Genre") @ColumnInfo(defaultValue = "") val genre: String = "",
    @SerializedName("Country") @ColumnInfo(defaultValue = "") val country: String = "",
    @SerializedName("Language") @ColumnInfo(defaultValue = "") val language: String = "",
    @SerializedName("Rated") @ColumnInfo(defaultValue = "") val rated: String = "",
    @SerializedName("Released") @ColumnInfo(defaultValue = "") val released: String = "",
    @SerializedName("Actors") @ColumnInfo(defaultValue = "") val actors: String = "",
    @SerializedName("Director") @ColumnInfo(defaultValue = "") val director: String = "",
    @SerializedName("Production") @ColumnInfo(defaultValue = "") val production: String = "",
    @SerializedName("Writer") @ColumnInfo(defaultValue = "") val writer: String = "",
    @SerializedName("BoxOffice") @ColumnInfo(defaultValue = "") val boxOffice: String = "",
    @SerializedName("Awards") @ColumnInfo(defaultValue = "") val awards: String = "",
    @SerializedName("imdbVotes") @ColumnInfo(defaultValue = "") val imdbVotes: String = "",
    @SerializedName("Metascore") @ColumnInfo(defaultValue = "") val metascore: String = "",
    @SerializedName("Runtime") @ColumnInfo(defaultValue = "") val runtime: String = "",
    @SerializedName("DVD") @ColumnInfo(defaultValue = "") val dVD: String = "",
    @SerializedName("Website") @ColumnInfo(defaultValue = "") val website: String = "",
    @SerializedName("Plot") @ColumnInfo(defaultValue = "") val plot: String = "",
    @SerializedName("Ratings") @ColumnInfo(defaultValue = "") val ratings: List<Rating> = emptyList(),
    @SerializedName("Response") @ColumnInfo(defaultValue = "") val response: String = "",
    @SerializedName("Error") @ColumnInfo(defaultValue = "") val error: String? = "",
) {
    companion object {
        fun fromMovie(movie: MovieEntity): MovieDetailEntity {
            return MovieDetailEntity(
                imdbID = movie.imdbID,
                poster = movie.posterUrl,
                type = movie.type,
                year = movie.year,
                title = movie.title
            )
        }
    }
}