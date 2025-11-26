package com.viona.roxflix.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp

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
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            maxLines = 1,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = character ?: "",
            maxLines = 1,
            textAlign = TextAlign.Center,
            fontSize = 11.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
