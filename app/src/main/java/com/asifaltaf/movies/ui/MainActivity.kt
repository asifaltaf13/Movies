package com.asifaltaf.movies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.asifaltaf.movies.ui.details.DetailsScreen
import com.asifaltaf.movies.ui.home.HomeScreen
import com.asifaltaf.movies.ui.theme.MoviesTheme
import com.asifaltaf.movies.ui.util.Screen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    MoviesTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            val navController = rememberAnimatedNavController()

                            AnimatedNavHost(
                                modifier = Modifier.semantics { testTagsAsResourceId = true },
                                navController = navController,
                                startDestination = Screen.HomeScreen.route
                            ) {
                                composable(
                                    route = Screen.HomeScreen.route,
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
                                    ),
                                    enterTransition = {
                                        fadeIn(tween()) + slideInHorizontally(
                                            initialOffsetX = { fullWidth -> fullWidth })
                                    },
                                    exitTransition = {
                                        fadeOut() + slideOutHorizontally(
                                            targetOffsetX = { fullWidth -> fullWidth })
                                    }
                                ) {
                                    DetailsScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}