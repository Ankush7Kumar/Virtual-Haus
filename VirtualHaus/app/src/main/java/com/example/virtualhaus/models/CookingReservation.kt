package com.example.virtualhaus.models

import android.os.Parcelable
import com.example.virtualhaus.views.common.TimeOfDay
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class CookingReservation(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val date: Date,
    val startHour: Int,
    val startMinute: Int,
    val startTimeOfDay: TimeOfDay,
    val endHour: Int,
    val endMinute: Int,
    val endTimeOfDay: TimeOfDay,
    val willUseCountertop: Boolean = false,
    val willUseStove: Boolean = false,
    val willUseOven: Boolean = false,
    val willUseDishwasher: Boolean = false
): Parcelable
