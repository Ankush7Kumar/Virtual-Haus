package com.example.virtualhaus.models

import com.example.virtualhaus.viewmodels.LaundryViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DatabaseManager {
    companion object {
        val shared = DatabaseManager()
    }

    private val db = Firebase.firestore
    private val userStatusService: UserStatusService = UserStatusServiceImpl()

    fun addNewUser(homeId: String, name: String, roomNumber: Number, owner: Boolean): String {
        val user = hashMapOf(
            "name" to name,
            "roomNumber" to roomNumber,
            "homeId" to homeId,
            "owner" to owner
        )

        val doc = db.collection("users").document()
        doc.set(user, SetOptions.merge())
        userStatusService.setupUserStatus(doc.id, homeId, name)
        return doc.id
    }

    fun getUserData(userId: String): MutableMap<String, Any>? {
        val doc = db.collection("users")
            .document(userId).get().await()
        return doc.data
    }

    fun createHome(houseName: String, numberOfWashrooms: Number, numberOfWashers: Number, numberOfDryers: Number): String {
        val data = hashMapOf(
            "houseName" to houseName,
            "numberOfWashrooms" to numberOfWashrooms,
            "numberOfWashers" to numberOfWashers,
            "numberOfDryers" to numberOfDryers
        )
        val doc = db.collection("homes").document()
        doc.set(data, SetOptions.merge())
        createLaundryMachines(numberOfWashers.toLong(), doc.id, LaundryViewModel.MachineType.WASHER)
        createLaundryMachines(numberOfDryers.toLong(), doc.id, LaundryViewModel.MachineType.DRYER)
        return doc.id
    }

    fun getUsersInHouse(houseId: String): MutableList<MutableMap<String, Any>> {
        var users : MutableList<MutableMap<String, Any>> = mutableListOf(mutableMapOf())
        val qs = db.collection("users")
                .whereEqualTo("homeId", houseId).get().await()
        for (doc in qs.documents){
            users.add(doc.data as MutableMap<String, Any>)
        }
        return users
    }

    fun getLaundryData(homeId: String): Pair<Long, Long> {
        val doc = db.collection("homes")
            .document(homeId).get().await()
        val numWashers = doc.data?.get("numberOfWashers") as Long
        val numDryers = doc.data?.get("numberOfDryers") as Long
        return numWashers to numDryers
    }

    fun getWashroomData(homeId: String): Long {
        val doc = db.collection("homes")
            .document(homeId).get().await()
        return doc.data?.get("numberOfWashrooms") as Long
    }

    private fun createLaundryMachines(
        numberOfWashers: Long,
        homeId: String,
        type: LaundryViewModel.MachineType
    ) {
        val laundryCollection = db.collection(LAUNDRY_COLLECTION_PATH)
        for (x in 0 until numberOfWashers) {
            val data = hashMapOf(
                "homeId" to homeId,
                "start" to Timestamp.now(),
                "timer" to 0L,
                "index" to x,
                "type" to type,
            )
            laundryCollection.document().set(data)
        }

    }

    fun addTransaction(userId: String, homeId: String, transaction: TransactionItem): String {
        val data = hashMapOf(
                "id" to transaction.id,
                "homeId" to homeId,
                "userId" to userId,
                "borrowed" to transaction.borrowed,
                "description" to transaction.description,
                "name" to transaction.name,
                "amt" to transaction.amt,
                "users" to transaction.users,
                "date" to transaction.date,
                "paid" to transaction.paid
        )
        val doc = db.collection("transactions").document()
        doc.set(data, SetOptions.merge())
        return doc.id
    }

    fun getTransactionsForUser(userId: String, homeId: String): MutableList<MutableMap<String, Any>> {
        var transactions : MutableList<MutableMap<String, Any>> = mutableListOf(mutableMapOf())
        val qs = db.collection("transactions")
                .whereEqualTo("homeId", homeId).get().await()
        for (doc in qs.documents){
            val transaction = doc.data as MutableMap<String, Any>
            transaction["id"] = doc.id
            transactions.add(transaction)
        }
        return transactions
    }

    fun updateTransaction(transactionId: String, fieldName: String, newValue: Any): String {
        db.collection("transactions").document(transactionId)
            .update(fieldName, newValue).await()
        return transactionId
    }

    private fun <TResult> Task<TResult>.await(): TResult {
        while (!isComplete) {
        }
        return result
    }
}
