package com.asifaltaf.movies.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey val imdbID: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Poster") val posterUrl: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Year") val year: String,
)