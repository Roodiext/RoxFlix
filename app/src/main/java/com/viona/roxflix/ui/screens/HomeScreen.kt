package com.viona.roxflix.ui.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.viona.roxflix.R
import com.viona.roxflix.ui.components.MovieItemCard
import com.viona.roxflix.ui.components.HeroCarousel
import com.viona.roxflix.ui.components.PremiumSearchBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.viona.roxflix.utils.LanguageManager
import com.viona.roxflix.utils.translateGenre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (Int, String) -> Unit,
    onSearchClick: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as? Activity

    var showLanguageDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
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

            // SEARCH + LANGUAGE BUTTON + HERO
            item {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // SEARCH BAR
                        Box(modifier = Modifier.weight(1f)) {
                            PremiumSearchBar(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onSearchClick() }
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // LANGUAGE BUTTON
                        IconButton(onClick = { showLanguageDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Change Language",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HeroCarousel(
                        items = viewModel.heroMovies,
                        onItemClick = onMovieClick
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // GENRE LIST
            items(viewModel.genres) { genre ->

                val translatedGenre = translateGenre(context, genre.name)

                Column(Modifier.fillMaxWidth()) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = translatedGenre,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        TextButton(
                            onClick = { onSeeAllClick(genre.id, genre.name) }
                        ) {
                            Text(
                                text = stringResource(id = R.string.see_all),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

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

    // ✅ LANGUAGE DIALOG (2 Column Flags)
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(R.string.choose_language)) },
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 🇮🇩 Bahasa Indonesia
                    Image(
                        painter = painterResource(id = R.drawable.id),
                        contentDescription = "Bahasa Indonesia",
                        modifier = Modifier
                            .size(70.dp)
                            .clickable {
                                LanguageManager.saveLanguage(context, "id")
                                showLanguageDialog = false
                                activity?.recreate()
                            }
                    )

                    // 🇬🇧 English
                    Image(
                        painter = painterResource(id = R.drawable.en),
                        contentDescription = "English",
                        modifier = Modifier
                            .size(70.dp)
                            .clickable {
                                LanguageManager.saveLanguage(context, "en")
                                showLanguageDialog = false
                                activity?.recreate()
                            }
                    )
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }




}
