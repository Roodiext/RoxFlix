package com.viona.roxflix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun RecentSearchItem(query: String, onClick: (String) -> Unit, onDelete: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        tonalElevation = 2.dp,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onClick(query) })
            },
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(text = query, modifier = Modifier.weight(1f))
            IconButton(onClick = { onDelete(query) }) {
                androidx.compose.material3.Icon(imageVector = Icons.Default.Close, contentDescription = "Delete")
            }
        }
    }
}
