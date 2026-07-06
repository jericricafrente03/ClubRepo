package com.android.clubserve.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.android.clubserve.ui.home.HomePage
import com.android.clubserve.ui.login.LoginPage
import com.android.clubserve.ui.splash.SplashPage

@Composable
fun NavGraph() {
    val backStack = rememberNavBackStack(Screen.Splash)
    
    val provider = entryProvider<NavKey> {
        addEntryProvider(Screen.Splash) {
            SplashPage(onNext = {
                backStack.clear()
                backStack.add(Screen.Home)
            })
        }
        addEntryProvider(Screen.Login) {
            LoginPage()
        }
        addEntryProvider(Screen.Home) {
            HomePage()
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { 
            if (backStack.size > 1) {
                backStack.removeAt(backStack.size - 1)
            }
        },
        entryProvider = provider
    )
}
