package com.android.clubserve.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Splash : Screen
    
    @Serializable
    data object Login : Screen

    @Serializable
    data object Home : Screen
}
