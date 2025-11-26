package com.viona.roxflix.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun ExpandableText(text: String, minimizedMaxLines: Int = 4) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            modifier = Modifier.clickable { expanded = !expanded }
        )
        if (!expanded) {
            Text(text = "... Read more", color = androidx.compose.ui.graphics.Color.Gray, modifier = Modifier.clickable { expanded = true })
        } else {
            Text(text = "Show less", color = androidx.compose.ui.graphics.Color.Gray, modifier = Modifier.clickable { expanded = false })
        }
    }
}
