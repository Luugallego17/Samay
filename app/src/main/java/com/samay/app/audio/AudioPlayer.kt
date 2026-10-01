package com.samay.app.audio

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
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
        if (path.isNullOrBlank()) {
            Log.w(TAG, "playFile: path vacío")
            return KitAudioResult.MISSING
        }
        val file = File(path)
        if (!file.exists()) {
            Log.w(TAG, "playFile: el archivo no existe: $path")
            return KitAudioResult.MISSING
        }
        Log.d(TAG, "playFile: $path (${file.length()} bytes)")
        return start(uri = Uri.fromFile(file).toString(), loop = false)
    }

    private fun start(uri: String, loop: Boolean): KitAudioResult {
        release()
        val attrs = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            setAudioAttributes(attrs, true)
            volume = 1f
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    Log.e(TAG, "ExoPlayer error: ${error.errorCodeName}", error)
                }

                override fun onPlaybackStateChanged(state: Int) {
                    Log.d(TAG, "estado=$state (1=IDLE 2=BUFFERING 3=READY 4=ENDED)")
                }
            })
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
        private const val TAG = "AudioPlayer"
        private const val POEM_AMBIENT_DEFAULT = "rain"
    }
}