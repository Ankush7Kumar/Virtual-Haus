package com.example.virtualhaus.models

import android.os.Parcelable
import com.example.virtualhaus.views.common.TimeOfDay
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class QuietTimeReservation(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val date: Date,
    val startHour: Int,
    val startMinute: Int,
    val startTimeOfDay: TimeOfDay,
    val endHour: Int,
    val endMinute: Int,
    val endTimeOfDay: TimeOfDay,
    val reason: String
): Parcelable
