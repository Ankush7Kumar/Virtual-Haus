package com.example.virtualhaus.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.*
import com.example.virtualhaus.views.common.TimeOfDay
import java.time.Instant
import java.util.*

class UserStatusViewModel : ViewModel() {
    private val userStatusService: UserStatusService = UserStatusServiceImpl()
    private val currentUserId = UserManager.shared.userId!!

    private val userStatuses: MutableLiveData<List<UserStatus>> by lazy {
        MutableLiveData<List<UserStatus>>().apply {
            userStatusService.getUserStatuses { statuses ->
                value = statuses.map { status ->
                    val endDate = computeEndTime(status) ?: return@map status

                    // Exclude any statuses that have expired
                    if (endDate < Instant.now()) {
                        UserStatus(status.id, status.userId, status.name, AtHomeStatus.NO_STATUS)
                    } else {
                        status
                    }
                }
            }
        }
    }

    val roommateStatuses: LiveData<List<UserStatus>> by lazy {
        MediatorLiveData<List<UserStatus>>().apply {
            addSource(userStatuses) { statuses ->
                value = statuses
                    .filter { it.userId != currentUserId }
                    .sortedBy { it.name }
            }
        }
    }

    val currentUserStatus: LiveData<UserStatus> by lazy {
        MediatorLiveData<UserStatus>().apply {
            addSource(userStatuses) { statuses ->
                value = statuses.singleOrNull { it.userId == currentUserId }
            }
        }
    }

    fun updateCurrentUserStatus(
        status: AtHomeStatus,
        day: Date? = null,
        hour: Int? = null,
        minute: Int? = null,
        timeOfDay: TimeOfDay? = null,
    ) {
        val oldStatus = currentUserStatus.value ?: return
        val newStatus = oldStatus.copy(
            status = status,
            endDate = day,
            endHour = hour,
            endMinute = minute,
            endTimeOfDay = timeOfDay,
        )

        userStatusService.updateUserStatus(newStatus)
    }
}

private fun computeEndTime(status: UserStatus): Instant? {
    val date = status.endDate ?: return null
    val hour = status.endHour ?: return null
    val minute = status.endMinute ?: return null
    val timeOfDay = status.endTimeOfDay ?: return null

    val adjustedHour = when (timeOfDay) {
        TimeOfDay.AM -> hour % 12 // 12AM is 0 hours after start of day
        TimeOfDay.PM -> (hour % 12) + 12
    }

    val totalMinutes = adjustedHour * 60 + minute

    // we store the date as the start of the day, so add on the minutes and hours
    return date.toInstant().plusSeconds(totalMinutes * 60L)
}
