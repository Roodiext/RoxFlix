package com.viona.roxflix.utils

import android.content.Context
import com.viona.roxflix.R

fun translateGenre(context: Context, original: String): String {
    return when (original.lowercase()) {
        "action" -> context.getString(R.string.genre_action)
        "adventure" -> context.getString(R.string.genre_adventure)
        "animation" -> context.getString(R.string.genre_animation)
        "comedy" -> context.getString(R.string.genre_comedy)
        "crime" -> context.getString(R.string.genre_crime)
        "documentary" -> context.getString(R.string.genre_documentary)
        "drama" -> context.getString(R.string.genre_drama)
        "family" -> context.getString(R.string.genre_family)
        "fantasy" -> context.getString(R.string.genre_fantasy)
        "history" -> context.getString(R.string.genre_history)
        "horror" -> context.getString(R.string.genre_horror)
        "music" -> context.getString(R.string.genre_music)
        "mystery" -> context.getString(R.string.genre_mystery)
        "romance" -> context.getString(R.string.genre_romance)
        "science fiction", "sci-fi" -> context.getString(R.string.genre_scifi)
        "tv movie" -> context.getString(R.string.genre_tvmovie)
        "thriller" -> context.getString(R.string.genre_thriller)
        "war" -> context.getString(R.string.genre_war)
        "western" -> context.getString(R.string.genre_western)
        else -> original
    }
}
