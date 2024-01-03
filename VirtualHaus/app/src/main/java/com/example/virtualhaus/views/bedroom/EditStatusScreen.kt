package com.example.virtualhaus.views.bedroom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.models.AtHomeStatus
import com.example.virtualhaus.models.AtHomeStatus.NO_STATUS
import com.example.virtualhaus.viewmodels.UserStatusViewModel
import com.example.virtualhaus.views.common.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDate

@Destination
@Composable
fun EditStatusScreen(
    navigator: DestinationsNavigator,
    viewModel: UserStatusViewModel = viewModel(),
) {
    var selectedStatus by remember { mutableStateOf(NO_STATUS) }
    var selectedDate by remember { mutableStateOf(LocalDate.now().toDate()) }
    var selectedHour by remember { mutableStateOf(12) }
    var selectedMinute by remember { mutableStateOf(0) }
    var selectedTimeOfDay by remember { mutableStateOf(TimeOfDay.AM) }

    var shouldIncludeTime by remember { mutableStateOf(false) }
    var setupHasRun by remember { mutableStateOf(false) }

    // Allows us to update after the initial DB load (initially all values are null)
    // This is a hack, but currentUserStatus.observeAsState() was not re-running the above declarations
    viewModel.currentUserStatus.observeForever { newStatus ->
        if (setupHasRun) return@observeForever

        selectedStatus = newStatus.status
        newStatus.endDate?.let { selectedDate = it }
        newStatus.endHour?.let { selectedHour = it }
        newStatus.endMinute?.let { selectedMinute = it }
        newStatus.endTimeOfDay?.let { selectedTimeOfDay = it }
        shouldIncludeTime = newStatus.endDate != null

        setupHasRun = true
    }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Title(text = "Set Status", modifier = Modifier.padding(bottom = 10.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            AtHomeStatus.values().forEach { status ->
                StatusIcon(
                    status = status,
                    modifier = Modifier
                        .alpha(if (status == selectedStatus) 1F else 0.4F)
                        .clickable(
                            onClick = { selectedStatus = status },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        )
                )
            }
        }

        LabeledCheckbox(
            label = "Include end date?",
            checked = shouldIncludeTime,
            onCheckedChanged = { shouldIncludeTime = it },
        )

        if (shouldIncludeTime) {
            DatePicker(initialDate = selectedDate) { selectedDate = it }

            Spacer(modifier = Modifier.height(20.dp))

            TimePicker(
                initialHour = selectedHour,
                initialMinute = selectedMinute,
                initialTimeOfDay = selectedTimeOfDay,
                modifier = Modifier.fillMaxWidth(),
            ) { hour, minute, timeOfDay ->
                hour?.let { selectedHour = it }
                minute?.let { selectedMinute = it }
                timeOfDay?.let { selectedTimeOfDay = it }
            }
        }

        Spacer(modifier = Modifier.weight(1F))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            PrimaryButton(text = "Cancel") { navigator.navigateUp() }

            Spacer(modifier = Modifier.size(20.dp))

            PrimaryButton(text = "Set") {
                viewModel.updateCurrentUserStatus(
                    status = selectedStatus,
                    day = selectedDate.takeIf { shouldIncludeTime },
                    hour = selectedHour.takeIf { shouldIncludeTime },
                    minute = selectedMinute.takeIf { shouldIncludeTime },
                    timeOfDay = selectedTimeOfDay.takeIf { shouldIncludeTime },
                )
                navigator.navigateUp()
            }
        }
    }
}