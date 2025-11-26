package com.viona.roxflix.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viona.roxflix.data.model.Movie
import com.viona.roxflix.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class MovieViewModel(private val repo: MovieRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState(isLoading = true))
    val state: StateFlow<HomeUiState> = _state

    init {
        loadPopular()
    }

    fun loadPopular() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val resp = repo.getPopular()
                _state.value = HomeUiState(movies = resp.results, isLoading = false)
            } catch (e: Exception) {
                _state.value = HomeUiState(error = e.localizedMessage ?: "Error", isLoading = false)
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val resp = repo.search(query)
                _state.value = HomeUiState(movies = resp.results, isLoading = false)
            } catch (e: Exception) {
                _state.value = HomeUiState(error = e.localizedMessage ?: "Error", isLoading = false)
            }
        }
    }

    suspend fun getDetail(movieId: Int) = repo.getDetail(movieId)
}
