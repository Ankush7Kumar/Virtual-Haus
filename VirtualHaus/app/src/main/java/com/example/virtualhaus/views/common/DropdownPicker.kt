package com.example.virtualhaus.views.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import com.example.virtualhaus.ui.theme.BodyFont

// Adapted from https://www.geeksforgeeks.org/drop-down-menu-in-android-using-jetpack-compose/

@Composable
fun DropdownPicker(
    label: String,
    items: List<String>,
    modifier: Modifier = Modifier,
    initialValue: String = items.first(),
    onSelect: (String) -> Unit,
) {
    var selection by remember { mutableStateOf(initialValue) }
    var dropdownSize by remember { mutableStateOf(Size.Zero) }
    var isExpanded by remember { mutableStateOf(false) }

    val icon = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier) {
        TextBox(
            label = label,
            value = selection,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { dropdownSize = it.size.toSize() },
            trailingIcon = {
                Icon(icon, null, Modifier.clickable { isExpanded = !isExpanded })
            },
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { dropdownSize.width.toDp() }),
        ) {
            items.forEach { label ->
                DropdownMenuItem(
                    content = { Text(text = label, fontFamily = BodyFont) },
                    onClick = {
                        selection = label
                        isExpanded = false
                        onSelect(label)
                    },
                )
            }
        }
    }
}

@Composable
fun IntegerDropdownPicker(
    label: String,
    items: List<Int>,
    modifier: Modifier = Modifier,
    initialValue: Int = items.first(),
    onSelect: (Int) -> Unit,
) {
    var selection by remember { mutableStateOf(initialValue) }
    var dropdownSize by remember { mutableStateOf(Size.Zero) }
    var isExpanded by remember { mutableStateOf(false) }

    val icon = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier) {
        TextBox(
            label = label,
            value = selection.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { dropdownSize = it.size.toSize() },
            trailingIcon = {
                Icon(icon, null, Modifier.clickable { isExpanded = !isExpanded })
            },
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { dropdownSize.width.toDp() }),
        ) {
            items.forEach { label ->
                DropdownMenuItem(
                    content = { Text(text = label.toString(), fontFamily = BodyFont) },
                    onClick = {
                        selection = label
                        isExpanded = false
                        onSelect(label)
                    },
                )
            }
        }
    }
}
