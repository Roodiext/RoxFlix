package com.viona.roxflix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import androidx.compose.ui.unit.dp
import com.viona.roxflix.data.repository.MovieRepository
import com.viona.roxflix.ui.components.CastItemSimple
import com.viona.roxflix.ui.components.ExpandableText
import com.viona.roxflix.ui.components.GenreChip
import com.viona.roxflix.ui.components.MovieItemCard
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    repo: MovieRepository,
    onBack: () -> Unit,
    onWatchTrailer: ((String) -> Unit)? = null,
    onMovieSelected: (Int) -> Unit = {}
) {
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var detail by remember { mutableStateOf<com.viona.roxflix.data.model.MovieDetail?>(null) }
    var cast by remember { mutableStateOf<List<com.viona.roxflix.data.remote.CastMember>>(emptyList()) }
    var similar by remember { mutableStateOf<List<com.viona.roxflix.data.model.Movie>>(emptyList()) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(movieId) {
        loading = true
        try {
            detail = repo.getDetail(movieId)
            cast = repo.getCredits(movieId).cast
            similar = repo.getSimilarMovies(movieId).results
        } catch (e: Exception) {
            e.printStackTrace()
            error = e.localizedMessage ?: "Failed to load details"
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(detail?.title ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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

        error?.let { err ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = err)
            }
            return@Scaffold
        }

        detail?.let { m ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Black)
                ) {
                    AsyncImage(
                        model = m.backdrop_path?.let { "https://image.tmdb.org/t/p/w780$it" },
                        contentDescription = m.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFE50914),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "%.1f".format(m.vote_average ?: 0.0),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    val key = repo.getVideos(movieId).results.firstOrNull()?.key ?: ""
                                    if (key.isNotBlank()) onWatchTrailer?.invoke(key)
                                } catch (_: Exception) { }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                    ) {
                        Text(text = "Watch Trailer")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(text = m.title, style = MaterialTheme.typography.titleLarge)

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(m.genres) { g ->
                            GenreChip(name = g.name)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Synopsis", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExpandableText(m.overview ?: "-")

                    Spacer(modifier = Modifier.height(16.dp))

                    if (cast.isNotEmpty()) {
                        Text(text = "Cast", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(cast) { actor ->
                                CastItemSimple(
                                    name = actor.name ?: "",
                                    character = actor.character ?: "",
                                    profilePath = actor.profile_path
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (similar.isNotEmpty()) {
                        Text(text = "Similar Movies", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(similar) { s ->

                                // ✅ ✅ FIX — Gunakan parameter yang benar
                                MovieItemCard(
                                    posterPath = s.poster_path ?: "",
                                    title = s.title,
                                    rating = s.vote_average ?: 0.0,
                                    year = s.release_date?.take(4) ?: "-",
                                    onClick = { onMovieSelected(s.id) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}
