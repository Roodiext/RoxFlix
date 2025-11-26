package com.viona.roxflix.ui.screens

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viona.roxflix.data.model.Genre
import com.viona.roxflix.data.model.Movie
import com.viona.roxflix.data.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: MovieRepository) : ViewModel() {

    var loading by mutableStateOf(true)
        private set

    var heroMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var genres by mutableStateOf<List<Genre>>(emptyList())
        private set

    // map genreId -> movies
    var genreMovies = mutableStateMapOf<Int, List<Movie>>()

    init {
        loadHome()
    }

    private fun loadHome() {
        viewModelScope.launch {
            loading = true
            try {
                // This expects your repository to offer these suspend functions:
                // getPopularMovies(), getGenres(), getMoviesByGenre(genreId)
                val popular = repo.getPopularMovies().results
                heroMovies = popular.take(6)

                val remoteGenres = repo.getGenres().genres
                genres = remoteGenres.map { Genre(it.id, it.name) }.take(6)

                genres.forEach { g ->
                    val movies = repo.getMoviesByGenre(g.id)
                    genreMovies[g.id] = movies
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                loading = false
            }
        }
    }
}
