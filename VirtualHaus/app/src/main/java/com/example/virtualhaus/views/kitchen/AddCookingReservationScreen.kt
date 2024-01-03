package com.example.virtualhaus.views.kitchen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.models.CookingReservation
import com.example.virtualhaus.ui.theme.BluePrimary
import com.example.virtualhaus.viewmodels.AddCookingReservationViewModel
import com.example.virtualhaus.views.common.DatePicker
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.common.TimeOfDay
import com.example.virtualhaus.views.common.TimePicker
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@Destination
@Composable
fun AddCookingReservationScreen(
    navigator: DestinationsNavigator,
    cookingReservation: CookingReservation? = null,
    viewModel: AddCookingReservationViewModel = viewModel(),
) {
    val date = cookingReservation?.date ?: Date()
    val startHour = cookingReservation?.startHour ?: 1
    val startMinute = cookingReservation?.startMinute ?: 0
    val startTimeOfDay = cookingReservation?.startTimeOfDay ?: TimeOfDay.PM
    val endHour = cookingReservation?.endHour ?: 2
    val endMinute = cookingReservation?.endMinute ?: 0
    val endTimeOfDay = cookingReservation?.endTimeOfDay ?: TimeOfDay.PM
    val willUseCountertop = cookingReservation?.willUseCountertop ?: false
    val willUseStove = cookingReservation?.willUseStove ?: false
    val willUseOven = cookingReservation?.willUseOven ?: false
    val willUseDishwasher = cookingReservation?.willUseDishwasher ?: false
    val bookOrUpdateButtonText = if (cookingReservation == null) "Book" else "Update"
    val cancelOrBackButtonText = if (cookingReservation == null) "Cancel" else "Back"

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
        var mWillUseCountertop by remember { mutableStateOf(willUseCountertop) }
        var mWillUseStove by remember { mutableStateOf(willUseStove) }
        var mWillUseOven by remember { mutableStateOf(willUseOven) }
        var mWillUseDishwasher by remember { mutableStateOf(willUseDishwasher) }

        Text(
            text = "$bookOrUpdateButtonText Cooking Reservation",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp),
        )

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

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Kitchen Areas & Appliances Required:",
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Countertop")
            Checkbox(checked = mWillUseCountertop,
                onCheckedChange = { mWillUseCountertop = it },
                colors = CheckboxDefaults.colors(checkedColor = BluePrimary))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Stove")
            Checkbox(checked = mWillUseStove,
                onCheckedChange = { mWillUseStove = it },
                colors = CheckboxDefaults.colors(checkedColor = BluePrimary))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Oven")
            Checkbox(checked = mWillUseOven,
                onCheckedChange = { mWillUseOven = it },
                colors = CheckboxDefaults.colors(checkedColor = BluePrimary))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Dishwasher")
            Checkbox(checked = mWillUseDishwasher,
                onCheckedChange = { mWillUseDishwasher = it },
                colors = CheckboxDefaults.colors(checkedColor = BluePrimary))
        }

        Spacer(modifier = Modifier.weight(weight = 1F))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {

            val newOrModifiedCookingReservation = CookingReservation(
                id = cookingReservation?.id ?: "",
                name = cookingReservation?.name ?: viewModel.name,
                date = mDate,
                startHour = mStartHour,
                startMinute = mStartMinute,
                startTimeOfDay = mStartTimeOfDay,
                endHour = mEndHour,
                endMinute = mEndMinute,
                endTimeOfDay = mEndTimeOfDay,
                willUseCountertop = mWillUseCountertop,
                willUseStove = mWillUseStove,
                willUseOven = mWillUseOven,
                willUseDishwasher = mWillUseDishwasher
            )

            if (cookingReservation != null) {
                PrimaryButton(text = "Delete", backgroundColor = Color.Red) {
                    navigator.navigateUp()
                    viewModel.deleteCookingReservation(
                        newOrModifiedCookingReservation
                    )
                }

                Spacer(modifier = Modifier.size(84.dp))
            }

            PrimaryButton(text = cancelOrBackButtonText) { navigator.navigateUp() }

            Spacer(modifier = Modifier.size(20.dp))

            PrimaryButton(text = bookOrUpdateButtonText) {
                viewModel.bookCookingReservation(newOrModifiedCookingReservation)
                navigator.navigateUp()
            }
        }
    }
}