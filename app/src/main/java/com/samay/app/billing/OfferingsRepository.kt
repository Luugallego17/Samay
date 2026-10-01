package com.samay.app.billing

import android.util.Log
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.getOfferingsWith

/**
 * F3 (#35): llama getOfferings() y deja el resultado en Logcat (`SamayRC`).
 *
 * DoD: Offering `default` con producto `samay_premium_quarterly` (Play o Test Store).
 */
object OfferingsRepository {
    private const val TAG = "SamayRC"

    fun fetchAndLogOfferings() {
        if (!Purchases.isConfigured) {
            Log.w(TAG, "Purchases no configurado — revisá revenuecat.apiKey en local.properties")
            return
        }

        Purchases.sharedInstance.getOfferingsWith(
            onError = { error: PurchasesError ->
                Log.e(TAG, "getOfferings ERROR: ${error.code} — ${error.message}")
                Log.e(
                    TAG,
                    "F3 pendiente en dashboard: entitlement '${RevenueCatIds.ENTITLEMENT_PREMIUM}', " +
                        "Offering '${RevenueCatIds.OFFERING_DEFAULT}', " +
                        "producto '${RevenueCatIds.PRODUCT_PREMIUM_QUARTERLY}'"
                )
            },
            onSuccess = { offerings: Offerings ->
                logOfferings(offerings)
            }
        )
    }

    private fun logOfferings(offerings: Offerings) {
        val current = offerings.current
        val allIds = offerings.all.keys.joinToString()
        Log.i(TAG, "Offerings all=[$allIds] current=${current?.identifier}")

        if (current == null) {
            Log.w(
                TAG,
                "F3 INCOMPLETE: sin Offering current. En RC creá Offering " +
                    "'${RevenueCatIds.OFFERING_DEFAULT}' y marcalo Current."
            )
            return
        }

        val packages = current.availablePackages
        if (packages.isEmpty()) {
            Log.w(
                TAG,
                "F3 INCOMPLETE: Offering '${current.identifier}' vacío. " +
                    "Agregá '${RevenueCatIds.PRODUCT_PREMIUM_QUARTERLY}' (Test Store si no hay Play)."
            )
            return
        }

        packages.forEach { pkg ->
            Log.i(
                TAG,
                "package=${pkg.identifier} productId=${pkg.product.id} price=${pkg.product.price.formatted}"
            )
        }

        val hasQuarterly = packages.any { it.product.id == RevenueCatIds.PRODUCT_PREMIUM_QUARTERLY }
        if (hasQuarterly) {
            Log.i(
                TAG,
                "F3 OK: getOfferings() trae '${RevenueCatIds.PRODUCT_PREMIUM_QUARTERLY}' " +
                    "en '${current.identifier}'. Entitlement a usar en F4: " +
                    "'${RevenueCatIds.ENTITLEMENT_PREMIUM}'."
            )
        } else {
            Log.w(
                TAG,
                "F3 INCOMPLETE: falta productId '${RevenueCatIds.PRODUCT_PREMIUM_QUARTERLY}'. " +
                    "Vistos: ${packages.map { it.product.id }}"
            )
        }
    }
}
