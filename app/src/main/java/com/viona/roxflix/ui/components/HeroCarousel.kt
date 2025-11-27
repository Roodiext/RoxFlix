package com.viona.roxflix.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun HeroCarousel(
    items: List<com.viona.roxflix.data.model.Movie>,
    onItemClick: (Int) -> Unit
) {
    if (items.isEmpty()) return

    val randomized = remember { items.shuffled(Random(System.currentTimeMillis())) }
    var index by remember { mutableStateOf(0) }

    LaunchedEffect(randomized) {
        while (true) {
            delay(3000L)
            index = (index + 1) % randomized.size
        }
    }

    Crossfade(
        targetState = index,
        animationSpec = tween(durationMillis = 800)
    ) { i ->

        val movie = randomized[i]

        val imageUrl = movie.poster_path?.let {
            "https://image.tmdb.org/t/p/original$it"
        } ?: ""

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .clickable { onItemClick(movie.id) }
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = movie.title ?: "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // ✅ Gradient Overlay Bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
            )

            // ✅ Title + Overview Text
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = movie.title ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = movie.overview ?: "",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
