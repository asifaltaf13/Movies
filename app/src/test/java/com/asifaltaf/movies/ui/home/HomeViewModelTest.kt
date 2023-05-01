package com.asifaltaf.movies.ui.home

import app.cash.turbine.test
import com.asifaltaf.movies.data.MovieRepository
import com.asifaltaf.movies.domain.model.OmdbSearch
import com.asifaltaf.movies.generateMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    fun loadMovies_correctTitle_expectedCorrectResponse() = runTest {
        val searchQuery = "Non empty title"
        val page = 1

        Mockito.`when`(repository.loadMovies(searchQuery, page))
            .thenReturn(Result.success(
                OmdbSearch("False", "True", generateMovies(10), "30"))
            )
        val sut = HomeViewModel(repository)
        sut.changeSearchQuery(searchQuery)

        sut.searchMovies()
        testDispatcher.scheduler.advanceUntilIdle()
        val state = sut.searchState.value

        assertEquals(30, state.totalResults)
    }

    @Test
    fun loadMovies_incorrectTitle_expectErrorMessageResponse() = runTest {
        val searchQuery = "incorrectTitle"
        val page = 1

        Mockito.`when`(repository.loadMovies(searchQuery, page))
            .thenReturn(Result.failure(Exception("Error")))
        val sut = HomeViewModel(repository)
        sut.changeSearchQuery(searchQuery)

        sut.searchMovies()
        testDispatcher.scheduler.advanceUntilIdle()

        sut.toastMessage.test{
            val toastMessage = awaitItem()
            assertNotNull(toastMessage)
        }
    }

    @Test
    fun loadMovies_emptyTitle_expectErrorMessageResponse() = runTest {
        val searchQuery = ""
        val page = 1

        Mockito.`when`(repository.loadMovies(searchQuery, page))
            .thenReturn(Result.failure(Exception("Error")))
        val sut = HomeViewModel(repository)
        sut.changeSearchQuery(searchQuery)

        sut.searchMovies()
        testDispatcher.scheduler.advanceUntilIdle()

        sut.toastMessage.test{
            val toastMessage = awaitItem()
            assertEquals("Movie title can not be empty", toastMessage)
        }
    }
}