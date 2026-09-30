import sys

content = open('app/src/main/java/com/samay/app/ui/paywall/PaywallScreen.kt', 'r', encoding='utf-8').read()

old_str = '''                SamayButton(
                    text = stringResource(R.string.paywall_subscribe),
                    enabled = true,
                    onClick = {
                        purchasing = true
                        statusMessage = null
                        
                        // F4 (Open Source / No Play Store fallback): Mock the purchase
                        Purchases.sharedInstance.purchaseWith(
                            PurchaseParams.Builder(activity, premiumPackage).build(),
                            onError = { error, userCancelled ->
                                purchasing = false
                                if (userCancelled) {
                                    statusMessage = "Compra cancelada"
                                } else {
                                    // Simulated success for Open Source track
                                    PremiumRepository.simulatePurchaseSuccess()
                                    statusMessage = "Premium activo (Simulado para Open Source)"
                                    Log.i(TAG, "F4 OK (Mock): Simulando \u01F8xito de compra sin Play Store")
                                }
                            },
                            onSuccess = { _, customerInfo ->
                                purchasing = false
                                PremiumRepository.refreshFromCustomerInfo(customerInfo)
                                statusMessage = "Premium activo"
                                Log.i(TAG, "F4 OK: purchase success")
                            }
                        )
                    }
                )'''

new_str = '''                SamayButton(
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
                )'''

# Using regex because the exact bytes of the weird char might vary
import re
new_content = re.sub(r'SamayButton\([\s\S]*?Log\.i\(TAG, "F4 OK: purchase success"\)\s*\}\s*\)\s*\}\s*\)', new_str, content)

with open('app/src/main/java/com/samay/app/ui/paywall/PaywallScreen.kt', 'w', encoding='utf-8') as f:
    f.write(new_content)

