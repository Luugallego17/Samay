package com.samay.app

import android.app.Application
import android.content.pm.ApplicationInfo
import android.util.Log
import com.revenuecat.purchases.LogLevel
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.samay.app.billing.OfferingsRepository
import io.sentry.android.core.SentryAndroid

/**
 * Arranque de la app (no es una pantalla).
 * La UI est� en MainActivity. Ac� se configura RevenueCat y Sentry.
 */
class SamayApp : Application() {
    override fun onCreate() {
        super.onCreate()
        configureSentry()
        configureRevenueCat()
    }

    private fun configureSentry() {
        val sentryDsn = BuildConfig.SENTRY_DSN
        if (sentryDsn.isNotBlank()) {
            SentryAndroid.init(this) { options ->
                options.dsn = sentryDsn
                // Habilitamos sample rate de rastreos para debug/performance (F6)
                options.tracesSampleRate = 1.0
            }
            Log.i("SamayApp", "Sentry inicializado (DSN configurado)")
        } else {
            Log.w("SamayApp", "Sin sentry.dsn en local.properties - Sentry desactivado")
        }
    }

    private fun configureRevenueCat() {
        val apiKey = BuildConfig.REVENUECAT_API_KEY
        if (apiKey.isBlank()) {
            Log.w("SamayApp", "Sin revenuecat.apiKey en local.properties - RC desactivado")
            return
        }

        val isDebug = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        Purchases.logLevel = if (isDebug) LogLevel.DEBUG else LogLevel.INFO
        Purchases.configure(
            PurchasesConfiguration.Builder(this, apiKey).build()
        )
        Log.i("SamayApp", "RevenueCat configurado")

        // F3 (#35): prueba getOfferings al arrancar
        OfferingsRepository.fetchAndLogOfferings()
    }

    private fun configureOneSignal() {
        val appId = BuildConfig.ONESIGNAL_APP_ID
        if (appId.isNotBlank()) {
            OneSignal.initWithContext(this, appId)
            CoroutineScope(Dispatchers.IO).launch {
                OneSignal.Notifications.requestPermission(false)
            }
            Log.i("SamayApp", "OneSignal inicializado")
        } else {
            Log.w("SamayApp", "Sin onesignal.appId en local.properties - OneSignal desactivado")
        }
    }
}

