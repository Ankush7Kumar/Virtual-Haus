package com.example.virtualhaus.views.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.virtualhaus.ui.theme.BluePrimary
import com.example.virtualhaus.ui.theme.BodyFont

@Composable
fun LabeledCheckbox(label: String, checked: Boolean, onCheckedChanged: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, fontFamily = BodyFont)

        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChanged,
            colors = CheckboxDefaults.colors(checkedColor = BluePrimary),
        )
    }
}
