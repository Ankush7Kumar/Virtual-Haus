package com.example.virtualhaus.views.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.virtualhaus.ui.theme.HeaderFont

@Composable
fun Title(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 30.sp,
        fontFamily = HeaderFont,
        modifier = modifier,
    )
}
