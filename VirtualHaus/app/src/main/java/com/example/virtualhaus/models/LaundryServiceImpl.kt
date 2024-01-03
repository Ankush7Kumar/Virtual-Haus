package com.example.virtualhaus.models

import com.example.virtualhaus.viewmodels.LaundryViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val LAUNDRY_COLLECTION_PATH = "laundry"

class LaundryServiceImpl: LaundryService {
    private val dbLaundry = Firebase.firestore.collection(LAUNDRY_COLLECTION_PATH)
    private val homeId = UserManager.shared.homeId

    override fun getWasherData(
        index: Int,
        type: LaundryViewModel.MachineType,
        onUpdate: (LaundryItem) -> Unit
    ) {
        dbLaundry.whereEqualTo("homeId", homeId).whereEqualTo("index", index)
            .whereEqualTo("type", type).get().addOnSuccessListener { documents ->
            val washers = documents.mapNotNull { document ->
                val data = document.data
                LaundryItem(
                    (data["index"] as Long).toInt(),
                    data["timer"] as Long,
                    data["start"] as Timestamp,
                    type,
                )
            }
            onUpdate(washers.first())
        }
    }

    override fun updateWasherData(
        index: Int,
        timer: Long,
        type: LaundryViewModel.MachineType,
        onUpdate: () -> Unit
    ) {
        dbLaundry.whereEqualTo("homeId", homeId).whereEqualTo("index", index)
            .whereEqualTo("type", type).get().addOnSuccessListener {
            val docId = it.documents.first().id
            val updateData = mapOf("start" to Timestamp.now(), "timer" to timer)
            dbLaundry.document(docId).update(updateData).addOnSuccessListener {
                onUpdate()
            }
        }
    }
}