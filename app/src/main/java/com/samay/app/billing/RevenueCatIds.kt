package com.samay.app.billing

/**
 * IDs alineados a F3 (#35). Deben coincidir 1:1 con RevenueCat (y Play si existe).
 * Sin Play Store: crear el producto en **Test Store** con el mismo productId.
 */
object RevenueCatIds {
    const val ENTITLEMENT_PREMIUM = "premium"
    const val OFFERING_DEFAULT = "default"
    const val PRODUCT_PREMIUM_QUARTERLY = "samay_premium_quarterly"
}
