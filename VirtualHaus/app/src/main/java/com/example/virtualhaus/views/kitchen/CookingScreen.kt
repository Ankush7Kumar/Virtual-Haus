package com.example.virtualhaus.views.kitchen

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.R
import com.example.virtualhaus.models.CookingReservation
import com.example.virtualhaus.ui.theme.*
import com.example.virtualhaus.viewmodels.CookingViewModel
import com.example.virtualhaus.views.common.ActionButton
import com.example.virtualhaus.views.common.toDate
import com.example.virtualhaus.views.destinations.AddCookingReservationScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDate
import java.util.*

@Destination
@Composable
fun CookingScreen(navigator: DestinationsNavigator, viewModel: CookingViewModel = viewModel()) {

    var selectedDate by remember { mutableStateOf(Date()) }
    val cookingReservations by viewModel.getCookingReservations(selectedDate)
        .observeAsState(initial = emptyList())

    Box(contentAlignment = Alignment.BottomEnd) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(color = BlueTertiary)
                    .fillMaxWidth()
            ) {
                AndroidView(factory = { CalendarView(it) },
                    update = {
                        it.setOnDateChangeListener { _, year, month, day ->
                            selectedDate = LocalDate.of(year, month + 1, day).toDate()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.size(7.dp))

            Text(
                text = "Cooking Reservations",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Disclaimer: Cooking reservations are only for planning purposes, and do not guarantee availability of the kitchen.",
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 4.dp)
            )

            if (cookingReservations.isEmpty()) NoCookingReservationsView()

            LazyColumn(Modifier.fillMaxSize()) {
                items(cookingReservations) { cookingReservation ->
                    if (cookingReservation.name == viewModel.name) {
                        CookingReservationView(cookingReservation = cookingReservation,
                            onClick = {
                                navigator.navigate(
                                    AddCookingReservationScreenDestination(
                                        cookingReservation
                                    )
                                )
                            })
                    } else {
                        CookingReservationView(cookingReservation = cookingReservation,
                            backgroundColor = Teal200,
                            onClick = {})
                    }
                }
            }

        }

        Row(modifier = Modifier.padding(10.dp)) {

            ActionButton(iconId = R.drawable.ic_plus) {
                navigator.navigate(AddCookingReservationScreenDestination())
            }

        }

    }
}


@Composable
fun CookingReservationView(
    cookingReservation: CookingReservation,
    backgroundColor: Color = BlueSecondary,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .background(backgroundColor, shape = RoundedCornerShape(4.dp))
            .clickable { onClick.invoke() }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
        ) {

            // Adds extra 0 in front of minutes 0 to 9
            var timeRange = " ${cookingReservation.startHour}:"
            if (cookingReservation.startMinute < 10) timeRange += "0"
            timeRange += "${cookingReservation.startMinute}${cookingReservation.startTimeOfDay} " +
                    "- ${cookingReservation.endHour}:"
            if (cookingReservation.endMinute < 10) timeRange += "0"
            timeRange += "${cookingReservation.endMinute}${cookingReservation.endTimeOfDay}"

            Text(
                text = timeRange,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = " ${cookingReservation.name}",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )

            val appliancesUsed = ArrayList<String>()
            if (cookingReservation.willUseCountertop) appliancesUsed.add("Countertop")
            if (cookingReservation.willUseStove) appliancesUsed.add("Stove")
            if (cookingReservation.willUseOven) appliancesUsed.add("Oven")
            if (cookingReservation.willUseDishwasher) appliancesUsed.add("Dishwasher")
            val appliancesUsedCommaSeparated = java.lang.String.join(", ", appliancesUsed)
            if (appliancesUsed.isNotEmpty()) {
                Text(
                    text = " Areas/Appliances Required: $appliancesUsedCommaSeparated",
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun NoCookingReservationsView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.size(70.dp))

        Text(
            text = "There are no Cooking Reservations!",
            fontFamily = HeaderFont,
            fontSize = 23.sp
        )

        Text(
            text = "Click the Plus icon to book a Cooking Reservation.",
            fontFamily = BodyFont,
            fontSize = 14.sp
        )

    }
}
