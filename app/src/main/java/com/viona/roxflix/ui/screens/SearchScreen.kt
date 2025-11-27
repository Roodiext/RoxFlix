package com.viona.roxflix.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.viona.roxflix.R
import com.viona.roxflix.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.search_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = viewModel.query,
                        onValueChange = { viewModel.updateQuery(it) },
                        placeholder = { Text(stringResource(R.string.search_placeholder)) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        trailingIcon = {
                            if (viewModel.query.isNotBlank()) {
                                IconButton(onClick = { viewModel.updateQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = null)
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                                viewModel.performSearchNow()
                            }
                        )
                    )

                    Spacer(Modifier.width(8.dp))

                    VoiceSearchButton {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
                            putExtra(RecognizerIntent.EXTRA_PROMPT, context.getString(R.string.voice_prompt))
                        }
                        launcher.launch(intent)
                    }
                }
            }

            if (viewModel.recentSearches.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.recent))
                        TextButton(onClick = { viewModel.clearRecents() }) {
                            Text(stringResource(R.string.clear_all))
                        }
                    }
                }

                items(viewModel.recentSearches) { s ->
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

            item {
                val demoGenres = listOf(
                    28 to stringResource(R.string.genre_action),
                    12 to stringResource(R.string.genre_adventure),
                    16 to stringResource(R.string.genre_animation),
                    35 to stringResource(R.string.genre_comedy),
                    18 to stringResource(R.string.genre_drama)
                )

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    demoGenres.forEach { (id, name) ->
                        val selected = viewModel.selectedGenres.contains(id)
                        val bg by animateColorAsState(
                            if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        AssistChip(
                            onClick = { viewModel.toggleGenre(id) },
                            label = { Text(name) },
                            colors = AssistChipDefaults.assistChipColors(bg)
                        )
                    }
                }
            }

            if (viewModel.loading && viewModel.results.isEmpty()) {
                item {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (!viewModel.loading && viewModel.results.isEmpty()) {
                item {
                    LottieEmptyState(
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                        message = if (viewModel.query.isBlank())
                            stringResource(R.string.search_empty_hint)
                        else
                            stringResource(R.string.search_no_results, viewModel.query)
                    )
                }
            } else {
                itemsIndexed(viewModel.results) { index, movie ->
                    SearchResultItem(
                        movie = movie,
                        onClick = { onMovieClick(movie.id) }
                    )

                    LaunchedEffect(index) {
                        viewModel.loadMoreIfNeeded(index)
                    }
                }

                item {
                    if (viewModel.loading) {
                        Box(
                            Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }
    }
}
