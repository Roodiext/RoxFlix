package com.viona.roxflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.viona.roxflix.data.repository.MovieRepository
import com.viona.roxflix.ui.screens.DetailScreen
import com.viona.roxflix.ui.screens.GenreScreen
import com.viona.roxflix.ui.screens.HomeScreen
import com.viona.roxflix.ui.screens.HomeViewModel
import com.viona.roxflix.ui.screens.MovieViewModel
import com.viona.roxflix.ui.screens.SearchScreen
import com.viona.roxflix.ui.screens.SearchViewModel
import com.viona.roxflix.ui.theme.RoxFlixTheme

class MainActivity : ComponentActivity() {

    private val API_KEY = "283d549b8f756cf99801d16dd792e9d2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoxFlixTheme {

                val navController = rememberNavController()
                val repo = remember { MovieRepository(API_KEY) }

                // ViewModels
                val homeViewModel = remember { HomeViewModel(repo) }
                val movieViewModel = remember { MovieViewModel(repo) }
                val searchViewModel = remember { SearchViewModel(repo) }

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {

                    // ✅ HOME SCREEN
                    composable("home") {
                        HomeScreen(
                            viewModel = homeViewModel,
                            onMovieClick = { movieId ->
                                navController.navigate("detail/$movieId")
                            },
                            onSeeAllClick = { genreId, genreName ->
                                val safeName = genreName.replace(" ", "%20")
                                navController.navigate("genre/$genreId/$safeName")
                            },
                            onSearchClick = {
                                navController.navigate("search")
                            }
                        )
                    }

                    // ✅ DETAIL SCREEN
                    composable(
                        route = "detail/{movieId}",
                        arguments = listOf(
                            navArgument("movieId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                        DetailScreen(
                            movieId = movieId,
                            repo = repo,
                            onBack = { navController.popBackStack() },
                            onMovieSelected = { selectedId ->
                                navController.navigate("detail/$selectedId")
                            }
                        )
                    }

                    // ✅ SEARCH SCREEN (FIXED)
                    composable("search") {
                        SearchScreen(
                            viewModel = searchViewModel,
                            onMovieClick = { movieId ->
                                navController.navigate("detail/$movieId")
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // ✅ GENRE SCREEN
                    composable(
                        route = "genre/{genreId}/{genreName}",
                        arguments = listOf(
                            navArgument("genreId") { type = NavType.IntType },
                            navArgument("genreName") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val genreId = backStackEntry.arguments?.getInt("genreId") ?: 0
                        val rawName = backStackEntry.arguments?.getString("genreName") ?: ""
                        val genreName = rawName.replace("%20", " ")

                        GenreScreen(
                            genreId = genreId,
                            genreName = genreName,
                            repo = repo,
                            onBack = { navController.popBackStack() },
                            onMovieClick = { id ->
                                navController.navigate("detail/$id")
                            }
                        )
                    }
                }
            }
        }
    }
}
