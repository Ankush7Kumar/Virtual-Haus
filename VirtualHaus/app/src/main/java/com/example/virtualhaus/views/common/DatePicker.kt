package com.example.virtualhaus.views.common

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.virtualhaus.ui.theme.BodyFont
import java.time.LocalDate
import java.util.*

@Composable
fun DatePicker(
    initialDate: Date = Date(),
    label: String = "Date",
    labelStyled: Boolean = false,
    enabled: Boolean = true,
    onDateChange: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.time = initialDate
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day -> onDateChange(LocalDate.of(year, month + 1, day).toDate()) },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (labelStyled)
            Text(text = "$label: ${initialDate.formatted()}", fontSize = 16.sp,
                fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
        else
            Text(text = "$label: ${initialDate.formatted()}", fontFamily = BodyFont)

        PrimaryButton(text = "Select Date", enabled = enabled) {
            datePickerDialog.show()
        }
    }
}