package com.example.virtualhaus.models

interface CookingService {
    fun addOrModifyCookingReservation(cookingReservation: CookingReservation)

    fun deleteCookingReservation(cookingReservation: CookingReservation)

    fun getCookingReservations(onUpdate: (List<CookingReservation>) -> Unit)
}