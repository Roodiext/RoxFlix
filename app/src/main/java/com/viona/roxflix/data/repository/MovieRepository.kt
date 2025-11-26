package com.viona.roxflix.data.repository

import com.viona.roxflix.data.model.*
import com.viona.roxflix.data.remote.RetrofitInstance
import com.viona.roxflix.data.remote.TmdbApiService

class MovieRepository(private val apiKey: String) {

    private val api: TmdbApiService = RetrofitInstance.api

    // Popular (wrapper name used by ViewModel)
    suspend fun getPopularMovies(page: Int = 1): MovieResponse =
        api.getPopularMovies(apiKey = apiKey, page = page)

    // kept original short name
    suspend fun getPopular(page: Int = 1): MovieResponse =
        api.getPopularMovies(apiKey = apiKey, page = page)

    suspend fun search(query: String, page: Int = 1): MovieResponse =
        api.searchMovies(apiKey = apiKey, query = query, page = page)

    suspend fun getDetail(movieId: Int): MovieDetail =
        api.getMovieDetail(movieId, apiKey = apiKey)

    suspend fun getCredits(movieId: Int) =
        api.getMovieCredits(movieId, apiKey = apiKey)

    suspend fun getSimilarMovies(movieId: Int): MovieResponse =
        api.getSimilarMovies(movieId, apiKey = apiKey)

    suspend fun getVideos(movieId: Int): VideoResponse =
        api.getMovieVideos(movieId, apiKey = apiKey)

    // --- FIX: convert remote GenreResponse -> model.GenreResponse to avoid return-type mismatch
    suspend fun getGenres(): GenreResponse {
        val remote = api.getGenres(apiKey = apiKey) // remote.GenreResponse
        // map each remote genre to model.Genre
        val mapped = remote.genres.map { r ->
            // assuming remote genre has id and name fields
            Genre(id = r.id, name = r.name)
        }
        return GenreResponse(genres = mapped)
    }

    suspend fun getMoviesByGenre(genreId: Int): List<Movie> =
        api.getMoviesByGenre(genreId, apiKey).results
}
