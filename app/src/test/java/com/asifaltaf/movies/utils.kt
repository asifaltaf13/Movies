package com.asifaltaf.movies

import com.asifaltaf.movies.data.data_source.MovieApi
import com.asifaltaf.movies.domain.model.MovieEntity
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader

fun generateMovies(n: Int): List<MovieEntity> {
    return (1..n).map { index ->
        MovieEntity(
            "test id $index",
            "test title $index",
            "http://test.poster.url/$index",
            "test type $index",
            "test year $index"
        )
    }
}

object Helper {
    fun readFileResource(filename: String): String {
        val inputStream = Helper::class.java.getResourceAsStream(filename)
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach { line ->
            builder.append(line)
        }
        return builder.toString()
    }

    fun getMockApi(server: MockWebServer): MovieApi {
        return Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MovieApi::class.java)
    }
}