package com.example.virtualhaus.models

import com.example.virtualhaus.views.common.TimeOfDay
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val WASHROOM_RESERVATIONS_COLLECTION_PATH = "washroomReservations"

class WashroomServiceImpl : WashroomService {
    private val dbWashroomReservations = Firebase.firestore.collection(WASHROOM_RESERVATIONS_COLLECTION_PATH)
    private val homeId = UserManager.shared.homeId.toString()

    override fun addOrModifyWashroomReservation(washroomReservation: WashroomReservation) {
        val item = hashMapOf(
            "name" to washroomReservation.name,
            "date" to washroomReservation.date,
            "startHour" to washroomReservation.startHour,
            "startMinute" to washroomReservation.startMinute,
            "startTimeOfDay" to washroomReservation.startTimeOfDay,
            "endHour" to washroomReservation.endHour,
            "endMinute" to washroomReservation.endMinute,
            "endTimeOfDay" to washroomReservation.endTimeOfDay,
            "washroomNumber" to washroomReservation.washroomNumber,
            "homeId" to homeId
        )
        val dbWashroomReservations = if (washroomReservation.id.isBlank()) {
            dbWashroomReservations.document() // new document
        } else {
            dbWashroomReservations.document(washroomReservation.id) // existing document
        }
        dbWashroomReservations.set(item)
    }

    override fun deleteWashroomReservation(washroomReservation: WashroomReservation) {
        dbWashroomReservations
            .document(washroomReservation.id)
            .delete()
    }

    override fun getWashroomReservations(onUpdate: (List<WashroomReservation>) -> Unit) {
        dbWashroomReservations
            .whereEqualTo("homeId", homeId)
            .addDocumentSnapshotListener { documents ->
                val washroomReservation = documents.mapNotNull { document ->
                    val data = document.data ?: return@mapNotNull null
                    WashroomReservation(
                        id = document.id,
                        name = data["name"] as String,
                        date = (data["date"] as Timestamp).toDate(),
                        startHour = (data["startHour"] as Long).toInt(),
                        startMinute = (data["startMinute"] as Long).toInt(),
                        startTimeOfDay = TimeOfDay.valueOf(data["startTimeOfDay"] as String),
                        endHour = (data["endHour"] as Long).toInt(),
                        endMinute = (data["endMinute"] as Long).toInt(),
                        endTimeOfDay = TimeOfDay.valueOf(data["endTimeOfDay"] as String),
                        washroomNumber = (data["washroomNumber"] as Long).toInt()
                    )
                }

                onUpdate(washroomReservation)
            }
    }

    override fun getNumberOfWashrooms(): Long {
        return DatabaseManager.shared.getWashroomData(homeId)
    }

}