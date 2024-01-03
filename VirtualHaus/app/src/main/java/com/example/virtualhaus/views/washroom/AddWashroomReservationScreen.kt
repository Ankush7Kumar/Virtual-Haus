package com.example.virtualhaus.views.washroom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.models.WashroomReservation
import com.example.virtualhaus.viewmodels.AddWashroomReservationViewModel
import com.example.virtualhaus.views.common.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@Destination
@Composable
fun AddWashroomReservationScreen(
    navigator: DestinationsNavigator,
    washroomReservation: WashroomReservation? = null,
    viewModel: AddWashroomReservationViewModel = viewModel(),
) {

    val washroomLabels = (1..viewModel.numberOfWashrooms.toInt()).toList()

    val date = washroomReservation?.date ?: Date()
    val startHour = washroomReservation?.startHour ?: 1
    val startMinute = washroomReservation?.startMinute ?: 0
    val startTimeOfDay = washroomReservation?.startTimeOfDay ?: TimeOfDay.PM
    val endHour = washroomReservation?.endHour ?: 2
    val endMinute = washroomReservation?.endMinute ?: 0
    val endTimeOfDay = washroomReservation?.endTimeOfDay ?: TimeOfDay.PM
    val washroomNumber = washroomReservation?.washroomNumber ?: 1
    val bookOrUpdateButtonText = if (washroomReservation == null) "Book" else "Update"
    val cancelOrBackButtonText = if (washroomReservation == null) "Cancel" else "Back"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        var mDate by remember { mutableStateOf(date) }
        var mStartHour by remember { mutableStateOf(startHour) }
        var mStartMinute by remember { mutableStateOf(startMinute) }
        var mStartTimeOfDay by remember { mutableStateOf(startTimeOfDay) }
        var mEndHour by remember { mutableStateOf(endHour) }
        var mEndMinute by remember { mutableStateOf(endMinute) }
        var mEndTimeOfDay by remember { mutableStateOf(endTimeOfDay) }
        var mwashroomNumber by remember { mutableStateOf(washroomNumber) }

        Text(
            text = "$bookOrUpdateButtonText Washroom Reservation",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        IntegerDropdownPicker(
            label = "Washroom Number",
            items = washroomLabels,
            modifier = Modifier.fillMaxWidth(),
            initialValue = mwashroomNumber,
            onSelect = { mwashroomNumber = it },
        )

        Spacer(modifier = Modifier.height(10.dp))

        DatePicker(
            initialDate = mDate,
            label = "Reservation Date",
            labelStyled = true,
            onDateChange = { mDate = it }
        )

        Text(
            text = "Start Time",
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        TimePicker(
            initialHour = mStartHour,
            initialMinute = mStartMinute,
            initialTimeOfDay = mStartTimeOfDay,
            modifier = Modifier.fillMaxWidth(),
        ) { hour, minute, timeOfDay ->
            hour?.let { mStartHour = it }
            minute?.let { mStartMinute = it }
            timeOfDay?.let { mStartTimeOfDay = it }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "End Time",
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        TimePicker(
            initialHour = mEndHour,
            initialMinute = mEndMinute,
            initialTimeOfDay = mEndTimeOfDay,
            modifier = Modifier.fillMaxWidth(),
        ) { hour, minute, timeOfDay ->
            hour?.let { mEndHour = it }
            minute?.let { mEndMinute = it }
            timeOfDay?.let { mEndTimeOfDay = it }
        }

        Spacer(modifier = Modifier.weight(weight = 1F))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {

            val newOrModifiedWashroomReservation = WashroomReservation(
                id = washroomReservation?.id ?: "",
                name = washroomReservation?.name ?: viewModel.name,
                date = mDate,
                startHour = mStartHour,
                startMinute = mStartMinute,
                startTimeOfDay = mStartTimeOfDay,
                endHour = mEndHour,
                endMinute = mEndMinute,
                endTimeOfDay = mEndTimeOfDay,
                washroomNumber = mwashroomNumber
            )

            if (washroomReservation != null) {
                PrimaryButton(text = "Delete", backgroundColor = Color.Red) {
                    navigator.navigateUp()
                    viewModel.deleteWashroomReservation(
                        newOrModifiedWashroomReservation
                    )
                }

                Spacer(modifier = Modifier.size(84.dp))
            }

            PrimaryButton(text = cancelOrBackButtonText) { navigator.navigateUp() }

            Spacer(modifier = Modifier.size(20.dp))

            PrimaryButton(text = bookOrUpdateButtonText) {
                viewModel.bookWashroomReservation(newOrModifiedWashroomReservation)
                navigator.navigateUp()
            }
        }
    }
}

private fun IntRange.stringify(): List<String> = toList().map { "$it" }