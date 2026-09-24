package com.samay.app.ui.therapy

import com.samay.app.audio.AudioPlayer
import com.samay.app.audio.KitAudioResult
import com.samay.app.data.kit.Kit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class BreathPhase { INHALE, HOLD, EXHALE }
enum class TherapySessionState { IDLE, RUNNING, ENDING }
enum class TherapyFeedback { BETTER, SAME, NEED_MORE_HELP }

data class TherapyUiState(
    val sessionState: TherapySessionState = TherapySessionState.IDLE,
    val phase: BreathPhase = BreathPhase.INHALE,
    val remainingSeconds: Int = TherapyController.DEFAULT_DURATION_SECONDS,
    val kit: Kit? = null,
    val audioResult: KitAudioResult? = null
)

class TherapyController(
    private val audioPlayer: AudioPlayer,
    requestedDurationSeconds: Int = TherapyController.DEFAULT_DURATION_SECONDS
) {
    private val durationSeconds: Int = requestedDurationSeconds.coerceIn(
        MIN_DURATION_SECONDS,
        MAX_DURATION_SECONDS
    )

    private val _state = MutableStateFlow(TherapyUiState(remainingSeconds = durationSeconds))
    val state: StateFlow<TherapyUiState> = _state.asStateFlow()

    private var sessionJob: Job? = null

    fun start(scope: CoroutineScope, kit: Kit) {
        if (_state.value.sessionState == TherapySessionState.RUNNING) return

        val audioResult = audioPlayer.playKit(kit)
        _state.value = _state.value.copy(
            sessionState = TherapySessionState.RUNNING,
            kit = kit,
            audioResult = audioResult,
            remainingSeconds = durationSeconds
        )

        sessionJob = scope.launch {
            launch { runBreathCycle() }
            runCountdown()
        }
    }

    private suspend fun runCountdown() {
        while (_state.value.remainingSeconds > 0 && _state.value.sessionState == TherapySessionState.RUNNING) {
            delay(1000)
            if (_state.value.sessionState != TherapySessionState.RUNNING) return
            _state.value = _state.value.copy(remainingSeconds = (_state.value.remainingSeconds - 1).coerceAtLeast(0))
        }
        if (_state.value.sessionState == TherapySessionState.RUNNING) endSession()
    }

    private suspend fun runBreathCycle() {
        while (_state.value.sessionState == TherapySessionState.RUNNING) {
            setPhase(BreathPhase.INHALE, INHALE_MS)
            setPhase(BreathPhase.HOLD, HOLD_MS)
            setPhase(BreathPhase.EXHALE, EXHALE_MS)
        }
    }

    private suspend fun setPhase(phase: BreathPhase, durationMs: Long) {
        if (_state.value.sessionState != TherapySessionState.RUNNING) return
        _state.value = _state.value.copy(phase = phase)
        delay(durationMs)
    }

    fun endSession() {
        sessionJob?.cancel()
        audioPlayer.stop()
        _state.value = _state.value.copy(sessionState = TherapySessionState.ENDING)
    }

    fun release() {
        sessionJob?.cancel()
        audioPlayer.release()
    }

    companion object {
        const val DEFAULT_DURATION_SECONDS = 180
        const val MIN_DURATION_SECONDS = 180
        const val MAX_DURATION_SECONDS = 300
        const val INHALE_MS = 4000L
        const val HOLD_MS = 4000L
        const val EXHALE_MS = 6000L
    }
}