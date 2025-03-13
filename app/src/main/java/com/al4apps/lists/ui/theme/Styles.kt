package com.al4apps.lists.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.al4apps.lists.R

val graphikFont = FontFamily(
    Font(R.font.graphik_thin, weight = FontWeight.Thin),
    Font(R.font.graphik_extra_light, weight = FontWeight.ExtraLight),
    Font(R.font.graphik_light, weight = FontWeight.Light),
    Font(R.font.graphik_regular, weight = FontWeight.Normal),
    Font(R.font.graphik_medium, weight = FontWeight.Medium),
    Font(R.font.graphik_black, weight = FontWeight.Black),
    Font(R.font.graphik_bold, weight = FontWeight.Bold),
    Font(R.font.graphik_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.graphik_super, weight = FontWeight.ExtraBold),
)

val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = graphikFont,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontFamily = graphikFont,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyLarge = TextStyle(
        fontFamily = graphikFont,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    ),
    titleSmall = TextStyle(
        fontFamily = graphikFont,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontFamily = graphikFont,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = TextStyle(
        fontFamily = graphikFont,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineSmall = TextStyle(
        fontFamily = graphikFont,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineMedium = TextStyle(
        fontFamily = graphikFont,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineLarge = TextStyle(
        fontFamily = graphikFont,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    ),
    displaySmall = TextStyle(
        fontFamily = graphikFont,
        fontSize = 36.sp,
        fontWeight = FontWeight.Normal
    ),
    displayMedium = TextStyle(
        fontFamily = graphikFont,
        fontSize = 45.sp,
        fontWeight = FontWeight.Normal
    ),
    displayLarge = TextStyle(
        fontFamily = graphikFont,
        fontSize = 57.sp,
        fontWeight = FontWeight.Normal
    ),
    labelSmall = TextStyle(
        fontFamily = graphikFont,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = TextStyle(
        fontFamily = graphikFont,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    ),
    labelLarge = TextStyle(
        fontFamily = graphikFont,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
)

val searchHintStyle = Typography.bodyMedium.copy(color = Color.Gray, fontWeight = FontWeight.Normal)