package com.example.virtualhaus.models

import com.example.virtualhaus.views.common.TimeOfDay
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val QUIET_TIME_RESERVATIONS_COLLECTION_PATH = "quietTimeReservations"

class QuietTimeServiceImpl : QuietTimeService {
    private val dbQuietTimeReservations = Firebase.firestore.collection(QUIET_TIME_RESERVATIONS_COLLECTION_PATH)
    private val homeId = UserManager.shared.homeId

    override fun addOrModifyQuietTimeReservation(quietTimeReservation: QuietTimeReservation) {
        val item = hashMapOf(
            "name" to quietTimeReservation.name,
            "date" to quietTimeReservation.date,
            "startHour" to quietTimeReservation.startHour,
            "startMinute" to quietTimeReservation.startMinute,
            "startTimeOfDay" to quietTimeReservation.startTimeOfDay,
            "endHour" to quietTimeReservation.endHour,
            "endMinute" to quietTimeReservation.endMinute,
            "endTimeOfDay" to quietTimeReservation.endTimeOfDay,
            "reason" to quietTimeReservation.reason,
            "homeId" to homeId
        )
        val dbQuietTimeReservations = if (quietTimeReservation.id.isBlank()) {
            dbQuietTimeReservations.document() // new document
        } else {
            dbQuietTimeReservations.document(quietTimeReservation.id) // existing document
        }
        dbQuietTimeReservations.set(item)
    }

    override fun deleteQuietTimeReservation(quietTimeReservation: QuietTimeReservation) {
        dbQuietTimeReservations
            .document(quietTimeReservation.id)
            .delete()
    }

    override fun getQuietTimeReservations(onUpdate: (List<QuietTimeReservation>) -> Unit) {
        dbQuietTimeReservations
            .whereEqualTo("homeId", homeId)
            .addDocumentSnapshotListener { documents ->
                val quietTimeReservations = documents.mapNotNull { document ->
                    val data = document.data ?: return@mapNotNull null
                    QuietTimeReservation(
                        id = document.id,
                        name = data["name"] as String,
                        date = (data["date"] as Timestamp).toDate(),
                        startHour = (data["startHour"] as Long).toInt(),
                        startMinute = (data["startMinute"] as Long).toInt(),
                        startTimeOfDay = TimeOfDay.valueOf(data["startTimeOfDay"] as String),
                        endHour = (data["endHour"] as Long).toInt(),
                        endMinute = (data["endMinute"] as Long).toInt(),
                        endTimeOfDay = TimeOfDay.valueOf(data["endTimeOfDay"] as String),
                        reason = data["reason"] as String
                    )
                }

                onUpdate(quietTimeReservations)
            }
    }
}