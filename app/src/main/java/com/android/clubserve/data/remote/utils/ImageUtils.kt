package com.android.clubserve.data.remote.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class ImageData(
    val imageSrc: String? = null
)

object ImageUtils {
    private const val BASE_URL = "https://control.app-preview.sb.clubserve.app"
    private val json = Json { ignoreUnknownKeys = true }

    fun getFullImageUrl(imagePath: String?): String? {
        if (imagePath.isNullOrEmpty()) return null
        
        val path = if (imagePath.startsWith("{")) {
            try {
                val data = json.decodeFromString<ImageData>(imagePath)
                data.imageSrc
            } catch (e: Exception) {
                null
            }
        } else {
            imagePath
        }

        return if (path != null) {
            if (path.startsWith("http")) path else "$BASE_URL$path"
        } else {
            null
        }
    }
}
