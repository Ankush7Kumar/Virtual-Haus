package com.example.virtualhaus.viewmodels

import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.*

class AddCookingReservationViewModel : ViewModel() {
    private val cookingService: CookingService = CookingServiceImpl()
    private val userId = UserManager.shared.userId!!

    var name = ""
        private set

    init {
        DatabaseManager.shared.getUserData(userId)?.let { data ->
            name = data["name"] as String
        }
    }

    fun bookCookingReservation(cookingReservation: CookingReservation) {
        cookingService.addOrModifyCookingReservation(cookingReservation)
    }

    fun deleteCookingReservation(cookingReservation: CookingReservation) {
        cookingService.deleteCookingReservation(cookingReservation)
    }
}