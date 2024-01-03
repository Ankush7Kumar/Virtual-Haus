package com.example.virtualhaus.models

interface QuietTimeService {
    fun addOrModifyQuietTimeReservation(quietTimeReservation: QuietTimeReservation)

    fun deleteQuietTimeReservation(quietTimeReservation: QuietTimeReservation)

    fun getQuietTimeReservations(onUpdate: (List<QuietTimeReservation>) -> Unit)
}