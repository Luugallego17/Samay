# Design system y handoff (B2, #13)

Todo lo de acá ya está en código en `app/src/main/java/com/samay/app/ui/theme/`. Si algo no coincide con Figma, manda el código y me avisan para corregir el Figma.

## Idea visual

Queríamos que se sienta como un cuarto tranquilo y no como otra app de bienestar violeta con gradientes. Verde bosque, fondo crema, títulos con serif y mucho aire entre elementos. Solo tema claro: en una crisis el fondo oscuro con texto chico cansa más.

## Colores

| Token | Hex | Uso |
|-------|-----|-----|
| `SamayForest` | #1B4332 | Botón principal, títulos, texto importante |
| `SamayForestSoft` | #2D6A4F | Estado presionado, íconos |
| `SamaySage` | #D8E6DC | Chips, tarjeta seleccionada, anillo del círculo de respiración |
| `SamayCream` | #F7F3EA | Fondo de todas las pantallas |
| `SamaySurface` | #FFFDF8 | Tarjetas sobre el fondo |
| `SamayMuted` | #5F6B66 | Texto secundario |
| `SamayOutline` | #D9D3C5 | Bordes de tarjetas y campos |
| `SamayCrisis` | #B3261E | Línea de ayuda. No usar para otra cosa |
| `SamayCrisisSoft` | #F9DEDC | Fondo de avisos de crisis |
| `SamayOnDark` | #FFFFFF | Texto sobre forest o crisis |

Contraste medido (WCAG, AA pide 4.5 para texto normal):

| Texto sobre fondo | Ratio |
|-------------------|-------|
| Forest sobre crema | 10.0 |
| Muted sobre crema | 5.0 |
| Muted sobre surface | 5.5 |
| Blanco sobre forest | 11.1 |
| Blanco sobre crisis | 6.5 |
| Crisis sobre crema | 5.9 |
| Crisis sobre crisis soft | 5.1 |
| Forest sobre sage | 8.6 |

## Tipografía

Serif del sistema para títulos y sans del sistema para el resto. No cargamos fuentes externas, así la app pesa menos y no depende de internet.

| Estilo | Fuente | Tamaño / interlineado | Dónde |
|--------|--------|------------------------|-------|
| `headlineMedium` | Serif 500 | 28 / 34 | Título de Inicio, "Estás acompañado/a" |
| `headlineSmall` | Serif 500 | 24 / 30 | Títulos de pantalla |
| `titleLarge` | Serif 500 | 20 / 26 | Texto dentro del círculo, marca |
| `titleMedium` | Sans 600 | 16 / 22 | Títulos de tarjeta |
| `bodyLarge` | Sans 400 | 17 / 25 | Poemas, textos de crisis |
| `bodyMedium` | Sans 400 | 15 / 21 | Texto general |
| `bodySmall` | Sans 400 | 13 / 18 | Notas y créditos. Nunca para crisis |
| `labelLarge` | Sans 600 | 16 / 20 | Botones |

## Espaciado y radios

Espaciado `SamaySpacing`: 4, 8, 16, 24, 32 dp. El margen lateral de las pantallas es 24.

Radios `SamayRadius`: 8 para chips, 16 para botones y tarjetas, 24 para paneles.

Alto mínimo de cualquier cosa tocable: `SamayMinTouch` = 48dp. Los botones principales miden 52.

## Componentes

| Nombre en Compose | Qué es | Notas |
|-------------------|--------|-------|
| `SamayButton` | Acción principal | Una por pantalla |
| `SamaySecondaryButton` | Acción secundaria | Borde forest, sin relleno |
| `CrisisButton` | Línea de ayuda | Siempre visible en Inicio y Terapia, nunca detrás del paywall |
| `AudioCard` | Opción del kit (poema, sonido, voz) | Seleccionada: fondo sage y borde de 2dp |
| `ContactChip` | Persona de confianza | Inicial en círculo + nombre |
| `ProgressDots` | Paso del onboarding | El punto actual es un poco más grande |
| `BreathCircle` | Círculo de respiración | Solo visual, recibe `scale` de 0.6 a 1.0 |
| `ScreenScaffold` | Contenedor de pantalla | Fondo crema, respeta la barra de estado, margen 24 |

Hay previews de todos al final de `SamayComponents.kt`, se ven en el panel Split de Android Studio.

## Spec de Modo Terapia (para Karen)

- Duración de la sesión entre 180 y 300 segundos. Por defecto 180.
- Ciclo: inhalar 4 s, sostener 4 s, exhalar 6 s. Exhalar más largo que inhalar es lo que baja el ritmo.
- El círculo crece a 1.0 al inhalar, se queda quieto al sostener y baja a 0.6 al exhalar. La animación dura lo mismo que la fase.
- Debajo del círculo va la palabra de la fase ("Inhalá", "Sostené", "Exhalá") en `headlineSmall` y el tiempo restante en `bodyMedium`.
- Si el kit es un poema, el texto va en `bodyLarge` itálica, centrado.
- Botones: "Llamar a [nombre]" como `SamayButton` abajo. "Salir" y "Línea de ayuda" como texto arriba. Si no hay persona cargada, abajo va `CrisisButton` "Hablar con alguien ahora".

## Planes

Lo gratis siempre se lista primero y completo: Modo Terapia, línea de ayuda, llamar a mi persona y un kit. Premium va abajo (varios kits para pánico, ansiedad o enojo, aviso automático a la familia, biblioteca ampliada). Al pie va el texto de seguridad de `COPY_ES.md`.

## Íconos y assets

- Ícono de la app en 1024 x 1024: `docs/store/icon-1024.png`.
- El resto de los íconos son de Material (Compose), no hace falta exportar SVG.
