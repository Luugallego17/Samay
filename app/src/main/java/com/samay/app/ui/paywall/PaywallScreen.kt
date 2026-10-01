package com.samay.app.ui.paywall

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revenuecat.purchases.Package as RcPackage
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.purchaseWith
import com.revenuecat.purchases.restorePurchasesWith
import com.samay.app.R
import com.samay.app.billing.PremiumRepository
import com.samay.app.billing.RevenueCatIds
import com.samay.app.ui.theme.SamayButton
import com.samay.app.ui.theme.SamayCream
import com.samay.app.ui.theme.SamayForest
import com.samay.app.ui.theme.SamayMuted

private const val TAG = "SamayRC"

/**
 * F4 (#36) — Planes: Free arriba, Premium abajo, promo link (F5), restore, footer seguridad.
 */
@Composable
fun PaywallScreen(
    onPromoCodeClick: () -> Unit = {},
    onClose: (() -> Unit)? = null
) {
    val activity = LocalContext.current as? Activity
    val isPremium by PremiumRepository.isPremium.collectAsState()
    val repoError by PremiumRepository.lastError.collectAsState()

    var loading by remember { mutableStateOf(true) }
    var purchasing by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var premiumPackage by remember { mutableStateOf<RcPackage?>(null) }
    var priceLabel by remember { mutableStateOf("$4.99 / 3 meses") }

    LaunchedEffect(Unit) {
        PremiumRepository.refresh()
        if (!Purchases.isConfigured) {
            loading = false
            statusMessage = "RevenueCat no configurado (local.properties)"
            return@LaunchedEffect
        }
        Purchases.sharedInstance.getOfferingsWith(
            onError = { error: PurchasesError ->
                loading = false
                statusMessage = error.message
                Log.e(TAG, "Paywall offerings error: ${error.message}")
            },
            onSuccess = { offerings ->
                val current = offerings.current
                    ?: offerings[RevenueCatIds.OFFERING_DEFAULT]
                val pkg = current?.availablePackages?.firstOrNull {
                    it.product.id == RevenueCatIds.PRODUCT_PREMIUM_QUARTERLY
                } ?: current?.availablePackages?.firstOrNull()
                premiumPackage = pkg
                if (pkg != null) {
                    priceLabel = pkg.product.price.formatted + " / 3 meses"
                } else {
                    statusMessage = "Sin paquetes en Offering (F3: producto + default)"
                }
                loading = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SamayCream)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.paywall_title),
            color = SamayForest,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.paywall_subtitle),
            color = SamayMuted,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(28.dp))

        // --- Free (arriba) ---
        PlanCard(
            title = stringResource(R.string.paywall_free_title),
            price = stringResource(R.string.paywall_free_price),
            bullets = listOf(
                stringResource(R.string.paywall_free_b1),
                stringResource(R.string.paywall_free_b2),
                stringResource(R.string.paywall_free_b3),
                stringResource(R.string.paywall_free_b4)
            ),
            highlighted = false
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onPromoCodeClick) {
            Text(
                text = stringResource(R.string.paywall_promo_link),
                color = SamayForest,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Premium ---
        PlanCard(
            title = stringResource(R.string.paywall_premium_title),
            price = priceLabel,
            bullets = listOf(
                stringResource(R.string.paywall_premium_b1),
                stringResource(R.string.paywall_premium_b2),
                stringResource(R.string.paywall_premium_b3),
                stringResource(R.string.paywall_premium_b4)
            ),
            highlighted = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isPremium -> {
                Text(
                    text = stringResource(R.string.paywall_already_premium),
                    color = SamayForest,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
            loading || purchasing -> {
                Text(
                    text = if (purchasing) {
                        stringResource(R.string.paywall_purchasing)
                    } else {
                        stringResource(R.string.paywall_fetching)
                    },
                    color = SamayMuted
                )
            }
            else -> {
                                SamayButton(
                    text = stringResource(R.string.paywall_subscribe),
                    enabled = true,
                    onClick = {
                        purchasing = true
                        statusMessage = null
                        
                        if (premiumPackage == null || activity == null) {
                            PremiumRepository.simulatePurchaseSuccess()
                            statusMessage = "Premium activo (Simulado para Open Source)"
                            purchasing = false
                        } else {
                            Purchases.sharedInstance.purchaseWith(
                                PurchaseParams.Builder(activity, premiumPackage!!).build(),
                                onError = { error, userCancelled ->
                                    purchasing = false
                                    if (userCancelled) {
                                        statusMessage = "Compra cancelada"
                                    } else {
                                        PremiumRepository.simulatePurchaseSuccess()
                                        statusMessage = "Premium activo (Simulado para Open Source)"
                                    }
                                },
                                onSuccess = { _, customerInfo ->
                                    purchasing = false
                                    PremiumRepository.refreshFromCustomerInfo(customerInfo)
                                    statusMessage = "Premium activo"
                                }
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        purchasing = true
                        
                        // F4 (Open Source fallback): Mock the restore
                        Purchases.sharedInstance.restorePurchasesWith(
                            onError = { error ->
                                purchasing = false
                                PremiumRepository.simulatePurchaseSuccess()
                                statusMessage = "Compras restauradas (Simulado)"
                            },
                            onSuccess = { info ->
                                purchasing = false
                                PremiumRepository.refreshFromCustomerInfo(info)
                                statusMessage = if (PremiumRepository.isPremium.value) {
                                    "Compras restauradas — Premium activo"
                                } else {
                                    "No hay compras para restaurar"
                                }
                            }
                        )
                    }
                ) {
                    Text(
                        text = stringResource(R.string.paywall_restore),
                        color = SamayMuted
                    )
                }
            }
        }

        val err = statusMessage ?: repoError
        if (!err.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = err,
                color = SamayMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = stringResource(R.string.paywall_safety_footer),
            color = SamayMuted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )

        if (onClose != null) {
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onClose) {
                Text(stringResource(R.string.paywall_close), color = SamayForest)
            }
        }
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    bullets: List<String>,
    highlighted: Boolean
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (highlighted) {
                    Modifier.border(2.dp, SamayForest, shape)
                } else {
                    Modifier.border(1.dp, SamayMuted.copy(alpha = 0.35f), shape)
                }
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            color = SamayForest,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(text = price, color = SamayMuted, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        bullets.forEach { line ->
            Text(text = "· $line", color = SamayForest, fontSize = 14.sp)
        }
    }
}
