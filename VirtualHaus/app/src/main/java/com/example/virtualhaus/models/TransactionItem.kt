package com.example.virtualhaus.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class TransactionItem(
        val id: String = UUID.randomUUID().toString(),
        val borrowed: Boolean?,
        val name: String,
        val description: String,
        val amt: Double?,
        val users : MutableList <String>,
        val date: Date?,
        var paid : Boolean?,
): Parcelable