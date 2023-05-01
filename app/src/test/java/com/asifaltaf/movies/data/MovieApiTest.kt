package com.asifaltaf.movies.data

import com.asifaltaf.movies.Helper
import com.asifaltaf.movies.Helper.getMockApi
import com.asifaltaf.movies.data.data_source.MovieApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: MovieApi

    @Before
    fun setUp() {
        server = MockWebServer()
        api = getMockApi(server)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun testSearchMovies_emptyResponseBody_expectMoviesListNull() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setBody("{}")
        server.enqueue(mockResponse)

        val response = api.searchMovies("Query", "1")
        server.takeRequest()

        response.body()

        assertEquals(true, response.body()!!.movies == null)
    }

    @Test
    fun testSearchMovies_successResponseBody_expectMoviesList() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        server.enqueue(mockResponse)

        val response = api.searchMovies("Query", "1")
        server.takeRequest()

        assertEquals(false, response.body()!!.movies.isEmpty())
        assertEquals(10, response.body()!!.movies.size)
    }

    @Test
    fun testSearchMovies_errorResponseBody_expectErrorMessage() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/error_response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        server.enqueue(mockResponse)

        val response = api.searchMovies("Query", "1")
        server.takeRequest()

        assertEquals(true, response.body()!!.movies == null)
        assertEquals(true, response.body()!!.error.isNotEmpty())
    }
}