package com.asifaltaf.movies.domain.model

import com.google.gson.annotations.SerializedName

data class OmdbSearch(
    @SerializedName("Error") val error: String,
    @SerializedName("Response") val response: String,
    @SerializedName("Search") val movies: List<MovieEntity>,
    @SerializedName("totalResults") val totalResults: String
)