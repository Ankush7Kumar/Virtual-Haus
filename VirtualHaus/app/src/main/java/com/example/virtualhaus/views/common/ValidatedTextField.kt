package com.example.virtualhaus.views.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.virtualhaus.ui.theme.BodyFont

@Composable
fun NonEmptyTextField(
    value: String,
    label: String,
    isValid: Boolean,
    onValueChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ValidatedTextField(
        errorMessage = "Field must not be empty.",
        isValid = isValid,
        modifier = modifier,
        value = value,
        label = label,
        enabled = enabled,
        onValueChange = { onValueChange(it, it.isNotBlank()) },
    )
}

@Composable
fun NameTextField(
    value: String,
    label: String,
    isValid: Boolean,
    onValueChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ValidatedTextField(
        errorMessage = "Please enter a valid name.",
        isValid = isValid,
        modifier = modifier,
        value = value,
        label = label,
        enabled = enabled,
        onValueChange = { onValueChange(it, it.isName()) },
    )
}

@Composable
fun IntegerTextField(
    value: String,
    label: String,
    isValid: Boolean,
    onValueChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ValidatedTextField(
        errorMessage = "Please enter a valid number.",
        isValid = isValid,
        modifier = modifier,
        value = value,
        label = label,
        enabled = enabled,
        onValueChange = { onValueChange(it, it.isInteger()) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Composable
fun MoneyTextField(
    value: String,
    label: String,
    isValid: Boolean,
    onValueChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ValidatedTextField(
        errorMessage = "Please enter a valid money amount.",
        isValid = isValid,
        modifier = modifier,
        value = value,
        label = label,
        enabled = enabled,
        onValueChange = { onValueChange(it, it.isMoney()) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        leadingIcon = { Text(text = "$") },
    )
}

@Composable
fun ValidatedTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    errorMessage: String,
    isValid: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    var hasEnteredInput by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it); hasEnteredInput = true },
            label = { Text(text = label, fontFamily = BodyFont) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = !isValid && hasEnteredInput,
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            leadingIcon = leadingIcon,
        )

        if (!isValid && hasEnteredInput) {
            Text(text = errorMessage, color = Color.Red, fontFamily = BodyFont, fontSize = 12.sp)
        }
    }
}

private fun String.isMoney(): Boolean {
    val moneyParts = split(".")
    val dollarAmount = moneyParts.firstOrNull()
    val centsAmount = moneyParts.getOrNull(index = 1)
    return moneyParts.size <= 2 &&                              // cannot have more than one "."
            moneyParts.all { it.isDigitsOnly() } &&             // must be decimal input
            dollarAmount != null &&                             // must have a dollar amount
            (centsAmount == null || centsAmount.length == 2)    // optionally a cents amount with exactly 2 inputs
}

private fun String.isName(): Boolean = isNotEmpty() && all { it.isLetter() }

private fun String.isInteger(): Boolean = toIntOrNull() != null

