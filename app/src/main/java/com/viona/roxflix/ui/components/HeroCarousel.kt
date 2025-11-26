package com.viona.roxflix.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import kotlin.random.Random
import kotlinx.coroutines.delay

@Composable
fun HeroCarousel(
    items: List<com.viona.roxflix.data.model.Movie>,
    onItemClick: (Int) -> Unit
) {
    if (items.isEmpty()) return

    // shuffle pada awal agar terlihat bervariasi
    val randomized = remember { items.shuffled(Random(System.currentTimeMillis())) }

    var index by remember { mutableStateOf(0) }

    // auto slide loop
    LaunchedEffect(randomized) {
        while (true) {
            delay(3000L) // 3 detik per slide (kamu bisa ubah)
            index = (index + 1) % randomized.size
        }
    }

    Crossfade(targetState = index, animationSpec = tween(durationMillis = 800)) { i ->
        val movie = randomized[i]
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
        ) {
            // gunakan poster_path (ada di model kamu). kalau kosong, kosongkan url.
            val imageUrl = movie.poster_path?.let { "https://image.tmdb.org/t/p/original$it" } ?: ""
            AsyncImage(
                model = imageUrl,
                contentDescription = movie.title ?: "hero",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            // (optional) kamu bisa overlay judul / tombol di sini dan panggil onItemClick(movie.id)
        }
    }
}
