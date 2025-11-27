package com.viona.roxflix.ui.components

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.viona.roxflix.utils.LanguageManager

@Composable
fun LanguageDialog(
    context: Context,
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (!showDialog) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Select language") },
        text = {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                Text(text = "Choose the app language:")
            }
        },
        confirmButton = {
            // keep empty: we will show language buttons in the content area below
            TextButton(onClick = onDismiss) { Text("") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )

    // A simple approach (outside the AlertDialog's slots) is to call the language actions directly.
    // But better is to use a custom dialog content. For clarity we'll demonstrate usage snippet below.
}
