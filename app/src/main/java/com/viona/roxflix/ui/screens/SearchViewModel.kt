package com.viona.roxflix.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viona.roxflix.data.model.Movie
import com.viona.roxflix.data.repository.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repo: MovieRepository) : ViewModel() {

    var query by mutableStateOf("")
        private set

    var results = mutableStateListOf<Movie>()
        private set

    var loading by mutableStateOf(false)
        private set

    var recentSearches = mutableStateListOf<String>()
        private set

    var selectedGenres = mutableStateListOf<Int>()
        private set

    private var page = 1
    private var canLoadMore = true

    fun updateQuery(newQuery: String) {
        query = newQuery
        if (newQuery.isBlank()) {
            results.clear()
        }
    }

    fun performSearchNow() {
        if (query.isBlank()) return
        page = 1
        canLoadMore = true
        results.clear()

        saveRecent(query)

        viewModelScope.launch {
            loading = true
            try {
                // Assuming repo.search can take a page number.
                val resp = repo.search(query, page)
                results.addAll(resp.results)
                if (resp.results.isEmpty()) {
                    canLoadMore = false
                }
            } catch (_: Exception) {
                // In a real app, handle errors
            } finally {
                loading = false
            }
        }
    }

    fun loadMoreIfNeeded(lastVisibleIndex: Int) {
        if (!canLoadMore || loading) return
        val buffer = 5 // Number of items from end to start loading more
        if (lastVisibleIndex < results.size - buffer) return

        page++
        viewModelScope.launch {
            try {
                val resp = repo.search(query, page)
                if (resp.results.isEmpty()) {
                    canLoadMore = false
                } else {
                    results.addAll(resp.results)
                }
            } catch (_: Exception) {
                canLoadMore = false // stop trying on error
            }
        }
    }

    fun toggleGenre(id: Int) {
        if (selectedGenres.contains(id)) {
            selectedGenres.remove(id)
        } else {
            selectedGenres.add(id)
        }
    }

    private fun saveRecent(q: String) {
        if (recentSearches.contains(q)) {
            recentSearches.remove(q)
        }
        recentSearches.add(0, q)
        if (recentSearches.size > 10) {
            recentSearches.removeAt(recentSearches.lastIndex)
        }
    }

    fun clearRecents() = recentSearches.clear()

    fun removeRecent(q: String) = recentSearches.remove(q)
}
