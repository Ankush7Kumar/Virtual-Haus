package com.example.virtualhaus.models

import android.os.Parcelable
import com.example.virtualhaus.viewmodels.LaundryViewModel
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaundryItem(
    val index: Int,
    val timer: Long,
    val startTime: Timestamp,
    val type: LaundryViewModel.MachineType
): Parcelable

data class LaundryData(val numWasher: Long, val numDryers: Long)