package com.android.clubserve.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MainCategoryDto(
    val id: String,
    val categoryName: String,
    val categoryKey: String,
    val image: String? = null,
    val subCategories: List<SubCategoryDto>? = null,
    val businessInfo: BusinessInfoDto? = null
)

@Serializable
data class SubCategoryDto(
    val id: String,
    val subCategoryName: String,
    val subCategoryKey: String,
    val image: String? = null,
    val startingPricePerHour: String? = null,
    val businessInfoName: String? = null,
    val businessInfoUrl: String? = null,
    val mainCategoryName: String? = null,
    val bookingCount: Int? = null,
    val likeCount: Int? = null
)

@Serializable
data class BusinessInfoDto(
    val id: String,
    val name: String,
    val url: String,
    val city: String? = null,
    val address: String? = null,
    val group: GroupDto? = null
)

@Serializable
data class GroupDto(
    val id: String,
    val name: String,
    val image: String? = null,
    val description: String? = null
)

@Serializable
data class LovedByLocalsResponse(
    val data: List<SubCategoryDto>,
    val total: Int
)

@Serializable
data class BookingDto(
    val id: String,
    // Add fields as needed when API is authorized
)
