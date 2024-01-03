package com.example.virtualhaus.models

interface WashroomService {
    fun addOrModifyWashroomReservation(washroomReservation: WashroomReservation)

    fun deleteWashroomReservation(washroomReservation: WashroomReservation)

    fun getWashroomReservations(onUpdate: (List<WashroomReservation>) -> Unit)

    fun getNumberOfWashrooms(): Long
}