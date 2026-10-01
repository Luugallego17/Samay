package com.samay.app

import android.app.Application
import android.content.pm.ApplicationInfo
import android.util.Log
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.samay.app.billing.OfferingsRepository

/**
 * Arranque de la app (no es una pantalla).
 * La UI está en MainActivity. Acá se configura RevenueCat y se valida F3 (getOfferings).
 */
class SamayApp : Application() {
    override fun onCreate() {
        super.onCreate()
        configureRevenueCat()
    }

    private fun configureRevenueCat() {
        val apiKey = BuildConfig.REVENUECAT_API_KEY
        if (apiKey.isBlank()) {
            Log.w("SamayApp", "Sin revenuecat.apiKey en local.properties — RC desactivado")
            return
        }

        val isDebug = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        Purchases.logLevel = if (isDebug) LogLevel.DEBUG else LogLevel.INFO
        Purchases.configure(
            PurchasesConfiguration.Builder(this, apiKey).build()
        )
        Log.i("SamayApp", "RevenueCat configurado")

        // F3 (#35): prueba getOfferings al arrancar — mirá Logcat tag SamayRC
        OfferingsRepository.fetchAndLogOfferings()
    }
}
