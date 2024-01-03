package com.example.virtualhaus.viewmodels

import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.*

class AddQuietTimeReservationViewModel : ViewModel() {
    private val quietTimeService: QuietTimeService = QuietTimeServiceImpl()
    private val userId = UserManager.shared.userId!!

    var name = ""
        private set

    init {
        DatabaseManager.shared.getUserData(userId)?.let { data ->
            name = data["name"] as String
        }
    }

    fun bookQuietTimeReservation(quietTimeReservation: QuietTimeReservation) {
        quietTimeService.addOrModifyQuietTimeReservation(quietTimeReservation)
    }

    fun deleteQuietTimeReservation(quietTimeReservation: QuietTimeReservation) {
        quietTimeService.deleteQuietTimeReservation(quietTimeReservation)
    }
}