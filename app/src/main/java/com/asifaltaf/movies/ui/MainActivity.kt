@file:OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)

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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
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

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    MoviesTheme {
                        windowInsetsController.isAppearanceLightStatusBars = !isSystemInDarkTheme()
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MoviesNavigation()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MoviesNavigation() {
        val navController = rememberAnimatedNavController()

        AnimatedNavHost(
            modifier = Modifier.semantics { testTagsAsResourceId = true },
            navController = navController,
            startDestination = Screen.HomeScreen.route
        ) {
            homeNavigation(navController)
            detailsNavigation()
        }
    }
}

private fun NavGraphBuilder.homeNavigation(
    navController: NavHostController
) {
    composable(
        route = Screen.HomeScreen.route,
    ) {
        HomeScreen(navController = navController)
    }
}

private fun NavGraphBuilder.detailsNavigation() {
    composable(
        route = Screen.DetailsScreen.route + "?imdbID={imdbID}",
        arguments = listOf(
            navArgument(name = "imdbID") {
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