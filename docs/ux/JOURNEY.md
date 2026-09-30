# Recorrido y wireframes (B1, #12)

Figma con los bocetos en alta: https://www.figma.com/make/gK8eOfockx0j2zGaXZnUpP/Bienestar-y-ayuda-app

## El caso que diseñamos primero

Alguien está en medio de un ataque de pánico. Le tiemblan las manos, no puede leer párrafos y no va a buscar nada en un menú. Ya armó su kit de calma en el onboarding, días antes, cuando estaba tranquila.

Lo que tiene que pasar:

1. Toca la notificación fija de Samay (o abre la app).
2. Toca el círculo grande del inicio, "Necesito calma".
3. Arranca Modo Terapia: respiración guiada con su audio (la voz de su mamá, el Salmo 23 o la lluvia) y el botón para llamar a su persona en la misma pantalla.

Desde la notificación son 1 o 2 toques. Desde el ícono de la app son 2. La meta era 3 como máximo, así que sobra margen.

Si nada de eso alcanza, la línea de ayuda del país está visible en el inicio y dentro de Modo Terapia, sin pasar por ningún paso intermedio y sin paywall.

## Mapa de pantallas

Los nombres son los mismos del `Screen` sealed class (A3, #10), así no hay que traducir nada entre diseño y código.

| Ruta | Pantalla | Para qué sirve |
|------|----------|----------------|
| `welcome` | Bienvenida | Qué es Samay en una frase y aviso de que no es un servicio médico |
| `lang_select` | Idioma | Español o inglés |
| `kit_choose` | Qué te calma | Voz de una persona, poema/versículo o sonido |
| `kit_voice` | Grabar voz | La persona de confianza graba un mensaje corto en el teléfono |
| `kit_poems` | Elegir poema | Textos de dominio público |
| `kit_music` | Elegir sonido | Lluvia, olas |
| `contact` | Tu persona | Nombre y teléfono, se puede saltear |
| `crisis_confirm` | Tu línea de ayuda | País detectado y número gratuito, editable |
| `confirm_ready` | Tu kit está listo | Resumen y aviso de que funciona sin internet |
| `home` | Inicio | Círculo "Necesito calma", resumen del kit, línea de ayuda |
| `therapy` | Modo Terapia | Respiración + audio del kit + llamar a mi persona |
| `therapy_end` | Terminaste | "¿Cómo te sentís ahora?" |
| `help` | Estás acompañado/a | Llamar o escribir a su persona, línea de ayuda, otra sesión |
| `crisis` | Línea de ayuda | Número del país, emergencias, directorio internacional |
| `settings` | Ajustes | Persona, país, notificación fija, créditos, aviso legal |
| `paywall` | Planes | Gratis arriba, Premium abajo |

## Jerarquía visual

En el inicio manda Modo Terapia: es el elemento más grande de la pantalla y está al centro. Después viene la línea de ayuda, un botón rojo abajo de todo que siempre está ahí. Ajustes y Premium quedan como texto chico arriba a la derecha, porque nadie los necesita en una crisis.

Dentro de Modo Terapia la regla es la misma. Círculo de respiración al centro, "Llamar a [nombre]" como botón principal abajo, y "Salir" y "Línea de ayuda" como texto arriba.

## Accesibilidad

- Contraste AA en todos los pares de texto y fondo. Los valores medidos están en `DESIGN_SYSTEM.md`; el más justo es el gris secundario sobre crema, con 5.0:1.
- Todo lo que se toca mide 48dp o más. Los botones principales miden 52dp de alto.
- El texto de crisis nunca baja de 15sp y el número de la línea se muestra en 28sp.
- Nada depende solo del color: el botón de ayuda dice "Línea de ayuda" además de ser rojo.
- En pantallas de crisis las frases son cortas. Una idea por línea.

## Estados por pantalla

| Pantalla | Vacío | Cargando | Error | Sin audio / permiso |
|----------|-------|----------|-------|---------------------|
| Grabar voz | Botón "Empezar a grabar" | "Grabando…" en rojo | Grabación muy corta, pedir otra | Sin micrófono: se puede seguir y grabar después |
| Tu persona | Botón dice "Saltear" | No aplica | Teléfono vacío no bloquea | No aplica |
| Tu línea de ayuda | País detectado solo | No aplica | País sin línea: directorio internacional | No aplica |
| Tu kit está listo | No aplica | "Guardando…" y botón desactivado | Mensaje en rojo, se puede reintentar | No aplica |
| Inicio | Sin kit: "Respiración guiada" | No aplica | No aplica | No aplica |
| Modo Terapia | Sin kit: respiración + lluvia | No aplica | No aplica | Audio faltante: aviso y la respiración sigue igual |
| Planes | No aplica | Precio cargando | RevenueCat sin configurar: aviso, lo gratis sigue igual | No aplica |

Los textos exactos de cada estado están en `COPY_ES.md`.
