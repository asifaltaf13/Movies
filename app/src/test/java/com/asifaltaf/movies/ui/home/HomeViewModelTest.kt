package com.asifaltaf.movies.ui.home

import com.asifaltaf.movies.data.MovieRepository
import com.asifaltaf.movies.domain.model.OmdbSearch
import com.asifaltaf.movies.generateMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchMovies_correctTitle_expectedCorrectMoviesInResponse() = runTest {
        val searchQuery = "Non empty title"
        val page = 1

        Mockito.`when`(repository.searchMovies(searchQuery, page))
            .thenReturn(Result.success(
                OmdbSearch("False", "True", generateMovies(10), "30"))
            )
        val sut = HomeViewModel(repository)
        sut.changeSearchQuery(searchQuery)

        sut.searchMovies()
        testDispatcher.scheduler.advanceUntilIdle()
        val state = sut.state.value

        assertEquals(10, state.movies.size)
        assertEquals(30, state.totalResults)
    }

    @Test
    fun showLoadedMovies_expectedCorrectMoviesInResponse() = runTest {
        val movies = generateMovies(3)

        Mockito.`when`(repository.selectAllMoviesFlow())
            .thenReturn(flow{emit(movies)})

        val sut = HomeViewModel(repository)
        sut.showLoadedMovies()
        testDispatcher.scheduler.advanceUntilIdle()
        val state = sut.state.value

        assertEquals(3, state.movies.size)
    }
}