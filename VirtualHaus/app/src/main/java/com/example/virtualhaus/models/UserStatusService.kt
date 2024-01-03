package com.example.virtualhaus.models

import android.os.Parcelable
import com.example.virtualhaus.views.common.TimeOfDay
import kotlinx.parcelize.Parcelize
import java.util.*

enum class AtHomeStatus(val shortName: String, val longName: String) {
    NO_STATUS("?", "No Status"),
    HOME("H", "Home"),
    AWAY("A", "Away"),
    NEED_QUIET("NQ", "Needs Quiet"),
}

@Parcelize
data class UserStatus(
    val id: String,
    val userId: String,
    val name: String,
    val status: AtHomeStatus,
    val endDate: Date? = null,
    val endHour: Int? = null,
    val endMinute: Int? = null,
    val endTimeOfDay: TimeOfDay? = null,
): Parcelable

interface UserStatusService {
    fun getUserStatuses(onUpdate: (Set<UserStatus>) -> Unit)

    fun setupUserStatus(userId: String, homeId: String, name: String)

    fun updateUserStatus(userStatus: UserStatus)
}
