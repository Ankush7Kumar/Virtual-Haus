package com.example.virtualhaus.views.bedroom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.models.QuietTimeReservation
import com.example.virtualhaus.ui.theme.BodyFont
import com.example.virtualhaus.viewmodels.AddQuietTimeReservationViewModel
import com.example.virtualhaus.views.common.DatePicker
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.common.TimeOfDay
import com.example.virtualhaus.views.common.TimePicker
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@Destination
@Composable
fun AddQuietTimeReservationScreen(
    navigator: DestinationsNavigator,
    quietTimeReservation: QuietTimeReservation? = null,
    viewModel: AddQuietTimeReservationViewModel = viewModel(),
) {
    val date = quietTimeReservation?.date ?: Date()
    val startHour = quietTimeReservation?.startHour ?: 1
    val startMinute = quietTimeReservation?.startMinute ?: 0
    val startTimeOfDay = quietTimeReservation?.startTimeOfDay ?: TimeOfDay.PM
    val endHour = quietTimeReservation?.endHour ?: 2
    val endMinute = quietTimeReservation?.endMinute ?: 0
    val endTimeOfDay = quietTimeReservation?.endTimeOfDay ?: TimeOfDay.PM
    val reason = quietTimeReservation?.reason ?: ""
    val bookOrUpdateButtonText = if (quietTimeReservation == null) "Book" else "Update"
    val cancelOrBackButtonText = if (quietTimeReservation == null) "Cancel" else "Back"

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
        var mReason by remember { mutableStateOf(reason) }

        Text(
            text = "$bookOrUpdateButtonText Quiet Time Request",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        OutlinedTextField(
            value = mReason,
            onValueChange = { mReason = it },
            label = { Text(text = "Reason (Optional)", fontFamily = BodyFont) },
            singleLine = true
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

            val newOrModifiedQuietTimeReservation = QuietTimeReservation(
                id = quietTimeReservation?.id ?: "",
                name = quietTimeReservation?.name ?: viewModel.name,
                date = mDate,
                startHour = mStartHour,
                startMinute = mStartMinute,
                startTimeOfDay = mStartTimeOfDay,
                endHour = mEndHour,
                endMinute = mEndMinute,
                endTimeOfDay = mEndTimeOfDay,
                reason = mReason
            )

            if (quietTimeReservation != null) {
                PrimaryButton(text = "Delete", backgroundColor = Color.Red) {
                    navigator.navigateUp()
                    viewModel.deleteQuietTimeReservation(
                        newOrModifiedQuietTimeReservation
                    )
                }

                Spacer(modifier = Modifier.size(84.dp))
            }

            PrimaryButton(text = cancelOrBackButtonText) { navigator.navigateUp() }

            Spacer(modifier = Modifier.size(20.dp))

            PrimaryButton(text = bookOrUpdateButtonText) {
                viewModel.bookQuietTimeReservation(newOrModifiedQuietTimeReservation)
                navigator.navigateUp()
            }
        }
    }
}