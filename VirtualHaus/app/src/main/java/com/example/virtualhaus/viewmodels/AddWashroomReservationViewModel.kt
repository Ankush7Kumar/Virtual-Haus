package com.example.virtualhaus.viewmodels

import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.*

class AddWashroomReservationViewModel : ViewModel() {
    private val washroomService: WashroomService = WashroomServiceImpl()
    private val userId = UserManager.shared.userId!!

    var name = ""
        private set
    var numberOfWashrooms = 1L

    init {
        DatabaseManager.shared.getUserData(userId)?.let { data ->
            name = data["name"] as String
        }

        numberOfWashrooms = washroomService.getNumberOfWashrooms()
    }

    fun bookWashroomReservation(washroomReservation: WashroomReservation) {
        washroomService.addOrModifyWashroomReservation(washroomReservation)
    }

    fun deleteWashroomReservation(washroomReservation: WashroomReservation) {
        washroomService.deleteWashroomReservation(washroomReservation)
    }

}