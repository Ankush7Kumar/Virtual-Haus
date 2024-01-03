package com.example.virtualhaus.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class CookingViewModel : ViewModel() {
    private val cookingService: CookingService = CookingServiceImpl()
    private val userId = UserManager.shared.userId!!

    var name = ""
        private set

    init {
        DatabaseManager.shared.getUserData(userId)?.let { data ->
            name = data["name"] as String
        }
    }

    private val cookingReservations: MutableLiveData<List<CookingReservation>> by lazy {
        MutableLiveData<List<CookingReservation>>().apply {
            cookingService.getCookingReservations { updatedReservations ->
                value = updatedReservations
            }
        }
    }

    fun getCookingReservations(selectedDate: Date): LiveData<List<CookingReservation>> {
        return MediatorLiveData<List<CookingReservation>>().apply {
            addSource(cookingReservations) { updatedReservations ->
                value = updatedReservations.sortedWith(compareBy<CookingReservation> { it.startTimeOfDay }
                    .thenBy { it.startHour }
                    .thenBy { it.startMinute })
                    .filter {
                            cookingReservation -> convertToLocalDateViaInstant(cookingReservation.date).dayOfMonth == convertToLocalDateViaInstant(selectedDate).dayOfMonth &&
                            convertToLocalDateViaInstant(cookingReservation.date).monthValue == convertToLocalDateViaInstant(selectedDate).monthValue &&
                            convertToLocalDateViaInstant(cookingReservation.date).year == convertToLocalDateViaInstant(selectedDate).year }
            }
        }
    }

    private fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDate {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

}