# F3 — RevenueCat sin Play Store (#35)

Play queda fuera por tiempo. El DoD de F3 se cumple con **Test Store** + la misma forma de IDs.

## IDs (no cambiar)

| Qué | ID |
|-----|-----|
| Entitlement | `premium` |
| Offering (Current) | `default` |
| Producto | `samay_premium_quarterly` |

Código: `app/.../billing/RevenueCatIds.kt` + `OfferingsRepository.kt` (log tag **`SamayRC`**).

## Setup en dashboard (5 min) — Adriana

1. [app.revenuecat.com](https://app.revenuecat.com) → proyecto **Samay**
2. **Product catalog → Entitlements** → New → Identifier: `premium` → Save
3. **Product catalog → Products** → Add:
   - Store: **Test Store** (no Google Play)
   - Identifier: `samay_premium_quarterly`
   - Attachment: entitlement `premium`
4. **Product catalog → Offerings** → si no existe `default`, crealo
   - Add package (ej. `$rc_quarterly` o Custom) → producto `samay_premium_quarterly`
   - Marcá Offering **Current**
5. **API keys**: en `local.properties` usá la key pública que corresponda:
   - Test Store → `test_…` **o**
   - Google Play app ya creada → `goog_…` (mientras el Offering tenga el producto Test Store o Google)

## Verificar en Android Studio

1. Sync + Run
2. Logcat → filtro: `SamayRC`
3. Buscá: `F3 OK: getOfferings() trae 'samay_premium_quarterly'`

Si sale `F3 INCOMPLETE` o ERROR, falta algún paso del dashboard (arriba).

## Qué ya hace el repo

- `Purchases.configure` en `SamayApp`
- `getOfferings()` al arrancar + logs
- Constantes listas para F4 (Paywall)

## Qué no puede el agente

Crear entitlement/producto/offering en tu cuenta RevenueCat: eso es solo en el dashboard (pasos 2–4).
