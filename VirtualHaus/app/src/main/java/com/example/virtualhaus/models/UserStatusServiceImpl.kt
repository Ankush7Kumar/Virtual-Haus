package com.example.virtualhaus.models

import com.example.virtualhaus.views.common.TimeOfDay
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val USER_STATUS_COLLECTION_PATH = "statuses"

class UserStatusServiceImpl : UserStatusService {
    private val dbUserStatuses = Firebase.firestore.collection(USER_STATUS_COLLECTION_PATH)
    private val homeId = UserManager.shared.homeId

    override fun getUserStatuses(onUpdate: (Set<UserStatus>) -> Unit) {
        dbUserStatuses
            .whereEqualTo("homeId", homeId)
            .addDocumentSnapshotListener { documents ->
                val userStatuses = documents.mapNotNull { document ->
                    val data = document.data ?: return@mapNotNull null

                    UserStatus(
                        id = document.id,
                        userId = data["userId"] as String,
                        name = data["name"] as String,
                        status = AtHomeStatus.valueOf(data["status"] as String),
                        endDate = (data["endDate"] as Timestamp?)?.toDate(),
                        endHour = (data["endHour"] as Long?)?.toInt(),
                        endMinute = (data["endMinute"] as Long?)?.toInt(),
                        endTimeOfDay = if (data["endTimeOfDay"] == null) null else {
                            TimeOfDay.valueOf(data["endTimeOfDay"] as String)
                        },
                    )
                }

                onUpdate(userStatuses.toSet())
            }
    }

    override fun setupUserStatus(userId: String, homeId: String, name: String) {
        val data = hashMapOf(
            "userId" to userId,
            "homeId" to homeId,
            "name" to name,
            "status" to AtHomeStatus.NO_STATUS,
            "endDate" to null,
            "endHour" to null,
            "endMinute" to null,
            "endTimeOfDay" to null,
        )

        dbUserStatuses
            .document()
            .set(data)
    }

    override fun updateUserStatus(userStatus: UserStatus) {
        val data = hashMapOf(
            "userId" to userStatus.userId,
            "homeId" to homeId,
            "name" to userStatus.name,
            "status" to userStatus.status,
            "endDate" to userStatus.endDate,
            "endHour" to userStatus.endHour,
            "endMinute" to userStatus.endMinute,
            "endTimeOfDay" to userStatus.endTimeOfDay,
        )

        dbUserStatuses
            .document(userStatus.id)
            .set(data)
    }
}
