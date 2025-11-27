package com.viona.roxflix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.viona.roxflix.R
import com.viona.roxflix.data.repository.MovieRepository
import com.viona.roxflix.ui.components.MovieItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreScreen(
    genreId: Int,
    genreName: String,
    repo: MovieRepository,
    onBack: () -> Unit,
    onMovieClick: (Int) -> Unit
) {
    // ✅ Localized strings (must be read inside composable, not coroutine)
    val backLabel = stringResource(id = R.string.back)
    val noMoviesLabel = stringResource(id = R.string.no_movies_found)

    var movies by remember { mutableStateOf(emptyList<com.viona.roxflix.data.model.Movie>()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(genreId) {
        loading = true
        try {
            movies = repo.getMoviesByGenre(genreId)
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = genreName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = backLabel)
                    }
                }
            )
        }
    ) { paddingValues ->

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // ✅ No movies state
        if (movies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = noMoviesLabel,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            return@Scaffold
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies) { movie ->

                MovieItemCard(
                    posterPath = movie.poster_path ?: "",
                    title = movie.title,
                    rating = movie.vote_average ?: 0.0,
                    year = (movie.release_date ?: "").take(4),
                    onClick = { onMovieClick(movie.id) }
                )
            }
        }
    }
}
