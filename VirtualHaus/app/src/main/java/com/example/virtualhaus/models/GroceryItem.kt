package com.example.virtualhaus.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class GroceryItem(
    val id: String = UUID.randomUUID().toString(),
    val itemName: String,
    val needToBuy: Boolean = false,
    val ownerName: String? = null,
    val purchaseDate: Date? = null,
    val cost: Double? = null,
): Parcelable