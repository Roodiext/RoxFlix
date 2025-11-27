package com.viona.roxflix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.viona.roxflix.data.model.Movie
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SearchResultItem(
    movie: Movie,
    onClick: () -> Unit,
    onLongPress: (Movie) -> Unit = {}
) {
    var showPreview by remember { mutableStateOf(false) }

    if (showPreview) {
        QuickPreviewDialog(movie = movie, onDismiss = { showPreview = false }, onOpenDetail = onClick)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 6.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showPreview = true
                        onLongPress(movie)
                    },
                    onTap = { onClick() }
                )
            }
    ) {
        Row(modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            , verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = movie.poster_path?.let { "https://image.tmdb.org/t/p/w154$it" },
                contentDescription = movie.title,
                modifier = Modifier
                    .size(width = 92.dp, height = 128.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title ?: "-",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = movie.overview?.take(160) ?: "-",
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("⭐ ${"%.1f".format(movie.vote_average ?: 0.0)}", color = MaterialTheme.colorScheme.primary)
                    Text(movie.release_date?.take(4) ?: "-", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
