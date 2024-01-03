package com.example.virtualhaus.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.CommonSpaceEvent
import com.example.virtualhaus.models.CommonSpaceService
import com.example.virtualhaus.models.CommonSpaceServiceImpl

class CommonSpaceViewModel: ViewModel() {
    private val commonSpaceService: CommonSpaceService = CommonSpaceServiceImpl()

    private val commonSpaceEvents: MutableLiveData<List<CommonSpaceEvent>> by lazy {
        MutableLiveData<List<CommonSpaceEvent>>().apply {
            commonSpaceService.getCommonSpaceEvents { updatedItems ->
                // Sort everything by purchase date, where null (need to buy) items come first
                value = updatedItems.sortedBy { it.date }
            }
        }
    }

    fun getCommonSpaceEvents(): LiveData<List<CommonSpaceEvent>> = commonSpaceEvents

    fun addCommonSpaceEvent(commonSpaceEvent: CommonSpaceEvent) {
        commonSpaceService.addCommonSpaceEvent(commonSpaceEvent)
    }
}
