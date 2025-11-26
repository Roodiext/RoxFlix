package com.viona.roxflix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.viona.roxflix.ui.components.MovieItemCard
import com.viona.roxflix.ui.components.HeroCarousel
import com.viona.roxflix.ui.components.PremiumSearchBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (Int, String) -> Unit,
    onSearchClick: () -> Unit
) {

    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ROXFLIX",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            item {
                Column(modifier = Modifier.fillMaxWidth()) {

                    PremiumSearchBar(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        onClick = { onSearchClick() }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HeroCarousel(items = viewModel.heroMovies, onItemClick = onMovieClick)
                    Spacer(Modifier.height(8.dp))
                }
            }

            items(viewModel.genres) { genre ->
                Column(Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(genre.name, color = MaterialTheme.colorScheme.onBackground)
                        TextButton(onClick = { onSeeAllClick(genre.id, genre.name) }) {
                            Text("See All", color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val movies = viewModel.genreMovies[genre.id] ?: emptyList()
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
        }
    }
}
