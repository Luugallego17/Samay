package com.samay.app.audio

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.samay.app.data.kit.Kit
import com.samay.app.data.kit.KitType
import java.io.File

enum class KitAudioResult { PLAYING, NOT_APPLICABLE, MISSING }

class AudioPlayer(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null

    fun playKit(kit: Kit): KitAudioResult = when (kit.type) {
        // TODO(P4): hardcodeado por ahora — cuando el equipo defina cómo elegir el
        // ambiente de un poema (depende de UI en onboarding, coordinar con Luna),
        // reemplazar POEMA_AMBIENT_DEFAULT por kit.ambientContentId o similar.
        KitType.POEM -> playRaw(POEM_AMBIENT_DEFAULT)
        KitType.MUSIC -> playRaw(kit.contentId)
        KitType.VOICE -> playFile(kit.voiceFilePath)
    }

    private fun playRaw(contentId: String?): KitAudioResult {
        val resName = contentId?.takeIf { it.isNotBlank() } ?: return KitAudioResult.MISSING
        val resId = context.resources.getIdentifier(resName, "raw", context.packageName)
        if (resId == 0) return KitAudioResult.MISSING
        return start(uri = "android.resource://${context.packageName}/$resId", loop = true)
    }

    private fun playFile(path: String?): KitAudioResult {
        if (path.isNullOrBlank()) return KitAudioResult.MISSING
        val file = File(path)
        if (!file.exists()) return KitAudioResult.MISSING
        return start(uri = Uri.fromFile(file).toString(), loop = false)
    }

    private fun start(uri: String, loop: Boolean): KitAudioResult {
        release()
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = if (loop) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
            prepare()
            play()
        }
        return KitAudioResult.PLAYING
    }

    fun pause() { exoPlayer?.pause() }
    fun resume() { exoPlayer?.play() }
    fun stop() { exoPlayer?.stop() }
    fun release() { exoPlayer?.release(); exoPlayer = null }

    companion object {
        private const val POEM_AMBIENT_DEFAULT = "rain"
    }
}