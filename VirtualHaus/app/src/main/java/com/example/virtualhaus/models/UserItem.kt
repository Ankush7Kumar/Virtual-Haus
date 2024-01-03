package com.example.virtualhaus.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItem(
    val name: String,
    val isSelected: Boolean
): Parcelable