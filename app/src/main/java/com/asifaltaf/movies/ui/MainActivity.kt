package com.asifaltaf.movies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.asifaltaf.movies.ui.details.DetailsScreen
import com.asifaltaf.movies.ui.home.HomeScreen
import com.asifaltaf.movies.ui.theme.MoviesTheme
import com.asifaltaf.movies.ui.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        modifier = Modifier
                            .semantics {
                                testTagsAsResourceId = true
                            },
                        navController = navController,
                        startDestination = Screen.HomeScreen.route
                    ) {

                        composable(
                            route = Screen.HomeScreen.route
                        ) {
                            HomeScreen(navController = navController)
                        }

                        composable(
                            route = Screen.DetailsScreen.route + "?imdbID={imdbID}",
                            arguments = listOf(
                                navArgument(
                                    name = "imdbID"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ) {
                            DetailsScreen()
                        }
                    }
                }
            }
        }
    }
}