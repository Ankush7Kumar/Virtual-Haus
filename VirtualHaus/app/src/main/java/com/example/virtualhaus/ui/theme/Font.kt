package com.example.virtualhaus.ui.theme


import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.virtualhaus.R

val HeaderFont = FontFamily(
    Font(R.font.roboto_regular),
    Font(R.font.roboto_bold, weight = FontWeight.Bold),
    Font(R.font.roboto_italic, style = FontStyle.Italic)
)

val BodyFont = FontFamily(
    Font(R.font.montserrat_regular),
    Font(R.font.montserrat_bold, weight = FontWeight.Bold),
    Font(R.font.montserrat_italic, style = FontStyle.Italic)
)