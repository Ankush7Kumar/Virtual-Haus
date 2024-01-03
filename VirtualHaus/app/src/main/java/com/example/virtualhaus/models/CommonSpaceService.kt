package com.example.virtualhaus.models

interface CommonSpaceService {
    fun addCommonSpaceEvent(commonSpaceEvent: CommonSpaceEvent)

    fun getCommonSpaceEvents(onUpdate: (List<CommonSpaceEvent>) -> Unit)
}