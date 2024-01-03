package com.example.virtualhaus.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalTime
import java.util.*

@Parcelize
data class CommonSpaceEvent(
    // todo: change val to val after writing function to fetch data
        val homeId: String = UUID.randomUUID().toString(),
        val name: String,
        val host: String,
        val date: Date,
        val startTime: LocalTime,
        val endTime: LocalTime,
        val guestNameList: MutableList<String>,
        val guestSTNList: MutableList<Boolean>,
): Parcelable