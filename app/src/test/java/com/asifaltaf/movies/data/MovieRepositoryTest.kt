package com.asifaltaf.movies.data

import com.asifaltaf.movies.data.data_source.MovieApi
import com.asifaltaf.movies.data.data_source.MovieDao
import com.asifaltaf.movies.domain.model.OmdbSearch
import com.asifaltaf.movies.generateMovies
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryTest {

    @Mock
    lateinit var dao: MovieDao
    @Mock
    lateinit var api: MovieApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun searchMovies_expectCorrectSizeInResponse() = runTest {
        val searchQuery = "query"
        val page = "1"

        Mockito.`when`(api.searchMovies(searchQuery, page))
            .thenReturn(
                Response.success(OmdbSearch("False", "True", generateMovies(3), "10"))
            )

        val sut = MovieRepository(dao, api) // system under test

        val sutResult = sut.searchMovies(searchQuery, page.toInt())
        assertEquals(sutResult.getOrThrow().movies.size, 3)
    }

    @Test
    fun searchMovies_expectFailureInResponse() = runTest {
        val searchQuery = "query"
        val page = "1"

        Mockito.`when`(api.searchMovies(searchQuery, page))
            .thenReturn(
                Response.success(202, OmdbSearch("True", "False", emptyList(), "0"))
            )

        val sut = MovieRepository(dao, api) // system under test

        val sutResult = sut.searchMovies(searchQuery, page.toInt())
        assert(sutResult.isFailure)
    }
}