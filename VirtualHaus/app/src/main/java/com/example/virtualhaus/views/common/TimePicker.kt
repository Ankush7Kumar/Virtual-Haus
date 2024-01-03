package com.example.virtualhaus.views.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class TimeOfDay {
    AM,
    PM,
}

private val PICKER_WIDTH = 100.dp
private val HOUR_LABELS = (1..12).stringify()
private val MINUTE_LABELS = (0..59).stringify().map { if (it.length == 1) "0$it" else it }
private val TIME_OF_DAY_LABELS = TimeOfDay.values().map { it.toString() }

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    initialHour: Int = 1,
    initialMinute: Int = 0,
    initialTimeOfDay: TimeOfDay = TimeOfDay.AM,
    onValueChange: (Int?, Int?, TimeOfDay?) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        DropdownPicker(
            label = "Hour",
            items = HOUR_LABELS,
            modifier = Modifier.width(PICKER_WIDTH),
            initialValue = "$initialHour",
            onSelect = { onValueChange(it.toInt(), null, null) },
        )
        Text(text = " : ", color = Color.Gray, fontSize = 24.sp)
        DropdownPicker(
            label = "Minute",
            items = MINUTE_LABELS,
            modifier = Modifier
                .width(PICKER_WIDTH)
                .padding(end = 10.dp),
            initialValue = if (initialMinute < 10) "0$initialMinute" else "$initialMinute",
            onSelect = { onValueChange(null, it.toInt(), null) },
        )
        DropdownPicker(
            label = "AM/PM",
            items = TIME_OF_DAY_LABELS,
            modifier = Modifier.width(PICKER_WIDTH),
            initialValue = "$initialTimeOfDay",
            onSelect = { onValueChange(null, null, TimeOfDay.valueOf(it)) },
        )
    }
}

private fun IntRange.stringify(): List<String> = toList().map { "$it" }