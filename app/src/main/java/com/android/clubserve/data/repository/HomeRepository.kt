package com.android.clubserve.data.repository

import com.android.clubserve.data.remote.api.ClubServeApi
import com.android.clubserve.data.remote.dto.*
import kotlinx.serialization.json.*

data class HomeDomainState(
    val categories: List<MainCategoryDto> = emptyList(),
    val subCategories: List<SubCategoryDto> = emptyList(),
    val groups: List<GroupDto> = emptyList(),
    val lovedByLocals: List<SubCategoryDto> = emptyList(),
    val bookings: List<BookingDto> = emptyList(),
    val error: String? = null
)

class HomeRepository(private val api: ClubServeApi) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getHomeData(): HomeDomainState {
        return try {
            val elements = api.getHomeData()
            
            val categories = parseElement<List<MainCategoryDto>>(elements.getOrNull(0)) ?: emptyList()
            val subCategories = parseElement<List<SubCategoryDto>>(elements.getOrNull(1)) ?: emptyList()
            val groups = parseElement<List<GroupDto>>(elements.getOrNull(2)) ?: emptyList()
            val lovedByLocals = parseElement<LovedByLocalsResponse>(elements.getOrNull(3))?.data ?: emptyList()
            // Bookings might fail if unauthorized, which is index 4
            val bookings = parseElement<List<BookingDto>>(elements.getOrNull(4)) ?: emptyList()

            HomeDomainState(
                categories = categories,
                subCategories = subCategories,
                groups = groups,
                lovedByLocals = lovedByLocals,
                bookings = bookings
            )
        } catch (e: Exception) {
            HomeDomainState(error = e.message)
        }
    }

    private inline fun <reified T> parseElement(element: JsonElement?): T? {
        if (element == null) return null
        val response = json.decodeFromJsonElement<TrpcResponse<T>>(element)
        return response.result?.data?.json
    }
}
