package com.you.framenotes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.you.framenotes.ui.detail.DetailScreen
import com.you.framenotes.ui.insights.InsightsScreen
import com.you.framenotes.ui.library.LibraryScreen
import com.you.framenotes.ui.privacy.PrivacyScreen

@Composable
fun FrameNotesNav(nav: NavHostController = rememberNavController()) {
    NavHost(navController = nav, startDestination = "library") {
        composable("library") { 
            LibraryScreen(
                onOpen = { id -> nav.navigate("detail/$id") },
                onInsights = { nav.navigate("insights") },
                onPrivacy = { nav.navigate("privacy") }
            ) 
        }
        composable("detail/{photoId}") { DetailScreen(onBack = { nav.popBackStack() }) }
        composable("insights") { InsightsScreen(onBack = { nav.popBackStack() }) }
        composable("privacy") { PrivacyScreen(onBack = { nav.popBackStack() }) }
    }
}
