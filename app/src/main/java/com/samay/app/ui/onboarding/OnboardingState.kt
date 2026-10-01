package com.samay.app.ui.onboarding

import com.samay.app.data.kit.Kit
import com.samay.app.data.kit.KitRepository
import com.samay.app.data.kit.KitType
import com.samay.app.data.prefs.OnboardingPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Pasos del onboarding (locales a esta vertical; NO son las rutas del NavHost de P2). */
enum class OnboardingStep { WELCOME, LANGUAGE, KIT_CHOOSE, KIT_CONTENT, VOICE, CONFIRM }

data class OnboardingState(
    val step: OnboardingStep = OnboardingStep.WELCOME,
    val language: String = "es",
    val kitType: KitType? = null,
    val selectedContentId: String? = null,
    val selectedTitle: String = "",
    val voiceFilePath: String? = null,
    val saving: Boolean = false,
    val error: String? = null
)

/**
 * Controlador de estado del onboarding. Clase Kotlin simple (sin AndroidX ViewModel)
 * para no depender de más librerías. P2 puede montar OnboardingFlow como un destino
 * del NavHost cuando A3 esté listo.
 */
class OnboardingController {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    /** Secuencia de pasos según el tipo de kit elegido. */
    private fun sequence(kitType: KitType?): List<OnboardingStep> {
        val content = if (kitType == KitType.VOICE) OnboardingStep.VOICE else OnboardingStep.KIT_CONTENT
        return listOf(
            OnboardingStep.WELCOME,
            OnboardingStep.LANGUAGE,
            OnboardingStep.KIT_CHOOSE,
            content,
            OnboardingStep.CONFIRM
        )
    }

    fun setLanguage(code: String) { _state.value = _state.value.copy(language = code) }

    fun setKitType(type: KitType) {
        _state.value = _state.value.copy(kitType = type, selectedContentId = null, selectedTitle = "")
    }

    fun selectContent(id: String, title: String) {
        _state.value = _state.value.copy(selectedContentId = id, selectedTitle = title)
    }

    fun setVoiceFilePath(path: String) {
        _state.value = _state.value.copy(voiceFilePath = path)
    }

    fun next() {
        val s = _state.value
        val seq = sequence(s.kitType)
        val i = seq.indexOf(s.step).coerceAtLeast(0)
        if (i < seq.lastIndex) _state.value = s.copy(step = seq[i + 1])
    }

    fun back(): Boolean {
        val s = _state.value
        val seq = sequence(s.kitType)
        val i = seq.indexOf(s.step).coerceAtLeast(0)
        return if (i > 0) { _state.value = s.copy(step = seq[i - 1]); true } else false
    }

    /** True si el paso actual permite avanzar. */
    fun canAdvance(): Boolean {
        val s = _state.value
        return when (s.step) {
            OnboardingStep.KIT_CHOOSE -> s.kitType != null
            OnboardingStep.KIT_CONTENT -> s.selectedContentId != null
            else -> true
        }
    }

    /** Construye el Kit del estado actual (Plan B: default lluvia si no eligió nada). */
    fun buildKit(): Kit {
        val s = _state.value
        return when (s.kitType) {
            KitType.POEM -> Kit(type = KitType.POEM, contentId = s.selectedContentId, title = s.selectedTitle)
            KitType.MUSIC -> Kit(type = KitType.MUSIC, contentId = s.selectedContentId ?: "rain", title = s.selectedTitle.ifBlank { "Lluvia" })
            KitType.VOICE -> Kit(type = KitType.VOICE, voiceFilePath = s.voiceFilePath, title = s.selectedTitle.ifBlank { "Voz de mi persona" })
            null -> Kit(type = KitType.MUSIC, contentId = "rain", title = "Lluvia")
        }
    }

    /** Persiste el kit (C5) y marca onboarding terminado + idioma. */
    suspend fun persist(kitRepo: KitRepository, prefs: OnboardingPrefs) {
        _state.value = _state.value.copy(saving = true, error = null)
        try {
            kitRepo.saveKit(buildKit())
            prefs.setLanguage(_state.value.language)
            prefs.setOnboardingDone(true)
            _state.value = _state.value.copy(saving = false)
        } catch (e: Exception) {
            _state.value = _state.value.copy(saving = false, error = e.message ?: "No se pudo guardar el kit")
        }
    }
}
