package com.samay.app.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File

enum class RecordingResult { STARTED, FAILED_BUSY, FAILED_STORAGE, FAILED_UNKNOWN }

/** Graba la voz de la persona de confianza con MediaRecorder. Siempre local, nunca se sube. */
class VoiceRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    fun start(): RecordingResult {
        // Si quedó un recorder anterior colgado, lo limpiamos antes de empezar.
        releaseQuietly()

        val file = File(context.filesDir, "trusted_voice_${System.currentTimeMillis()}.m4a")
        val mr = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
        return try {
            mr.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioChannels(1)
                setAudioSamplingRate(44100)
                setAudioEncodingBitRate(128000)
                setOutputFile(file.absolutePath)
                prepare()
                start()
            }
            recorder = mr
            outputFile = file
            RecordingResult.STARTED
        } catch (e: java.io.IOException) {
            Log.e(TAG, "start() IOException", e)
            cleanupFailedStart(mr, file)
            RecordingResult.FAILED_STORAGE
        } catch (e: IllegalStateException) {
            Log.e(TAG, "start() IllegalStateException", e)
            cleanupFailedStart(mr, file)
            RecordingResult.FAILED_BUSY
        } catch (e: Exception) {
            Log.e(TAG, "start() fallo inesperado", e)
            cleanupFailedStart(mr, file)
            RecordingResult.FAILED_UNKNOWN
        }
    }

    fun stop(): String? {
        return try {
            recorder?.apply { stop(); release() }
            recorder = null
            outputFile?.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "stop() fallo", e)
            releaseQuietly()
            outputFile?.delete()
            outputFile = null
            null
        }
    }

    fun cancel() {
        try {
            recorder?.apply { stop(); release() }
        } catch (_: Exception) {
        } finally {
            recorder = null
            outputFile?.delete()
            outputFile = null
        }
    }

    private fun cleanupFailedStart(mr: MediaRecorder, file: File) {
        try { mr.release() } catch (_: Exception) {}
        file.delete()
        recorder = null
        outputFile = null
    }

    private fun releaseQuietly() {
        try { recorder?.release() } catch (_: Exception) {}
        recorder = null
    }

    private companion object {
        const val TAG = "VoiceRecorder"
    }
}