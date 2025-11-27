package com.viona.roxflix.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.viona.roxflix.R

@Composable
fun RecentSearchItem(
    query: String,
    onClick: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { onClick(query) })
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = query,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { onDelete(query) }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        }
    }
}
