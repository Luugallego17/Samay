package com.samay.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Serif en títulos (voz cálida), sans en el resto (legible en crisis). Cuerpo mínimo 15sp.
private val Serif = FontFamily.Serif
private val Sans = FontFamily.SansSerif

val SamayTypography = Typography(
    displaySmall = TextStyle(fontFamily = Serif, fontWeight = FontWeight.Medium, fontSize = 34.sp, lineHeight = 40.sp),
    headlineMedium = TextStyle(fontFamily = Serif, fontWeight = FontWeight.Medium, fontSize = 28.sp, lineHeight = 34.sp),
    headlineSmall = TextStyle(fontFamily = Serif, fontWeight = FontWeight.Medium, fontSize = 24.sp, lineHeight = 30.sp),
    titleLarge = TextStyle(fontFamily = Serif, fontWeight = FontWeight.Medium, fontSize = 20.sp, lineHeight = 26.sp),
    titleMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
    bodyLarge = TextStyle(fontFamily = Sans, fontSize = 17.sp, lineHeight = 25.sp),
    bodyMedium = TextStyle(fontFamily = Sans, fontSize = 15.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontFamily = Sans, fontSize = 13.sp, lineHeight = 18.sp),
    labelLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 20.sp)
)

/** Espaciado del handoff: 4 / 8 / 16 / 24 / 32. */
object SamaySpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
}

/** Radios del handoff. */
object SamayRadius {
    val sm = RoundedCornerShape(8.dp)    // chips
    val md = RoundedCornerShape(16.dp)   // botones y tarjetas
    val lg = RoundedCornerShape(24.dp)   // hojas y paneles
}

/** Alto mínimo de cualquier control tocable (accesibilidad, B1). */
val SamayMinTouch = 48.dp
