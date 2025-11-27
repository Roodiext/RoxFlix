package com.viona.roxflix.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.viona.roxflix.R

@Composable
fun ExpandableText(
    text: String,
    minimizedMaxLines: Int = 4
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // ✅ Fetch localized strings
    val readMore = context.getString(R.string.read_more)
    val showLess = context.getString(R.string.show_less)

    Column {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable { expanded = !expanded }
        )

        Text(
            text = if (expanded) showLess else readMore,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.clickable { expanded = !expanded }
        )
    }
}
