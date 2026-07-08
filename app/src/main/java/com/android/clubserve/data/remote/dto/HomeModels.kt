package com.android.clubserve.data.remote.dto

import kotlinx.serialization.Serializable
import com.android.clubserve.data.remote.utils.ImageUtils

@Serializable
data class MainCategoryDto(
    val id: String,
    val categoryName: String,
    val categoryKey: String,
    val image: String? = null,
    val subCategories: List<SubCategoryDto>? = null,
    val businessInfo: BusinessInfoDto? = null
) {
    val fullImageUrl: String? get() = ImageUtils.getFullImageUrl(image)
}

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
    val likeCount: Int? = null,
    val status: String? = null,
    val hasExtras: Boolean = false
) {
    val fullImageUrl: String? get() = ImageUtils.getFullImageUrl(image)
}

@Serializable
data class BusinessInfoDto(
    val id: String,
    val name: String,
    val url: String,
    val city: String? = null,
    val address: String? = null,
    val logo: String? = null,
    val banner: String? = null,
    val group: GroupDto? = null
) {
    val fullLogoUrl: String? get() = ImageUtils.getFullImageUrl(logo)
    val fullBannerUrl: String? get() = ImageUtils.getFullImageUrl(banner)
}

@Serializable
data class GroupDto(
    val id: String,
    val name: String,
    val image: String? = null,
    val description: String? = null,
    val status: String? = null
) {
    val fullImageUrl: String? get() = ImageUtils.getFullImageUrl(image)
}

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
