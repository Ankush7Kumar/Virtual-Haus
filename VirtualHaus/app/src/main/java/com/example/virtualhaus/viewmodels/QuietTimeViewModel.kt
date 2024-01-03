package com.example.virtualhaus.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class QuietTimeViewModel : ViewModel() {
    private val quietTimeService: QuietTimeService = QuietTimeServiceImpl()
    private val userId = UserManager.shared.userId!!

    var name = ""
        private set

    init {
        DatabaseManager.shared.getUserData(userId)?.let { data ->
            name = data["name"] as String
        }
    }

    private val quietTimeReservations: MutableLiveData<List<QuietTimeReservation>> by lazy {
        MutableLiveData<List<QuietTimeReservation>>().apply {
            quietTimeService.getQuietTimeReservations { updatedReservations ->
                value = updatedReservations
            }
        }
    }

    fun getQuietTimeReservations(selectedDate: Date): LiveData<List<QuietTimeReservation>> {
        return MediatorLiveData<List<QuietTimeReservation>>().apply {
            addSource(quietTimeReservations) { updatedReservations ->
                value = updatedReservations.sortedWith(compareBy<QuietTimeReservation> { it.startTimeOfDay }
                    .thenBy { it.startHour }
                    .thenBy { it.startMinute })
                    .filter {
                            quietTimeReservation -> convertToLocalDateViaInstant(quietTimeReservation.date).dayOfMonth == convertToLocalDateViaInstant(selectedDate).dayOfMonth &&
                            convertToLocalDateViaInstant(quietTimeReservation.date).monthValue == convertToLocalDateViaInstant(selectedDate).monthValue &&
                            convertToLocalDateViaInstant(quietTimeReservation.date).year == convertToLocalDateViaInstant(selectedDate).year }
            }
        }
    }

    private fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDate {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

}