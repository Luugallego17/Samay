package com.samay.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samay.app.data.kit.FakeKitRepository
import com.samay.app.data.kit.KitRepository
import com.samay.app.ui.therapy.TherapyEndScreen
import com.samay.app.ui.therapy.TherapyFeedback
import com.samay.app.ui.therapy.TherapyRoute

@Composable
fun SamayNavHost(
    navController: NavHostController = rememberNavController(),
    onboardingDone: Boolean = false
) {
    val start = if (onboardingDone) Screen.Home.route else Screen.Welcome.route
    val kitRepository: KitRepository = remember { FakeKitRepository() }

    NavHost(navController = navController, startDestination = start) {

        composable(Screen.Welcome.route) {
            PlaceholderScreen("Welcome", "P3", onNext = { navController.navigate(Screen.LangSelect.route) })
        }
        composable(Screen.LangSelect.route) {
            PlaceholderScreen("Language", "P3", onNext = { navController.navigate(Screen.KitChoose.route) })
        }
        composable(Screen.KitChoose.route) {
            PlaceholderScreen("Choose kit", "P3", onNext = { navController.navigate(Screen.Contact.route) })
        }
        composable(Screen.KitVoice.route) { PlaceholderScreen("Kit: Voice", "P3") }
        composable(Screen.KitPoems.route) { PlaceholderScreen("Kit: Poems", "P3") }
        composable(Screen.KitMusic.route) { PlaceholderScreen("Kit: Music", "P3") }
        composable(Screen.Contact.route) {
            PlaceholderScreen("Trusted person", "P5", onNext = { navController.navigate(Screen.CrisisConfirm.route) })
        }
        composable(Screen.CrisisConfirm.route) {
            PlaceholderScreen("Crisis line / country", "P5", onNext = { navController.navigate(Screen.ConfirmReady.route) })
        }
        composable(Screen.ConfirmReady.route) {
            PlaceholderScreen("Your kit is ready", "P3", onNext = { navController.navigate(Screen.Home.route) }, nextLabel = "Go to app")
        }

        composable(Screen.Home.route) {
            PlaceholderScreen("Home", "P4", onNext = { navController.navigate(Screen.Therapy.route) }, nextLabel = "Therapy Mode")
        }

        composable(Screen.Therapy.route) {
            TherapyRoute(
                kitRepository = kitRepository,
                onExit = { navController.popBackStack() },
                onSessionEnd = { navController.navigate(Screen.TherapyEnd.route) }
            )
        }

        composable(Screen.TherapyEnd.route) {
            TherapyEndScreen(
                onFeedback = { feedback ->
                    if (feedback == TherapyFeedback.NEED_MORE_HELP) {
                        navController.navigate(Screen.Help.route)
                    } else {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    }
                }
            )
        }

        composable(Screen.Help.route) { PlaceholderScreen("You're not alone", "P5") }
        composable(Screen.Settings.route) { PlaceholderScreen("Settings", "P1") }
        composable(Screen.Paywall.route) { PlaceholderScreen("Plans / Premium", "P6") }
        composable(Screen.Crisis.route) { PlaceholderScreen("Crisis", "P5") }
    }
}