package com.example.virtualhaus.views.common

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TextBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(text = label) },
        readOnly = true,
        singleLine = true,
        modifier = modifier,
        trailingIcon = trailingIcon,
    )
}