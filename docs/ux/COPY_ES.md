# Textos de la app en español (B3, #14)

Reglas que seguí para todo el texto:

- Nada que suene a diagnóstico. No decimos "tenés ansiedad" ni "esto es un ataque de pánico". Hablamos de "momentos difíciles" y de lo que la persona siente.
- Nada de promesas médicas. No decimos que Samay cura, trata o reemplaza a un profesional.
- Frases cortas en todo lo que se lee en crisis.
- Voseo, igual que el resto de la app.
- Lo de seguridad siempre dice que es gratis.

Los valores ya están en `app/src/main/res/values/strings_copy.xml` (archivo aparte para no chocar con otros `strings.xml`), con el prefijo `copy_`, para que cada vertical los use sin copiar y pegar. Alicia hace la versión en inglés en #42.

## Aviso de que no es un servicio médico

Corto, para Bienvenida y Modo Terapia (`copy_disclaimer_short`):
> Samay te acompaña, pero no es un servicio médico ni de emergencia.

Largo, para Ajustes > Sobre Samay (`copy_disclaimer_long`):
> Samay es una herramienta de acompañamiento para momentos difíciles. No es un servicio médico, no hace diagnósticos y no reemplaza la ayuda de un profesional de salud. Si tu vida o la de otra persona corre peligro, llamá a la línea de ayuda o al número de emergencias de tu país.

## Tu kit está listo

`copy_ready_offline`:
> Tu kit funciona sin internet. Lo vas a encontrar siempre en el inicio.

## Crisis

Botón en Inicio (`copy_crisis_button`):
> Línea de ayuda gratuita

Título de la pantalla (`copy_crisis_title`):
> No tenés que pasar por esto solo/a

Texto de apoyo (`copy_crisis_body`):
> Hablar con alguien entrenado ayuda. La llamada es gratuita y confidencial.

Siempre gratis (`copy_crisis_free`):
> Esta línea es gratis y siempre va a estar acá, con o sin Premium.

Escalar sin diagnosticar, al pie de Crisis (`copy_crisis_escalate`):
> Si tu vida corre peligro ahora, llamá al número de emergencias de tu país.

Estás acompañado/a, título y texto (`copy_help_title`, `copy_help_body`):
> Estás acompañado/a
> Está bien necesitar más. Elegí lo que te sirva ahora.

Mensaje que se precarga al escribirle a la persona de confianza (`copy_help_sms`):
> Hola, estoy pasando un momento difícil. ¿Podés llamarme cuando puedas?

## Footer obligatorio de Planes

`copy_paywall_safety_footer`:
> Modo Terapia, la línea de ayuda y llamar a tu persona son gratis para siempre. Nada de lo que te cuida está detrás de un pago.

## Estados vacíos

Sin persona de confianza, en Modo Terapia (`copy_empty_contact`):
> Todavía no agregaste a tu persona. Podés hacerlo en Ajustes cuando quieras.

Sin kit, en Inicio (`copy_empty_kit`):
> Tu kit todavía está vacío. Mientras tanto, la respiración guiada funciona igual.

Sin audio, en Modo Terapia (`copy_empty_audio`):
> No encontramos el audio de tu kit. Seguí respirando, el ejercicio sigue igual.

Sin permiso de micrófono (`copy_empty_mic`):
> Sin permiso de micrófono no podemos grabar. Podés seguir y grabar después desde Ajustes.

Sin permiso de notificaciones (`copy_empty_notifications`):
> Para tener Samay a un toque necesitamos permiso para mostrar una notificación. Podés activarlo en Ajustes.

País sin línea cargada (`copy_empty_country`):
> Todavía no tenemos una línea cargada para tu país. Te mostramos un directorio internacional.
