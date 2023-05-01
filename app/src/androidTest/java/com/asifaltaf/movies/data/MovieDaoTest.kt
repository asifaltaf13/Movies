package com.asifaltaf.movies.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.asifaltaf.movies.data.data_source.MovieDao
import com.asifaltaf.movies.data.data_source.MovieDatabase
import com.asifaltaf.movies.domain.model.MovieEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var dao: MovieDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java)
            .allowMainThreadQueries().build()
        dao = database.movieDao
    }

    @After
    fun after() {
        database.close()
    }

    @Test
    fun insertMovies_selectMovieByImdbId_expectCorrectSelection() = runTest {
        generateInsertAndGetMovies(3)

        val movie = dao.selectMovieByImdbID("test id 1")
        assertEquals("test id 1", movie?.imdbID)
    }

    // runBlocking is used instead of runTest
    // it does not optimize the delays which we want
    @Test
    fun insertMovies_selectAllMovies_expectEqualSizes() = runBlocking {
        var value = "1"
        dao.insertMovie(MovieEntity(value, value, value, value, value))
        value = "2"

        launch {
            delay(500)
            dao.insertMovie(MovieEntity(value, value, value, value, value))
        }

        dao.selectAllMoviesFlow().test {
            val moviesList = awaitItem()
            assertEquals(1, moviesList.size)
            val newMoviesList = awaitItem()
            assertEquals(2, newMoviesList.size)
        }
    }

    @Test
    fun insertMovies_deleteMovies_expectNoResults() = runTest {
        generateInsertAndGetMovies(3)

        dao.deleteAllMovies()

        //first emit of flow contains the list of all movies
        val collectedValues = dao.selectAllMoviesFlow().first()

        assertEquals(0, collectedValues.size)
    }

    private suspend fun generateInsertAndGetMovies(n: Int): List<MovieEntity> {
        val movies = (1..n).map { index ->
            MovieEntity(
                "test id $index",
                "test title $index",
                "http://test.poster.url/$index",
                "test type $index",
                "test year $index"
            )
        }.onEach { movie -> dao.insertMovie(movie) }
        return movies
    }
}