package com.example.virtualhaus.models

import com.example.virtualhaus.viewmodels.LaundryViewModel

interface LaundryService {
    fun getWasherData(
        index: Int,
        type: LaundryViewModel.MachineType,
        onUpdate: (LaundryItem) -> Unit
    )

    fun updateWasherData(
        index: Int,
        timer: Long,
        type: LaundryViewModel.MachineType,
        onUpdate: () -> Unit
    )
}