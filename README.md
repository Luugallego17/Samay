# Samay: A place to breathe

📥 **[DOWNLOAD SAMAY APK HERE](releases/Samay_v1.0.apk)**

*For judges: Please download the APK above to test the native offline-first experience directly on your Android device.*

---

# Samay — Plan de ejecución Shipaton 2026 (6 personas)

> App Android de contención emocional. Diferenciador = **kit de calma personalizado** (voz real / poema-versículo dominio público / música ambient) que vive **dentro** de Modo Terapia.
>
> Categorías: **RevenueCat Peace Prize** (principal) · **#BuildInPublic** · **Next Gen** (plan B sin Play).
>
> Constraint crítico: Closed Testing Play = **12 testers × 14 días**. Subir esqueleto **hoy**; el reloj corre en paralelo.

---

# 1. MAPA GENERAL DEL PROYECTO

**Flujo base (P0 demo):** ver §1.1 (prototipo = referencia, no estricto).
Welcome → idioma → armar kit → (opcional) persona de confianza → confirmar línea de crisis/país → resumen “kit listo” → Home → Modo Terapia → fin sesión → (si hace falta) pantalla de acompañamiento / crisis.

**Stack:** Kotlin + Compose · Room/DataStore · Media3 · RevenueCat · OneSignal · Firebase (Functions+Firestore mínimo) · Codemagic · Sentry.

**No hacemos:** diagnóstico médico, detección de crisis por IA, clonación de voz, widgets (P3), multiplataforma iOS.

---

## 1.1 FLUJO BASE DE REFERENCIA (prototipo UI — NO estricto)

> Este flujo viene del prototipo visual. Es la **guía de producto / orden deseado**, no un contrato rígido.
> Si falta tiempo: se pueden saltar pantallas, unir pasos o usar defaults — sin romper P0.

### Onboarding (orden deseado)

| # | Pantalla (copy prototipo) | Qué hace | ¿Saltabile? |
|---|---------------------------|----------|-------------|
| 1 | **Welcome** — “Un lugar para respirar” / CTA **Empezar** + link **¿Tenés un código de acceso?** | Entrada + promo/código jueces | Código = opcional |
| 2 | **Idioma** — “¿En qué idioma preferís usar la app?” · Español (AR MX ES) / English (US GB) | Locale | Se puede default ES |
| 3 | **Tu kit de calma** — “¿Qué te ayuda a calmarte?” · Voz / Versículo o poema / Música | Elegir tipo(s) de kit | No (o kit default) |
| 4 | **Config kit** (ej. Versículos y poemas — “Elegí los que te resuenan” + Vista previa) | Selección contenido + preview | Contenido mínimo 1 item |
| 5 | **Tu persona de confianza** — Nombre / Número / Mensaje + **Configurar después** | Contacto para “Llamar a mi persona” | **Sí — explícito en UI** |
| 6 | **Línea de crisis** — “Siempre gratis…” · lista países (ej. Argentina · 135 · CAPS) · **Confirmar** | País + número crisis | Default por locale |
| 7 | **Tu kit está listo** — resumen kit / persona / línea + nota offline · **Ir a la app** | Confirm onboarding | Se puede ir directo a Home |

### App principal

| # | Pantalla | Qué hace |
|---|----------|----------|
| 8 | **Home** — “¿Necesitás un momento?” · CTA grande **Modo Terapia** · card **Línea de crisis** · texto acceso rápido notificaciones | Entrada 1-tap a calma + crisis siempre visible |
| 9 | **Modo Terapia** — círculo respiración (ej. Exhala / countdown) · bloque **Kit de calma** (audio) · **Llamar a mi persona** · X salir | Kit vive DENTRO de Therapy |
| 10 | **Bien hecho** — “¿Cómo te sentís ahora?” · Mejor / Igual / **Necesito más ayuda** · Volver al inicio | Cierre de sesión |
| 11 | **Estás acompañado** (si “Necesito más ayuda”) — Llamar línea de crisis / Llamar a mi persona / Intentar otro momento de calma | Escalada suave, no diagnóstico |
| 12 | **Configuración** — kits / persona / notificación persistente / aviso automático (premium) / país·crisis / idioma / plan | Edición post-onboarding |

### Reglas de producto reflejadas en el prototipo

1. **Kit de calma ≠ pantalla aparte en Home** — es el audio/contenido que se reproduce dentro de Modo Terapia.
2. **Crisis siempre gratis y visible** (Home + escalada + settings); nunca detrás de paywall.
3. **Persona de confianza es opcional** en onboarding (`Configurar después`).
4. **Offline** una vez configurado (copy en “Tu kit está listo”).
5. **Promo / código de acceso** desde Welcome (jueces Shipaton).
6. Visual: serif títulos + verde bosque + fondos calmados (foto naturaleza / cream) — P1 sigue tokens; no es bloqueante para código Día 1.

### Mapa rutas Compose (alineado al prototipo, nombres flexibles)

```
Welcome → LangSelect → KitChoose → KitVoice | KitPoems | KitMusic
       → Contact (skippable) → Country/CrisisConfirm → ConfirmReady
       → Home ⇄ Therapy → TherapyEnd → (Help/Escalation) | Home
       → Settings | Paywall | Crisis (acceso directo)
```

---

# 2. MVP P0

Si falla algo de esta lista, **no hay demo válida**:

| ID | Capacidad |
|----|-----------|
| P0-01 | Proyecto Android corre en emulador + 1 dispositivo físico |
| P0-02 | Navegación completa (sealed Screen + NavHost) alineada al flujo base §1.1 (orden flexible) |
| P0-03 | Onboarding mínimo: Welcome → idioma → kit → (contacto opcional) → país/crisis → ConfirmReady → Home |
| P0-04 | Persistencia local Kit + Contact (Room) + flag onboardingDone (DataStore) |
| P0-05 | Home: CTA **Modo Terapia** + card **Línea de crisis** siempre visible |
| P0-06 | TherapyScreen: respiración (ej. Exhala + timer) + reproduce kit + “Llamar a mi persona” |
| P0-07 | Intent dial/SMS a persona de confianza (si configurada; si no, CTA a configurar o ocultar) |
| P0-08 | Crisis: número según país (ej. AR 135 CAPS), siempre gratis, desde Home / Help / Settings |
| P0-09 | Contenido mínimo: 1 ambient + lista poemas (Salmo 23 / dominio público / textos libres de copyright) + preview |
| P0-10 | Grabación de voz del familiar (MediaRecorder) local + playback (si eligió tipo voz) |
| P0-11 | RevenueCat + paywall; código de acceso desde Welcome (jueces) |
| P0-12 | OneSignal init + acceso rápido a Therapy por notificación (mínimo viable) |
| P0-13 | AAB/APK Closed Testing + **12 testers activos** |
| P0-14 | Disclaimer no-médico + copy offline en ConfirmReady |
| P0-15 | Repo público + LICENSE open source (Next Gen) |
| P0-16 | Video demo ≤2 min scripteado (flujo §1.1) |
| P0-17 | TherapyEnd básico (Mejor / Igual / Necesito más ayuda) — UI simple OK |

---

# 3. FEATURES P1 / P2 / P3

## P1 — Importante

- Pantalla **Estás acompañado** (escalada desde “Necesito más ayuda”)
- Aviso automático al familiar vía Firebase Function + OneSignal (toggle en Settings · **solo Premium**)
- Múltiples kits / contactos ilimitados (**Premium**; Free = 1 kit + 1 persona)
- Biblioteca ampliada poemas/música (**Premium**)
- Settings completo (kits / persona / país / idioma / plan)
- Notificación persistente ON/OFF (Settings + hint en Home) — **Free** (acceso rápido, no es paywall de seguridad)
- Pantalla **Planes** (ver §3.1) + promo codes jueces desde Welcome y Planes
- Crash reporting Sentry
- i18n ES/EN real
- Screenshots + icono 1024 + store listing

## P2 — Mejora

- Animaciones respiración pulidas (círculo Exhala)
- Offline-first robusto (audios bundled)
- Codemagic pipeline release
- Analytics eventos (onboarding complete, therapy start, crisis tap, purchase, help escalate)
- Deep link `samay://therapy`
- Multi-select poemas con “Vista previa” pulida

## P3 — Nice to have

- Widget home
- Personalización visual themes / más fondos foto
- Multi-contacto ya cubierto en Premium; extras de UX
- iOS (fuera de alcance)
- Detección automática de crisis

### 3.1 Pantalla Planes (prototipo — referencia, no estricto)

| Plan | Precio | Incluye |
|------|--------|---------|
| **Gratuito · Siempre** (plan actual default) | $0 | Modo Terapia completo · Línea de crisis · **1** kit de calma · **1** persona de confianza |
| **Premium** | **$4.99 / 3 meses** (~$1.67/mes) | Todo lo gratis + kits y contactos **ilimitados** + aviso automático al familiar + biblioteca ampliada |

- UI: link **¿Tenés un código promo?** (gift) entre Free y Premium
- CTA: **Suscribirse a Premium** → RevenueCat (`samay_premium_quarterly` o equivalente)
- Disclaimer obligatorio (footer): *“El Modo Terapia y la línea de crisis son siempre gratis. Nada relacionado a tu seguridad queda bloqueado por el plan.”*

**Implicación técnica (ya prevista):** entitlement `premium` en RevenueCat; gates solo en multi-kit, multi-contacto, notify automático, biblioteca extra. **No gatear** Therapy ni Crisis.

---

# 4. DISTRIBUCIÓN DE LAS 6 PERSONAS

| Persona | Rol principal | Rol secundario | Módulos / ownership |
|---------|---------------|----------------|---------------------|
| **P1** | UX/UI + design system | Compose UI polish + QA visual | Flujos, Figma/handoff, theme tokens, copy |
| **P2** | Arquitectura Compose + navegación | Integraciones shell | App shell, NavHost, DI/base, design system code |
| **P3** | Vertical Onboarding + Kit data | Contenido copy/poemas | Onboarding screens + KitRepository + Room Kit |
| **P4** | Vertical Modo Terapia + Audio | Animaciones | TherapyScreen, Media3, grabación voz, respiración |
| **P5** | Vertical Crisis + Contactos + Push | Firebase notify | Crisis lines, Contact, OneSignal, Function notify |
| **P6** | Vertical Monetización + Play + Ship | Demo/video + #BuildInPublic | RevenueCat, Play Console, testers, Codemagic, pitch assets |

**Regla anti-cuello:** P3–P6 trabajan con **mocks** hasta que P1 entregue tokens; P2 publica `ui/theme` + componentes stub el Día 1 mañana.

---

# 5. PERSONA 1 — UX/UI

### A. Arquitectura UX (P0) — empezar YA

1. User journey crisis (persona entra en pánico → 3 taps a calma)
2. Mapa de pantallas = lista sealed Screen
3. Wireframes lo-fi de: Welcome, KitChoose, KitVoice/Poems/Music, Contact, Country, Confirm, Home, Therapy, Crisis, Paywall
4. Jerarquía: **Therapy CTA > Crisis > Settings**
5. Microcopy disclaimer + empty states
6. Criterios accesibilidad: contraste, tap target ≥48dp, texto crisis legible

### B. Diseño UI (P0)

1. Tokens: Color, Type, Spacing, Radius (nombres = `Color.kt` / `Type.kt`)
2. Componentes: PrimaryButton, SecondaryButton, CrisisButton, AudioCard, BreathCircle, ContactChip, ProgressDots
3. Mood: calma, no “wellness púrpura genérico”; evitar clutter
4. Therapy: un círculo respiración + audio + 2 botones (persona / salir)
5. Paywall: premium features claras; **gratis siempre listado arriba**

### C. Estados por pantalla (P0)

Para **cada** pantalla P0: empty · loading · error · success · disabled · offline audio missing

### D. Handoff (contrato P1→P2/P3/P4)

- Export: colores hex, tipografía, spacing 4/8/16/24/32
- Nombres de componentes = nombres Compose
- Assets: iconos SVG/PNG, waveforms placeholder
- Spec Therapy: duración 180–300s, fases inhale/hold/exhale

### E. Secundario cuando no bloquea

- Implementar Theme.kt / tipografía Compose
- QA visual en device
- Screenshots store
- Storyboard video 2 min
- Posts #BuildInPublic (apoyo P6)

**DoD P1 Día 1:** tokens + wireframes high-fi Home/Therapy/Onboarding kit choose listos.

**Cola secundaria:** TherapyEnd, Paywall polish, icono app.

---

# 6. PERSONA 2 — FRONTEND (Arquitectura Compose)

### A. Setup (P0) — YA

1. Crear repo `samay-android`, LICENSE MIT/Apache
2. Empty Activity, package `com.samay.app`, minSdk 26
3. Gradle: Compose BOM, Navigation, Room, DataStore, Media3, RC, OneSignal, Firebase, Sentry
4. `SamayApplication` stubs (keys en `local.properties`)
5. `AndroidManifest`: RECORD_AUDIO, INTERNET, POST_NOTIFICATIONS
6. Estructura carpetas exacta de la guía técnica

### B. Design system code (P0)

1. Theme.kt / Color.kt / Type.kt desde tokens P1 (usar placeholders 2h si P1 no listo)
2. Componentes stub: SamayButton, CrisisButton, ScreenScaffold
3. Preview Compose de cada componente

### C. Navegación (P0)

1. `Screen` sealed class completa
2. NavHost con rutas; startDestination condicional `onboardingDone`
3. Deep link opcional P2

### D. Shell pantallas (P0)

Scaffold vacío por ruta con título + “TODO P3/P4/P5” — **desbloquea paralelo**

### E. Contratos

- Expone interfaces: `KitRepository`, `ContactRepository`, `PremiumRepository`, `AudioPlayer`
- Fake implementations en `:app` debug

**DoD P2 Día 1:** app navega Welcome→…→Home→Therapy→Crisis con pantallas stub.

**Cola:** Sentry init, i18n strings.xml ES/EN, edge navigation back stack.

---

# 7. PERSONA 3 — DESARROLLO (Onboarding + Kit + Persistencia)

**Ownership:** todo lo que arma el kit y lo guarda.

### MÓDULO ONBOARDING+KIT — P0

**Tareas granulares:**

1. Modelos `Kit`, enums `KitType` (VOICE/POEM/MUSIC)
2. Room: `AppDatabase`, `KitDao` CRUD
3. DataStore: `onboarding_done`, `language`, `country_code`
4. `KitRepository` real + FakeKitRepository
5. WelcomeScreen UI + next
6. LangSelectScreen
7. KitChooseScreen (1 o multi-select según diseño; MVP = 1 tipo primario)
8. KitPoemsScreen: lista dominio público, multi-select max N, preview texto
9. KitMusicScreen: lista `res/raw`, preview con ExoPlayer corto
10. KitVoiceScreen UI (grava P4; P3 deja callback/`voiceFilePath`)
11. ConfirmScreen resume kit
12. Guardar kit al terminar onboarding
13. Skip/re-entry: si `onboarding_done` → Home
14. Tests: insert kit, read kit, onboarding flag
15. Content pack: 5 poemas/versículos dominio público (texto) + metadatos JSON

**Dependencias:** Theme mínimo P2; grabación voz P4; país/crisis P5.

**Mocks:** poems/music hardcodeados hasta assets finales.

**DoD:** cold start → onboarding completo → Home con kit en DB.

**Plan B:** kit default “respiración + lluvia” precargado.

**Cola:** KitSettingsScreen (P1), multi-kit premium gate.

---

# 8. PERSONA 4 — DESARROLLO (Modo Terapia + Audio)

**Ownership:** experiencia de calma.

### MÓDULO THERAPY+AUDIO — P0

1. `AudioPlayer` wrapper Media3 (play/pause/stop/release)
2. Cargar ambient desde raw + voice file path
3. Mezcla simple: ambient loop bajo + voice/poem overlay (o secuencia: breath cue → audio kit)
4. TherapyViewModel: state machine IDLE→RUNNING→ENDING
5. Timer 180s default (config 180–300)
6. BreathCircle animación (Compose `Animatable`)
7. TherapyScreen layout: círculo, texto fase, controles
8. Integrar kit desde Room (observe)
9. Botón “Llamar a mi persona” → Intent dial (Contact de Room; mock si vacío)
10. Botón SMS opcional con `contact.message`
11. TherapyEndScreen básico
12. Permiso RECORD_AUDIO flow + grabación MediaRecorder → file interno
13. Playback test grabación
14. Manejo audio missing → fallback ambient only + snackbar
15. No claims médicos en UI

**Dependencias:** Kit en DB (P3); Contact (P5) — mock Contact(“Ana”,“+591…”).

**DoD:** Home → Therapy reproduce kit 3 min → call intent funciona.

**Plan B:** solo ambient + texto poema on-screen si audio falla.

**Cola:** notifyContact trigger hook (P5), animaciones P2 polish.

---

# 9. PERSONA 5 — DESARROLLO (Crisis + Contactos + OneSignal/Firebase)

**Ownership:** seguridad y red de apoyo.

### MÓDULO SAFETY — P0/P1

**P0:**

1. Modelo `Contact` + `ContactDao`
2. ContactScreen onboarding: nombre, teléfono, mensaje prefill
3. Validación teléfono E.164-ish
4. `CrisisLines` map país→número (mínimo: BO, AR, MX, CO, PE, CL, US; “default” internacional)
5. CountryScreen
6. CrisisScreen: número grande, dial, disclaimer, “siempre gratis”
7. Acceso Crisis desde Home + Settings + (ideal) top bar global
8. OneSignal init + permiso notificaciones
9. Notificación/acción “Abrir Modo Terapia” (categoría/action button o silent+tap)
10. Strings crisis actualizados; **verificar números oficiales** (fuente lista documentada en README)

**P1:**

11. Firebase project + Function `notifyContact`
12. Guardar `oneSignalId` del familiar (flujo: familiar instala app / o deep link simplificado — **MVP simplificado:** enviar SMS/WhatsApp template; push solo si mismo app user)
13. Toggle “avisar a mi persona al iniciar Therapy” (premium gate P6)
14. Fallback si Function falla → no bloquea Therapy

**Nota realista 15 días:** push al familiar requiere que el familiar tenga la app. **Plan B P0:** SMS intent con mensaje. Push = P1 si da tiempo.

**DoD P0:** crisis dial por país + contacto guardado + call/SMS desde Therapy.

**Cola:** documentar números, tests país desconocido → default.

---

# 10. PERSONA 6 — DESARROLLO (RevenueCat + Play + Ship + Demo)

**Ownership:** publicación, plata, evidencia hackathon.

### MÓDULO SHIP — P0 (paralelo desde HORA 0)

1. **Play Console:** ¿cuenta pre-nov 2023? Si no → crear / usar existente
2. Crear app “Samay” (nombre final confirmado)
3. Subir **esqueleto AAB Día 1** a Closed Testing
4. Reclutar **12 testers** (lista WhatsApp) + aceptar invite
5. Dashboard: confirmar 14-day clock corriendo
6. RevenueCat proyecto + API keys
7. Producto `samay_premium_quarterly` (o monthly si más simple) en Play
8. Entitlement `premium`, Offering `default`
9. PaywallScreen Compose + purchase sandbox
10. Promo codes UI (lista local) → unlock soft (documentar para jueces) **sin saltar RC fraud**; ideal: entitlement promo vía RC o flag DataStore solo debug + RC real para store
11. Free forever checklist visible en paywall
12. Codemagic yaml básico
13. Sentry DSN
14. Store assets: icon 1024, screenshots 1179×2556
15. Repo público + releases
16. Calendario posts #Shipaton #BuildInPublic (mín. 1/día)
17. Script video 2 min + grabar
18. Devpost: Next Gen emails .edu
19. Checklist Ship Kit RC milestones

**DoD Día 1:** closed testing live + 12 invites sent.

**DoD Día 7–10:** sandbox purchase works.

**DoD pre-deadline:** production si clock cumple; else Next Gen package completo.

**Cola:** press kit; no métricas fake.

---

# 11. MATRIZ RACI

| Feature | P1 | P2 | P3 | P4 | P5 | P6 |
|---------|----|----|----|----|----|-----|
| Design system / flujos | **A/R** | C | I | I | I | I |
| App shell + Nav | C | **A/R** | I | I | I | I |
| Onboarding + Kit persist | C | C | **A/R** | C | C | I |
| Therapy + Audio + Voice | C | C | C | **A/R** | C | I |
| Crisis + Contact + Push | C | C | I | C | **A/R** | I |
| RevenueCat + Paywall | C | C | I | I | I | **A/R** |
| Play Closed Testing | I | C | I | I | I | **A/R** |
| Contenido dominio público | C | I | **R** | C | I | A |
| Demo video / Devpost | C | I | I | I | I | **A/R** |
| #BuildInPublic | C | I | I | I | I | **A/R** |
| QA integración E2E | C | **R** | C | C | C | A |

Leyenda: **R** = Responsible · **A** = Accountable · **C** = Consulted · **I** = Informed

---

# 12. MATRIZ DE DEPENDENCIAS

| ID | Tarea | Resp | Depende de | Paralelo | Bloquea a |
|----|-------|------|------------|----------|-----------|
| T01 | Repo + Gradle + carpetas | P2 | — | sí | todos código |
| T02 | Tokens + wire Therapy/Home | P1 | — | sí | polish UI |
| T03 | NavHost + stubs | P2 | T01 | sí | integración pantallas |
| T04 | Room Kit/Contact schema | P3+P5 | T01 | sí | onboarding/therapy data |
| T05 | Closed Testing AAB v0 | P6 | T01 (APK mínimo) | **CRÍTICO** | Peace/BIP deadline |
| T06 | 12 testers accept | P6 | T05 | sí | 14-day clock |
| T07 | Onboarding UI+save | P3 | T03,T04 | sí | kit real |
| T08 | AudioPlayer + Therapy | P4 | T03; kit mock ok | sí | demo core |
| T09 | Crisis map + Contact | P5 | T03,T04 | sí | call button |
| T10 | Wire call button | P4 | T08,T09 | — | demo |
| T11 | RC configure + paywall | P6 | T01, producto Play | sí | monetización |
| T12 | OneSignal init | P5 | T01 | sí | notify P1 |
| T13 | E2E dry-run | P2 lead | T07–T11 | — | video |
| T14 | Video + Devpost | P6 | T13 | — | submit |

**Bloqueadores #1:** T01, T05/T06 (Play), T03.

---

# 13. PLAN DE TRABAJO POR DÍA

Asume **~15 días** hasta deadline (ajustar fechas reales del equipo).

### DÍA 0–1 — FUNDACIÓN + PLAY CLOCK

- P1: tokens + wireframes P0
- P2: repo, build, nav stubs
- P3: Room Kit + poems stub
- P4: AudioPlayer hello rain.mp3
- P5: CrisisLines table + Contact entity
- P6: **AAB closed testing + 12 testers** + RC project create

### DÍA 2–4 — CORE P0

- Onboarding completo, Therapy usable, Crisis dial, Contact call/SMS
- Integración kit→therapy
- Paywall sandbox

### DÍA 5–7 — INTEGRACIÓN E2E

- Cold start real device
- Fix permisos audio/notif
- Disclaimer + empty states
- Primera grabación demo interna

### DÍA 8–10 — P1 SELECTIVO + STORE

- Notify familiar (o SMS fallback documentado)
- Settings edit kit
- Screenshots, icon, listing
- Sentry, OneSignal action Therapy

### DÍA 11–12 — QA + FREEZE FEATURES

- Feature freeze inicio Día 12
- Edge cases P0
- Promo codes jueces

### DÍA 13 — POLISH + VIDEO

- Grabar demo 2 min
- Posts finales #BuildInPublic
- Production track **si** 14 días cumplidos; else package Next Gen

### DÍA 14–15 — SUBMIT

- Devpost submission completa
- Backup APK + script demo
- Ensayo pitch 5 min

---

# 14. PRIMERAS 3 HORAS

| Tiempo | P1 | P2 | P3 | P4 | P5 | P6 |
|--------|----|----|----|----|----|-----|
| **0:00–0:30** | Journey 8 pantallas en papel/Figma | Crear repo, LICENSE, clone a todos | Definir campos Kit JSON | Buscar 1 audio CC0 lluvia | Lista crisis BO/MX/AR/CO | Login Play Console; check cuenta legacy |
| **0:30–1:00** | Tokens color/tipo | New project Compose + push | Entity Kit + migración | Probar ExoPlayer sample | Entity Contact | Crear app listing draft |
| **1:00–2:00** | Wire Home+Therapy hi-fi | NavHost stub 15 rutas | KitDao insert/get | `AudioPlayer` play/stop | `CrisisLines` object | Build debug AAB firmado |
| **2:00–3:00** | Wire onboarding KitChoose | Theme placeholders + SamayButton | Fake poems list | BreathCircle anim v0 | ContactScreen form mock | **Upload closed testing + enviar 12 invites** |

**Al minuto 180 debe existir:** repo común, navegación stub, clock Play corriendo.

---

# 15. PRIMERAS 24 HORAS

**MVP vertical mínimo demostrable (aunque feo):**

1. Abrir app → Welcome → (skip rápido o onboarding corto)
2. Kit default o poems seleccionados guardados
3. Home → Therapy reproduce lluvia 30s
4. Botón crisis abre dial
5. Closed Testing con ≥12 testers invited (ideal ≥5 accepted)

**No requerido a las 24h:** RC purchase real, Firebase notify, multi-kit, animaciones finales.

---

# 16. CHECKPOINTS

| CP | Cuándo | Criterio |
|----|--------|----------|
| CP1 | +3h | Entorno + Play upload |
| CP2 | +24h | Vertical stub Therapy+Crisis |
| CP3 | +72h | Onboarding→Therapy con kit real |
| CP4 | +7d | MVP P0 E2E + paywall sandbox |
| CP5 | Día 12 | **Feature freeze** |
| CP6 | Día 13 | **Demo freeze** (script fijo) |
| CP7 | Submit | Devpost + video + repo + store o Next Gen |

**Daily 10 min:** Ayer / Hoy / Bloqueo / Ayuda — dueño: P2 o P6 rotativo.

---

# 17. QA

**Continuo, no al final.** Owner QA rotativo diario (P2 checklist).

### Por feature P0

- Functional: happy path, vacío, inválido, error, loading, back, kill process, rotation
- UX: 3 taps a Therapy desde cold start post-onboarding
- Device: 1 emulador API 26 + 1 físico Android 12+
- Integration: Room→UI, Intent dial, RC offerings null, audio file deleted
- Safety: crisis visible sin premium; no paywall delante de crisis

### Checklist release

- [ ] Disclaimer visible
- [ ] Crisis sin login
- [ ] No medical claims
- [ ] Audios dominio público atribuidos en Settings/About
- [ ] Voz = grabación real consentida

---

# 18. EDGE CASES

| Feature | Normal | Vacío | Inválido | Error | Loading | Edge |
|---------|--------|-------|----------|-------|---------|------|
| Onboarding kit | Elige poema | No selecciona | — | DB write fail | Saving… | Back mid-flow |
| Voice record | Graba 15s | Cancela | Sin permiso | Mic busy | Preparing | Storage full |
| Therapy play | Play kit | Kit null → default | File missing | Codec fail | Buffering | Llamada telefónica interrumpe audio |
| Call person | Dial | Sin contacto | Tel vacío | No dialer | — | Airplane mode |
| Crisis | Dial país | País null → default | Número mal | — | — | Roaming |
| Paywall | Compra OK | Offerings null | User cancel | Billing unavailable | Fetching | Promo code typo |
| Notif Therapy | Abre Therapy | Permiso denied | — | OneSignal fail | — | App killed |

---

# 19. GIT Y COLABORACIÓN

**Simple:**

- `main` protegida (1 review ideal; si no hay tiempo: pair merge)
- Ramas: `feat/p3-onboarding`, `feat/p4-therapy`, `feat/p5-crisis`, `feat/p6-revenuecat`, `feat/p2-nav`, `design/tokens`
- PR pequeño &lt;400 líneas cuando se pueda
- Merge ≥1/día a main

**Commits:**

`feat:` `fix:` `ui:` `docs:` `chore:` `release:`

**Conflictos calientes:** `AppNavigation.kt`, `Theme.kt`, `build.gradle.kts` → P2 es merge owner.

**Secrets:** `local.properties` + CI secrets; nunca keys en git.

---

# 20. DEMO

**Duración target:** 90–120 s video / 3–4 min live.

| Escena | Acción | Resultado | Mensaje | Seg |
|--------|--------|-----------|---------|-----|
| 1 Problema | Welcome “Un lugar para respirar” | — | Peace/impacto | 8 |
| 2 Onboarding | Kit (voz/poema/música) + crisis país | Kit + línea listos | Personalización = diferencial | 25 |
| 3 Confirm | “Tu kit está listo” → Ir a la app | Home | Offline / configurable | 8 |
| 4 Home | Tap **Modo Terapia** | Entra Therapy | Un tap | 5 |
| 5 Therapy | Exhala + kit audio + Llamar persona | Calma | Kit vive DENTRO de Therapy | 25 |
| 6 Fin | Bien hecho → Necesito más ayuda | Estás acompañado | Escalada ética | 12 |
| 7 Crisis | Llamar línea (ej. AR 135) | Dial | Siempre gratis | 8 |
| 8 Premium | Paywall / código acceso | Free vs plus | Monetización ética | 12 |
| 9 Close | #Shipaton + repo | — | BIP / Next Gen | 7 |

**Quien demo:** P4 (conoce Therapy) o P6.

**Backup:** video pregrabado si device falla.

---

# 21. PITCH

| Rol | Persona |
|-----|---------|
| Abre problema + impacto Peace | P1 o P6 |
| Demo en vivo | P4 |
| Tech (Compose, offline, RC) | P2 |
| Safety / crisis ethics | P5 |
| Business + Play + categories | P6 |
| Backup Q&A técnico | P2+P4 |
| Backup producto | P1 |

No hace falta que hablen los 6.

**Estructura 3 min:** Problema → Kit personalizado → Demo → Gratis vs Premium ético → Traction plan (#BuildInPublic) → Ask.

---

# 22. PREGUNTAS DEL JURADO

| Tipo | Pregunta | Respuesta corta | Evidencia |
|------|----------|-----------------|-----------|
| Producto | ¿Por qué no Calm/Headspace? | Ellos son biblioteca genérica; nosotros kit *tuy* + crisis + persona | Onboarding kit |
| UX | ¿Y si no puede completar onboarding en crisis? | Home permite Therapy con kit default; crisis siempre 1 tap | Default kit + Crisis |
| Tech | ¿Offline? | Audios en raw + Room local | Código |
| Seguridad | ¿Diagnostican? | No. Contención + recursos. Disclaimer | Copy |
| Datos | ¿Suben la voz a cloud? | MVP local only | File path interno |
| IA | ¿Clonan voces? | No; grabación real consentida | Recorder |
| Negocio | ¿Paywall sobre crisis? | Nunca; premium = kits extra / notify | Paywall UI |
| Impacto | ¿Medición? | Sesiones Therapy, crisis taps, #BuildInPublic | Events |
| Limitaciones | ¿Push al familiar? | Requiere app en ambos o SMS fallback | Docs |
| Play | ¿Publicados? | Closed testing desde día 1 / production si clock OK / Next Gen backup | Console |
| Escalabilidad | ¿LatAm? | Crisis lines por país + i18n | CrisisLines |
| Futuro | ¿Widget/iOS? | Roadmap P3 post-Shipaton | Backlog |

---

# 23. BACKUP PLAN

| P0 | Ideal | Parcial | Falla total |
|----|-------|---------|-------------|
| Play production | Producción | Closed testing link jueces | **Next Gen only** |
| Voice kit | Grabación | Poema + música | Solo ambient default |
| Therapy audio | Media3 mix | Secuencia play files | UI respiración + texto |
| Call person | Dial | SMS | Mostrar número copy clipboard |
| OneSignal | Action button | Tap notif abre app | Omitir en demo |
| RC purchase | Sandbox/real | Paywall UI + “Restore” mock flagged | Screenshot + explicación |
| Firebase notify | Function | SMS intent | Contar como roadmap |
| Multi-idioma | ES+EN | Solo ES | — |

**Siempre en bolsillo:** APK instalado, video mp4, script papel, screenshots.

---

# 24. FEATURE FREEZE

- **Feature freeze:** inicio **Día 12** (o 72h antes de submit).
- Después: solo bugs P0, copy, visual, estabilidad, video, Devpost.
- **Demo freeze:** Día 13 — no cambiar flujo demo.
- Cualquier P2/P3 nuevo → backlog post-submit.

---

# 25. ORDEN EXACTO DE EJECUCIÓN

1. Kickoff 15 min: roles, nombre app final, package, repo
2. P6 verifica Play account + prepara 12 testers
3. P2 crea repo + proyecto + push
4. Todos clonan + “Hello compile”
5. P6 sube AAB stub Closed Testing **hoy**
6. P1 tokens + wire Therapy/Home
7. P2 NavHost + stubs
8. P3+P5 schema Room paralelo
9. P4 AudioPlayer + rain
10. P3 onboarding save kit
11. P5 crisis+contact
12. P4 Therapy consume kit + call
13. P6 RC + paywall
14. E2E device real
15. Contenido final dominio público + disclaimer
16. OneSignal action (si tiempo)
17. Store assets + video
18. Feature freeze → QA → submit Peace/BIP o Next Gen

---

# 26. TABLERO FINAL DE TAREAS

| ID | Tarea | Persona | Pri | Estado | Dep | Est. |
|----|-------|---------|-----|--------|-----|------|
| A1 | Repo+LICENSE+README | P2 | P0 | ⚪ | — | 1h |
| A2 | Gradle deps+Application stubs | P2 | P0 | ⚪ | A1 | 2h |
| A3 | NavHost+Screen sealed+stubs | P2 | P0 | ⚪ | A2 | 3h |
| A4 | Theme placeholders | P2 | P0 | ⚪ | A2 | 2h |
| B1 | Journey+wireframes P0 | P1 | P0 | ⚪ | — | 3h |
| B2 | Tokens+component specs | P1 | P0 | ⚪ | B1 | 3h |
| B3 | Copy disclaimer+crisis | P1 | P0 | ⚪ | B1 | 1h |
| B4 | Screenshots+icon | P1 | P1 | ⚪ | MVP UI | 3h |
| C1 | Kit entity+Dao+DB | P3 | P0 | ⚪ | A2 | 2h |
| C2 | DataStore flags | P3 | P0 | ⚪ | A2 | 1h |
| C3 | Poems/music content pack | P3 | P0 | ⚪ | — | 3h |
| C4 | Onboarding screens flow | P3 | P0 | ⚪ | A3,B2 | 8h |
| C5 | Persist kit+confirm | P3 | P0 | ⚪ | C1,C4 | 2h |
| D1 | AudioPlayer Media3 | P4 | P0 | ⚪ | A2 | 3h |
| D2 | BreathCircle+timer VM | P4 | P0 | ⚪ | A3 | 4h |
| D3 | TherapyScreen integrate kit | P4 | P0 | ⚪ | C5,D1 | 4h |
| D4 | Voice record+playback | P4 | P0 | ⚪ | A2 | 4h |
| D5 | Call/SMS intents | P4 | P0 | ⚪ | E2 | 1h |
| E1 | Contact entity+Dao+screen | P5 | P0 | ⚪ | A2,A3 | 4h |
| E2 | CrisisLines+CrisisScreen | P5 | P0 | ⚪ | A3 | 3h |
| E3 | OneSignal init+perm | P5 | P0 | ⚪ | A2 | 2h |
| E4 | Notif open Therapy | P5 | P1 | ⚪ | E3,A3 | 3h |
| E5 | Firebase notifyContact | P5 | P1 | ⚪ | E3 | 5h |
| F1 | Play app+Closed Testing AAB | P6 | P0 | ⚪ | A2 | 3h |
| F2 | 12 testers accepted | P6 | P0 | ⚪ | F1 | 1–2d |
| F3 | RC project+products | P6 | P0 | ⚪ | F1 | 3h |
| F4 | PaywallScreen+sandbox | P6 | P0 | ⚪ | A3,F3 | 4h |
| F5 | Promo codes jueces | P6 | P1 | ⚪ | F4 | 2h |
| F6 | Codemagic+Sentry | P6 | P2 | ⚪ | A2 | 3h |
| F7 | Video 2min+Devpost | P6 | P0 | ⚪ | E2E | 6h |
| F8 | #BuildInPublic calendar | P6 | P1 | ⚪ | — | daily |
| G1 | E2E QA checklist | P2 | P0 | ⚪ | C5,D3,E2,F4 | 4h |
| G2 | Feature freeze | All | P0 | ⚪ | G1 | — |

Estados: 🔴 BLOQUEADO · ⚪ NO INICIADO · 🟡 EN PROGRESO · 🔵 EN REVIEW · 🟢 TERMINADO

---

## Balance de carga (2ª pasada)

| Persona | Riesgo | Mitigación |
|---------|--------|------------|
| P6 | Sobrecarga Play+RC+video | P1 ayuda assets/video; P2 ayuda Codemagic |
| P4 | Therapy es el corazón | P2 ayuda animación; P3 deja kit mock día 1 |
| P5 | Firebase notify puede comer tiempo | **SMS fallback = P0**; push = P1 |
| P1 | Puede quedar idle | Implementa Theme + QA visual + posts |
| P3 | Espera diseño | Parte con UI “ugly functional” |

## Cola secundaria si terminan early

- **P1:** TherapyEnd, motion, store listing
- **P2:** i18n, deep links, back stack polish
- **P3:** KitSettings, multi-kit gate
- **P4:** audio ducking, end screen
- **P5:** verificar números crisis, tests país
- **P6:** métricas, press kit, ensayo Q&A

---

## Primera acción ahora mismo (los 6)

1. **P6** lanza Closed Testing
2. **P2** crea repo
3. **P1** abre Figma del Therapy
4. Resto clona y compila

---

*Documento generado para ejecución real de hackathon · Samay / Shipaton 2026*
