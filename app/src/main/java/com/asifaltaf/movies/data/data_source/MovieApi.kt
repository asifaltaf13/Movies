package com.asifaltaf.movies.data.data_source

import com.asifaltaf.movies.BuildConfig
import com.asifaltaf.movies.domain.model.MovieDetailEntity
import com.asifaltaf.movies.domain.model.OmdbSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    companion object {
        const val BASE_URL = "https://www.omdbapi.com"
    }

    @GET("/")
    suspend fun searchMovies(
        @Query("s") searchQuery: String,
        @Query("page") page: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): Response<OmdbSearch>

    @GET("/")
    suspend fun searchMovie(
        @Query("i") imdbID: String,
        @Query("plot") plot: String = "full",
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): Response<MovieDetailEntity>


}