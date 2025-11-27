package com.viona.roxflix

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.viona.roxflix.data.repository.MovieRepository
import com.viona.roxflix.ui.screens.*
import com.viona.roxflix.ui.theme.RoxFlixTheme
import com.viona.roxflix.utils.LanguageManager

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val updatedContext = LanguageManager.applyLocale(newBase)
        super.attachBaseContext(updatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Apply locale BEFORE setContent
        LanguageManager.applyLocale(this)

        setContent {
            RoxFlixTheme {

                val navController = rememberNavController()
                val repo = remember { MovieRepository("283d549b8f756cf99801d16dd792e9d2") }

                val homeViewModel = remember { HomeViewModel(repo) }
                val movieViewModel = remember { MovieViewModel(repo) }
                val searchViewModel = remember { SearchViewModel(repo) }

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            viewModel = homeViewModel,
                            onMovieClick = { navController.navigate("detail/$it") },
                            onSeeAllClick = { id, name ->
                                navController.navigate("genre/$id/${name.replace(" ", "%20")}")
                            },
                            onSearchClick = { navController.navigate("search") }
                        )
                    }

                    composable(
                        "detail/{movieId}",
                        arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        DetailScreen(
                            movieId = backStackEntry.arguments?.getInt("movieId") ?: 0,
                            repo = repo,
                            onBack = { navController.popBackStack() },
                            onMovieSelected = { navController.navigate("detail/$it") }
                        )
                    }

                    composable("search") {
                        SearchScreen(
                            viewModel = searchViewModel,
                            onMovieClick = { navController.navigate("detail/$it") },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        "genre/{genreId}/{genreName}",
                        arguments = listOf(
                            navArgument("genreId") { type = NavType.IntType },
                            navArgument("genreName") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        GenreScreen(
                            genreId = backStackEntry.arguments?.getInt("genreId") ?: 0,
                            genreName = backStackEntry.arguments?.getString("genreName")?.replace("%20", " ") ?: "",
                            repo = repo,
                            onBack = { navController.popBackStack() },
                            onMovieClick = { navController.navigate("detail/$it") }
                        )
                    }
                }
            }
        }
    }
}
