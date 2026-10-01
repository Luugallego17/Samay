package com.samay.app.ui.therapy

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.samay.app.audio.AudioPlayer
import com.samay.app.audio.KitAudioResult
import com.samay.app.data.content.PublicDomainContent
import com.samay.app.data.kit.Kit
import com.samay.app.data.kit.KitRepository
import com.samay.app.data.kit.KitType
import com.samay.app.ui.theme.SamayButton
import com.samay.app.ui.theme.SamayCream
import com.samay.app.ui.theme.SamayForest
import com.samay.app.ui.theme.SamayMuted

// TODO(P5): reemplazar por el Contact real de Belén apenas esté listo
private const val TRUSTED_PERSON_NAME = "Ana"
private const val TRUSTED_PERSON_PHONE = "+59170000000"

// Kit por defecto cuando el usuario todavía no armó ninguno (D3, #24)
private val DEFAULT_KIT = Kit(
    id = 0,
    type = KitType.MUSIC,
    contentId = "rain",
    title = "Lluvia"
)

fun callTrustedPerson(context: Context, phone: String = TRUSTED_PERSON_PHONE) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    context.startActivity(intent)
}

@Composable
fun TherapyRoute(
    kitRepository: KitRepository,
    onExit: () -> Unit,
    onSessionEnd: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val audioPlayer = remember { AudioPlayer(context) }
    val controller = remember { TherapyController(audioPlayer) }
    val uiState by controller.state.collectAsState()
    val activeKit by kitRepository.observeActiveKit().collectAsState(initial = null)

    LaunchedEffect(activeKit) {
        if (uiState.sessionState == TherapySessionState.IDLE) {
            val kit = activeKit ?: DEFAULT_KIT
            controller.start(scope, kit)
        }
    }

    LaunchedEffect(uiState.sessionState) {
        if (uiState.sessionState == TherapySessionState.ENDING) onSessionEnd()
    }

    DisposableEffect(Unit) {
        onDispose { controller.release() }
    }

    TherapyScreen(
        state = uiState,
        onCallPerson = { callTrustedPerson(context) },
        onExit = {
            controller.endSession()
            onExit()
        }
    )
}

@Composable
fun TherapyScreen(
    state: TherapyUiState,
    onCallPerson: () -> Unit,
    onExit: () -> Unit
) {
    val context = LocalContext.current
    // Se carga una sola vez por composición, no en cada recomposición
    val poems = remember { PublicDomainContent.poems(context) }
    val poemText = remember(state.kit?.contentId) {
        if (state.kit?.type == KitType.POEM) {
            poems.firstOrNull { it.id == state.kit.contentId }?.text
        } else {
            null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SamayCream)
            .padding(24.dp)
    ) {
        IconButton(onClick = onExit, modifier = Modifier.align(Alignment.TopStart)) {
            Text(text = "✕", style = MaterialTheme.typography.titleLarge, color = SamayForest)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BreathCircle(phase = state.phase)

            Text(
                text = phaseLabel(state.phase),
                style = MaterialTheme.typography.headlineSmall,
                color = SamayForest,
                modifier = Modifier.padding(top = 24.dp)
            )

            Text(
                text = formatTime(state.remainingSeconds),
                style = MaterialTheme.typography.bodyMedium,
                color = SamayMuted,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (state.audioResult == KitAudioResult.MISSING) {
                Text(
                    text = "No encontramos el audio del kit — seguí respirando, el ejercicio sigue igual.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SamayMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            if (poemText != null) {
                Text(
                    text = poemText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = SamayForest,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)
                )
            }

            state.kit?.title?.takeIf { it.isNotBlank() }?.let { title ->
                Text(
                    text = "Kit: $title",
                    style = MaterialTheme.typography.bodySmall,
                    color = SamayMuted,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            SamayButton(
                text = "Llamar a mi persona ($TRUSTED_PERSON_NAME)",
                onClick = onCallPerson,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            )
        }
    }
}

@Composable
private fun BreathCircle(phase: BreathPhase) {
    val scale = remember { Animatable(0.6f) }

    LaunchedEffect(phase) {
        val target = when (phase) {
            BreathPhase.INHALE -> 1f
            BreathPhase.HOLD -> 1f
            BreathPhase.EXHALE -> 0.6f
        }
        val durationMs = when (phase) {
            BreathPhase.INHALE -> TherapyController.INHALE_MS
            BreathPhase.HOLD -> 0L
            BreathPhase.EXHALE -> TherapyController.EXHALE_MS
        }
        scale.animateTo(target, animationSpec = tween(durationMillis = durationMs.toInt()))
    }

    Box(
        modifier = Modifier.size(220.dp).padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(220.dp * scale.value)
                .background(SamayForest.copy(alpha = 0.85f), CircleShape)
        )
    }
}

private fun phaseLabel(phase: BreathPhase): String = when (phase) {
    BreathPhase.INHALE -> "Inhalá"
    BreathPhase.HOLD -> "Sostené"
    BreathPhase.EXHALE -> "Exhalá"
}

private fun formatTime(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "%d:%02d".format(m, s)
}

@Composable
fun TherapyEndScreen(onFeedback: (TherapyFeedback) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SamayCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¿Cómo te sentís ahora?",
            style = MaterialTheme.typography.headlineSmall,
            color = SamayForest,
            textAlign = TextAlign.Center
        )
        SamayButton(text = "Mejor", onClick = { onFeedback(TherapyFeedback.BETTER) }, modifier = Modifier.padding(top = 32.dp))
        SamayButton(text = "Igual", onClick = { onFeedback(TherapyFeedback.SAME) }, modifier = Modifier.padding(top = 12.dp))
        SamayButton(text = "Necesito más ayuda", onClick = { onFeedback(TherapyFeedback.NEED_MORE_HELP) }, modifier = Modifier.padding(top = 12.dp))
    }
}