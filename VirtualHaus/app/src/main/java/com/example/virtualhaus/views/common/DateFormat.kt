package com.example.virtualhaus.views.common

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun Date.formatted(): String {
    val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

fun LocalDate.toDate(): Date {
    return Date.from(atStartOfDay(ZoneId.systemDefault()).toInstant())
}
