package com.viona.roxflix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

@Composable
fun CastItemSimple(name: String, character: String?, profilePath: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(90.dp)
            .padding(end = 12.dp),
        verticalArrangement = Arrangement.Top
    ) {
        AsyncImage(
            model = profilePath?.let { "https://image.tmdb.org/t/p/w185$it" },
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Actor Name (Theme-aware)
        Text(
            text = name,
            maxLines = 1,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        // ✅ Character Name (Greyed & Theme-aware, Hidden if blank)
        if (!character.isNullOrBlank()) {
            Text(
                text = character,
                maxLines = 1,
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
