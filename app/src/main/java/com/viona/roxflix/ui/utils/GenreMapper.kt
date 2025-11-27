package com.viona.roxflix.ui.utils

import android.content.Context
import androidx.compose.ui.res.stringResource
import com.viona.roxflix.R

fun translateGenre(context: Context, englishName: String): String {
    return when (englishName.lowercase()) {
        "action" -> context.getString(R.string.genre_action)
        "adventure" -> context.getString(R.string.genre_adventure)
        "animation" -> context.getString(R.string.genre_animation)
        "comedy" -> context.getString(R.string.genre_comedy)
        "drama" -> context.getString(R.string.genre_drama)
        "crime" -> context.getString(R.string.genre_crime)
        "documentary" -> context.getString(R.string.genre_documentary)
        "fantasy" -> context.getString(R.string.genre_fantasy)
        "horror" -> context.getString(R.string.genre_horror)
        "romance" -> context.getString(R.string.genre_romance)
        "science fiction", "sci-fi", "science_fiction" -> context.getString(R.string.genre_scifi)
        "thriller" -> context.getString(R.string.genre_thriller)
        "family" -> context.getString(R.string.genre_family)
        "war" -> context.getString(R.string.genre_war)
        "mystery" -> context.getString(R.string.genre_mystery)
        "music" -> context.getString(R.string.genre_music)
        "history" -> context.getString(R.string.genre_history)
        else -> englishName // fallback
    }
}
