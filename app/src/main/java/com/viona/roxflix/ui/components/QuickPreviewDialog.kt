package com.viona.roxflix.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.viona.roxflix.data.model.Movie

@Composable
fun QuickPreviewDialog(movie: Movie, onDismiss: () -> Unit, onOpenDetail: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = movie.title ?: "Preview") },
        text = {
            Row {
                AsyncImage(
                    model = movie.poster_path?.let { "https://image.tmdb.org/t/p/w154$it" },
                    contentDescription = movie.title,
                    modifier = Modifier.size(96.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "⭐ ${"%.1f".format(movie.vote_average ?: 0.0)}")
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = movie.overview?.take(220) ?: "-", maxLines = 6)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onOpenDetail()
                onDismiss()
            }) {
                Text("Open")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
