package com.viona.roxflix.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.viona.roxflix.ui.components.LottieEmptyState
import com.viona.roxflix.ui.components.RecentSearchItem
import com.viona.roxflix.ui.components.SearchResultItem
import com.viona.roxflix.ui.components.VoiceSearchButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    // Launcher for voice recognition result
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val heard = matches?.firstOrNull()
            if (!heard.isNullOrBlank()) {
                viewModel.updateQuery(heard)
                viewModel.performSearchNow()
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text("Search", style = MaterialTheme.typography.titleMedium)
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            // SEARCH ROW
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = viewModel.query,
                    onValueChange = { viewModel.updateQuery(it) },
                    placeholder = { Text("Search movies, actors...") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    singleLine = true,
                    trailingIcon = {
                        if (viewModel.query.isNotBlank()) {
                            IconButton(onClick = { viewModel.updateQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                            viewModel.performSearchNow()
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Voice button
                VoiceSearchButton(onStartVoice = {
                    // launch Android speech recognizer
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
                    }
                    launcher.launch(intent)
                })
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Recent searches (swipe-to-delete)
            if (viewModel.recentSearches.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent", fontWeight = FontWeight.Bold)
                    TextButton(onClick = { viewModel.clearRecents() }) {
                        Text("Clear all")
                    }
                }
                Column {
                    viewModel.recentSearches.forEach { s ->
                        RecentSearchItem(
                            query = s,
                            onClick = {
                                viewModel.updateQuery(it)
                                viewModel.performSearchNow()
                            },
                            onDelete = { viewModel.removeRecent(it) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Genre chips
            val demoGenres = listOf(28 to "Action", 12 to "Adventure", 16 to "Animation", 35 to "Comedy", 18 to "Drama")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                demoGenres.forEach { (id, name) ->
                    val selected = viewModel.selectedGenres.contains(id)
                    val bg by animateColorAsState(
                        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        label = "Chip background color"
                    )
                    AssistChip(
                        onClick = { viewModel.toggleGenre(id) },
                        label = { Text(name) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = bg)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Results or empty state
            if (viewModel.loading && viewModel.results.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (!viewModel.loading && viewModel.results.isEmpty()) {
                LottieEmptyState(
                    modifier = Modifier.fillMaxWidth().height(260.dp),
                    message = if (viewModel.query.isBlank()) "Try searching movies or actors" else "No results for '''${viewModel.query}'''"
                )
            } else {
                // result list with infinite scroll
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(viewModel.results) { index, movie ->
                        SearchResultItem(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) },
                            onLongPress = { selected ->
                                // show quick preview dialog; handled inside SearchResultItem
                            }
                        )
                        // check to load more
                        LaunchedEffect(index) {
                            viewModel.loadMoreIfNeeded(lastVisibleIndex = index)
                        }
                    }

                    // loading footer
                    item {
                        if (viewModel.loading) {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(modifier = Modifier.padding(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
