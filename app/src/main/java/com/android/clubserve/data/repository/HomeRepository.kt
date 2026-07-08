package com.android.clubserve.data.repository

import android.util.Log
import com.android.clubserve.data.remote.api.ClubServeApi
import com.android.clubserve.data.remote.dto.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.*

data class HomeDomainState(
    val categories: List<MainCategoryDto> = emptyList(),
    val subCategories: List<SubCategoryDto> = emptyList(),
    val groups: List<GroupDto> = emptyList(),
    val lovedByLocals: List<SubCategoryDto> = emptyList(),
    val bookings: List<BookingDto> = emptyList(),
    val location: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeRepository(private val api: ClubServeApi) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getHomeData(): Flow<HomeDomainState> = flow {
        emit(HomeDomainState(isLoading = true))
        try {
            val elements = api.getHomeData()
            
            // Index 0: mainCategory.listAll
            val categories = parseElement<List<MainCategoryDto>>(elements.getOrNull(0))
                ?.map { category ->
                    category.copy(
                        subCategories = category.subCategories?.filter { it.status == "ACTIVE" }
                    )
                } ?: emptyList()
            
            // Index 1: subCategory.list
            val subCategories = parseElement<List<SubCategoryDto>>(elements.getOrNull(1))
                ?.filter { it.status == "ACTIVE" }
                ?.groupBy { it.subCategoryName }
                ?.map { it.value.first() }   // keep first occurrence of each name, preserving array order
                ?.take(10)
                ?: emptyList()
            
            // Index 2: groups.list
            val groups = parseElement<List<GroupDto>>(elements.getOrNull(2)) ?: emptyList()
            
            // Index 3: subCategory.lovedByLocals (The 4th result)
            val lovedByLocalsResponse = parseElement<LovedByLocalsResponse>(elements.getOrNull(3))
            val lovedByLocals = lovedByLocalsResponse?.data?.filter { it.status == "ACTIVE" } ?: emptyList()
            
            // Index 4: bookings.getUserUpcomingBookings
            val bookings = parseElement<List<BookingDto>>(elements.getOrNull(4)) ?: emptyList()
            emit(HomeDomainState(
                categories = categories,
                subCategories = subCategories,
                groups = groups,
                lovedByLocals = lovedByLocals,
                bookings = bookings,
                isLoading = false
            ))
        } catch (e: Exception) {
            Log.e("HomeRepository", "Error fetching home data", e)
            emit(HomeDomainState(error = e.message, isLoading = false))
        }
    }

    private inline fun <reified T> parseElement(element: JsonElement?): T? {
        if (element == null) return null
        return try {
            val response = json.decodeFromJsonElement<TrpcResponse<T>>(element)
            response.result?.data?.json
        } catch (e: Exception) {
            Log.w("HomeRepository", "Failed to parse element: ${e.message}")
            null
        }
    }
}
