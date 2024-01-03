package com.example.virtualhaus.views.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.virtualhaus.ui.theme.BluePrimary
import com.example.virtualhaus.ui.theme.BodyFont
import com.example.virtualhaus.ui.theme.Shapes

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = Shapes.medium,
    backgroundColor: Color = BluePrimary,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        content = content,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
    )
}

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color = BluePrimary,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    PrimaryButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        backgroundColor = backgroundColor,
        content = {
            Text(
                text = text,
                color = Color.White,
                fontFamily = BodyFont,
                textAlign = TextAlign.Center,
            )
        },
    )
}

@Composable
fun ActionButton(iconId: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        backgroundColor = BluePrimary,
        onClick = onClick,
        modifier = modifier,
        content = {
            Icon(
                painter = painterResource(id = iconId),
                tint = Color.White,
                contentDescription = null,
            )
        },
    )
}
