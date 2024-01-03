package com.example.virtualhaus.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalTime

private const val EVENTS_COLLECTION_PATH = "commonSpaceEvents"

class CommonSpaceServiceImpl : CommonSpaceService {
    private val dbEvents = Firebase.firestore.collection(EVENTS_COLLECTION_PATH)
    private val homeId = UserManager.shared.homeId

    override fun addCommonSpaceEvent(commonSpaceEvent: CommonSpaceEvent) {
        val event = hashMapOf(
                "homeId" to homeId,
                "name" to commonSpaceEvent.name,
                "host" to commonSpaceEvent.host,
                "date" to commonSpaceEvent.date,
                "startTime" to commonSpaceEvent.startTime,
                "endTime" to commonSpaceEvent.endTime,
                "guestNameList" to commonSpaceEvent.guestNameList,
                "guestSTNList" to commonSpaceEvent.guestSTNList,
        )

        val dbEvent = if (commonSpaceEvent.homeId.isBlank()) {
            dbEvents.document() // new document
        } else {
            dbEvents.document(commonSpaceEvent.homeId) // existing document
        }
        dbEvent.set(event)
    }

    override fun getCommonSpaceEvents(onUpdate: (List<CommonSpaceEvent>) -> Unit) {
        dbEvents
                .whereEqualTo("homeId", homeId)
                .addDocumentSnapshotListener { documents ->
                    val commonSpaceEvents = documents.mapNotNull { document ->
                        val data = document.data ?: return@mapNotNull null
                        CommonSpaceEvent(
                            homeId = document.id,
                            name = data["name"] as String,
                            host = data["host"] as String,
                            date = (data["date"] as Timestamp).toDate(),
                            startTime = (data["startTime"] as HashMap<*, *>).toLocalTime(),
                            endTime = (data["endTime"] as HashMap<*, *>).toLocalTime(),
                            guestNameList = (data["guestNameList"] as ArrayList<*>).toGuestNameList(),
                            guestSTNList = (data["guestSTNList"] as ArrayList<*>).toGuestSTNList(),
                        )
                    }

                    onUpdate(commonSpaceEvents)
                }
    }
}

private fun ArrayList<*>.toGuestNameList(): MutableList<String> {
    val temp: MutableList<String> = mutableListOf()
    for(item in this){
        temp.add(item as String)
    }
    return temp
}

private fun ArrayList<*>.toGuestSTNList(): MutableList<Boolean> {
    val temp: MutableList<Boolean> = mutableListOf()
    for(item in this){
        temp.add(item as Boolean)
    }
    return temp
}

private fun HashMap<*, *>.toLocalTime(): LocalTime {
    return LocalTime.of((this["hour"] as Long).toInt(), (this["minute"] as Long).toInt())
}
