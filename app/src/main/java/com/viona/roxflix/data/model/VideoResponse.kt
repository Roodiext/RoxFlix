package com.viona.roxflix.data.model

import com.google.gson.annotations.SerializedName

data class Video(
    val id: String?,
    val key: String?,
    val name: String?,
    val site: String?,    // e.g., "YouTube"
    val type: String?     // e.g., "Trailer"
)

data class VideoResponse(
    val id: Int,
    @SerializedName("results")
    val results: List<Video>
)
