package com.example.virtualhaus.models

import com.example.virtualhaus.views.common.TimeOfDay
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val COOKING_RESERVATIONS_COLLECTION_PATH = "cookingReservations"

class CookingServiceImpl : CookingService {
    private val dbCookingReservations = Firebase.firestore.collection(COOKING_RESERVATIONS_COLLECTION_PATH)
    private val homeId = UserManager.shared.homeId

    override fun addOrModifyCookingReservation(cookingReservation: CookingReservation) {
        val item = hashMapOf(
            "name" to cookingReservation.name,
            "date" to cookingReservation.date,
            "startHour" to cookingReservation.startHour,
            "startMinute" to cookingReservation.startMinute,
            "startTimeOfDay" to cookingReservation.startTimeOfDay,
            "endHour" to cookingReservation.endHour,
            "endMinute" to cookingReservation.endMinute,
            "endTimeOfDay" to cookingReservation.endTimeOfDay,
            "willUseCountertop" to cookingReservation.willUseCountertop,
            "willUseStove" to cookingReservation.willUseStove,
            "willUseOven" to cookingReservation.willUseOven,
            "willUseDishwasher" to cookingReservation.willUseDishwasher,
            "homeId" to homeId
        )
        val dbCookingReservations = if (cookingReservation.id.isBlank()) {
            dbCookingReservations.document() // new document
        } else {
            dbCookingReservations.document(cookingReservation.id) // existing document
        }
        dbCookingReservations.set(item)
    }

    override fun deleteCookingReservation(cookingReservation: CookingReservation) {
        dbCookingReservations
            .document(cookingReservation.id)
            .delete()
    }

    override fun getCookingReservations(onUpdate: (List<CookingReservation>) -> Unit) {
        dbCookingReservations
            .whereEqualTo("homeId", homeId)
            .addDocumentSnapshotListener { documents ->
                val cookingReservations = documents.mapNotNull { document ->
                    val data = document.data ?: return@mapNotNull null
                    CookingReservation(
                        id = document.id,
                        name = data["name"] as String,
                        date = (data["date"] as Timestamp).toDate(),
                        startHour = (data["startHour"] as Long).toInt(),
                        startMinute = (data["startMinute"] as Long).toInt(),
                        startTimeOfDay = TimeOfDay.valueOf(data["startTimeOfDay"] as String),
                        endHour = (data["endHour"] as Long).toInt(),
                        endMinute = (data["endMinute"] as Long).toInt(),
                        endTimeOfDay = TimeOfDay.valueOf(data["endTimeOfDay"] as String),
                        willUseCountertop = data["willUseCountertop"] as Boolean,
                        willUseStove = data["willUseStove"] as Boolean,
                        willUseOven = data["willUseOven"] as Boolean,
                        willUseDishwasher = data["willUseDishwasher"] as Boolean
                    )
                }

                onUpdate(cookingReservations)
            }
    }
}