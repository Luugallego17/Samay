package com.samay.app.billing

import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.getCustomerInfoWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Estado Premium (F4 #36). Entitlement [RevenueCatIds.ENTITLEMENT_PREMIUM].
 */
object PremiumRepository {
    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()

    fun refreshFromCustomerInfo(info: CustomerInfo) {
        val active = info.entitlements[RevenueCatIds.ENTITLEMENT_PREMIUM]?.isActive == true
        _isPremium.value = active
        _lastError.value = null
    }

    fun refresh() {
        if (!Purchases.isConfigured) {
            _lastError.value = "RevenueCat no configurado"
            return
        }
        Purchases.sharedInstance.getCustomerInfoWith(
            onError = { error: PurchasesError ->
                _lastError.value = error.message
            },
            onSuccess = { info ->
                refreshFromCustomerInfo(info)
            }
        )
    }

    fun simulatePurchaseSuccess() {
        _isPremium.value = true
        _lastError.value = null
    }

    fun setError(message: String?) {
        _lastError.value = message
    }
}
