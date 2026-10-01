package com.samay.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samay.app.ui.paywall.PaywallScreen
import com.samay.app.data.AppDatabase
import com.samay.app.data.content.PublicDomainContent
import com.samay.app.data.kit.KitType
import com.samay.app.data.kit.RoomKitRepository
import com.samay.app.data.prefs.OnboardingPrefs
import com.samay.app.ui.onboarding.ConfirmStep
import com.samay.app.ui.onboarding.KitChooseStep
import com.samay.app.ui.onboarding.KitContentStep
import com.samay.app.ui.onboarding.LanguageStep
import com.samay.app.ui.onboarding.OnboardingController
import com.samay.app.ui.onboarding.VoiceStep
import com.samay.app.ui.onboarding.WelcomeStep
import kotlinx.coroutines.launch
import com.samay.app.ui.therapy.TherapyEndScreen
import com.samay.app.ui.therapy.TherapyFeedback
import com.samay.app.ui.therapy.TherapyRoute

@Composable
fun SamayNavHost(
    navController: NavHostController = rememberNavController(),
    onboardingDone: Boolean = false
) {
    val start = if (onboardingDone) Screen.Home.route else Screen.Welcome.route

    // Estado de onboarding compartido entre las rutas del flujo (P3).
    val onboarding = remember { OnboardingController() }
    val context = LocalContext.current
    val kitRepo = remember { RoomKitRepository(AppDatabase.get(context).kitDao()) }
    val contactRepo = remember { com.samay.app.data.contact.ContactRepository(com.samay.app.data.AppDatabase.get(context).contactDao()) }
    val prefs = remember { OnboardingPrefs(context) }
    val content = remember { PublicDomainContent.load(context) }
    val scope = rememberCoroutineScope()
    var showPromoDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    if (showPromoDialog) {
        com.samay.app.ui.paywall.PromoCodeDialog(onDismiss = { showPromoDialog = false })
    }

    NavHost(navController = navController, startDestination = start) {

        // ---------- Onboarding (P3: pantallas reales; Contact/CrisisConfirm = P5) ----------
        composable(Screen.Welcome.route) {
            WelcomeStep(onStart = { navController.navigate(Screen.LangSelect.route) }, onPromoClick = { showPromoDialog = true })
        }
        composable(Screen.LangSelect.route) {
            val s by onboarding.state.collectAsState()
            LanguageStep(
                selected = s.language,
                onSelect = onboarding::setLanguage,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(Screen.KitChoose.route) }
            )
        }
        composable(Screen.KitChoose.route) {
            val s by onboarding.state.collectAsState()
            KitChooseStep(
                selected = s.kitType,
                onSelect = onboarding::setKitType,
                canAdvance = s.kitType != null,
                onBack = { navController.popBackStack() },
                onNext = {
                    val dest = when (s.kitType) {
                        KitType.VOICE -> Screen.KitVoice.route
                        KitType.MUSIC -> Screen.KitMusic.route
                        else -> Screen.KitPoems.route
                    }
                    navController.navigate(dest)
                }
            )
        }
        composable(Screen.KitPoems.route) {
            val s by onboarding.state.collectAsState()
            KitContentStep(
                items = content.filter { it.type == KitType.POEM },
                selectedId = s.selectedContentId,
                onSelect = { onboarding.selectContent(it.id, it.title) },
                canAdvance = s.selectedContentId != null,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(Screen.Contact.route) }
            )
        }
        composable(Screen.KitMusic.route) {
            val s by onboarding.state.collectAsState()
            KitContentStep(
                items = content.filter { it.type == KitType.MUSIC },
                selectedId = s.selectedContentId,
                onSelect = { onboarding.selectContent(it.id, it.title) },
                canAdvance = s.selectedContentId != null,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(Screen.Contact.route) }
            )
        }
        composable(Screen.KitVoice.route) {
            VoiceStep(
                onVoiceRecorded = onboarding::setVoiceFilePath,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(Screen.Contact.route) }
            )
        }

        // Contact y CrisisConfirm los implementa P5 (E1/E2); por ahora placeholders.
        composable(Screen.Contact.route) {
            val contact by contactRepo.contact.collectAsState(initial = null)
            com.samay.app.ui.contact.ContactStep(
                initialName = contact?.name ?: "",
                initialPhone = contact?.phone ?: "",
                onBack = { navController.popBackStack() },
                onSkip = {
                    scope.launch { contactRepo.clearContact() }
                    navController.navigate(Screen.CrisisConfirm.route)
                },
                onNext = { name, phone ->
                    scope.launch { contactRepo.saveContact(name, phone) }
                    navController.navigate(Screen.CrisisConfirm.route)
                }
            )
        }
        composable(Screen.CrisisConfirm.route) {
            val currentCountry by prefs.countryCode.collectAsState(initial = null)
            com.samay.app.ui.crisis.CrisisConfirmStep(
                selectedCountry = currentCountry,
                onSelect = { code -> scope.launch { prefs.setCountryCode(code) } },
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(Screen.ConfirmReady.route) }
            )
        }

        composable(Screen.ConfirmReady.route) {
            val s by onboarding.state.collectAsState()
            val contact by contactRepo.contact.collectAsState(initial = null)
            val currentCountry by prefs.countryCode.collectAsState(initial = null)
            val crisisLine = com.samay.app.data.crisis.CrisisLines.getByCode(currentCountry)
            
            ConfirmStep(
                contactSummary = contact?.name ?: "No configurada",
                crisisSummary = "${crisisLine.countryName} - ${crisisLine.number}",
                kitTitle = s.selectedTitle.ifBlank {
                    when (s.kitType) {
                        KitType.MUSIC -> "Lluvia"
                        KitType.VOICE -> "Voz de mi persona"
                        else -> "Kit de calma"
                    }
                },
                saving = s.saving,
                error = s.error,
                onBack = { navController.popBackStack() },
                onFinish = {
                    scope.launch {
                        onboarding.persist(kitRepo, prefs)
                        if (onboarding.state.value.error == null) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Welcome.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        // ---------- Main app (dueños de cada pantalla) ----------
        composable(Screen.Home.route) {
            com.samay.app.ui.home.HomeScreen(
                onTherapyClick = { navController.navigate(Screen.Therapy.route) },
                onCrisisClick = { navController.navigate(Screen.Crisis.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onPremiumClick = { navController.navigate(Screen.Paywall.route) }
            )
        }
        composable(Screen.Therapy.route) {
            TherapyRoute(
                kitRepository = kitRepo,
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
        composable(Screen.Help.route) {
            PlaceholderScreen("You're not alone", "P5")
        }
        composable(Screen.Settings.route) {
            PlaceholderScreen("Settings", "P1")
        }
        composable(Screen.Paywall.route) {
            PaywallScreen(
                onPromoCodeClick = { showPromoDialog = true },
                onClose = { navController.popBackStack() }
            )
        }
        composable(Screen.Crisis.route) {
            val currentCountry by prefs.countryCode.collectAsState(initial = null)
            com.samay.app.ui.crisis.CrisisScreen(
                countryCode = currentCountry,
                onBack = { navController.popBackStack() }
            )
        }
    }
}