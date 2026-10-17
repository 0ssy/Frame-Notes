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
