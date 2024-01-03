package com.example.virtualhaus.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class WashroomViewModel : ViewModel() {
    private val washroomService: WashroomService = WashroomServiceImpl()
    private val userId = UserManager.shared.userId!!

    var name = ""
        private set

    init {
        DatabaseManager.shared.getUserData(userId)?.let { data ->
            name = data["name"] as String
        }
    }

    private val washroomReservations: MutableLiveData<List<WashroomReservation>> by lazy {
        MutableLiveData<List<WashroomReservation>>().apply {
            washroomService.getWashroomReservations { updatedReservations ->
                value = updatedReservations
            }
        }
    }

    fun getWashroomReservations(selectedDate: Date): LiveData<List<WashroomReservation>> {
        return MediatorLiveData<List<WashroomReservation>>().apply {
            addSource(washroomReservations) { updatedReservations ->
                value = updatedReservations.sortedWith(compareBy<WashroomReservation> { it.startTimeOfDay }
                    .thenBy { it.startHour }
                    .thenBy { it.startMinute })
                    .filter {
                            washroomReservation -> convertToLocalDateViaInstant(washroomReservation.date).dayOfMonth == convertToLocalDateViaInstant(selectedDate).dayOfMonth &&
                            convertToLocalDateViaInstant(washroomReservation.date).monthValue == convertToLocalDateViaInstant(selectedDate).monthValue &&
                            convertToLocalDateViaInstant(washroomReservation.date).year == convertToLocalDateViaInstant(selectedDate).year }
            }
        }
    }

    private fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDate {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

}