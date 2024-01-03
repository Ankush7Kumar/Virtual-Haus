package com.example.virtualhaus.views.washroom

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
import com.example.virtualhaus.models.WashroomReservation
import com.example.virtualhaus.ui.theme.*
import com.example.virtualhaus.viewmodels.WashroomViewModel
import com.example.virtualhaus.views.common.ActionButton
import com.example.virtualhaus.views.common.toDate
import com.example.virtualhaus.views.destinations.AddWashroomReservationScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDate
import java.util.*

@Destination
@Composable
fun WashroomScreen(navigator: DestinationsNavigator, viewModel: WashroomViewModel = viewModel()) {

    var selectedDate by remember { mutableStateOf(Date()) }
    val washroomReservations by viewModel.getWashroomReservations(selectedDate)
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
                text = "Washroom Reservations",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Disclaimer: Washroom reservations are only for planning purposes, and do not guarantee availability of a washroom.",
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 4.dp)
            )

            if (washroomReservations.isEmpty()) NoWashroomReservationsView()

            LazyColumn(Modifier.fillMaxSize()) {
                items(washroomReservations) { washroomReservation ->
                    if (washroomReservation.name == viewModel.name) {
                        WashroomReservationView(washroomReservation = washroomReservation,
                            onClick = {
                                navigator.navigate(
                                    AddWashroomReservationScreenDestination(
                                        washroomReservation
                                    )
                                )
                            })
                    } else {
                        WashroomReservationView(washroomReservation = washroomReservation,
                            backgroundColor = Teal200,
                            onClick = {})
                    }
                }
            }

        }

        Row(modifier = Modifier.padding(10.dp)) {

            ActionButton(iconId = R.drawable.ic_plus) {
                navigator.navigate(AddWashroomReservationScreenDestination())
            }

        }

    }
}


@Composable
fun WashroomReservationView(
    washroomReservation: WashroomReservation,
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
            var timeRange = " ${washroomReservation.startHour}:"
            if (washroomReservation.startMinute < 10) timeRange += "0"
            timeRange += "${washroomReservation.startMinute}${washroomReservation.startTimeOfDay} " +
                    "- ${washroomReservation.endHour}:"
            if (washroomReservation.endMinute < 10) timeRange += "0"
            timeRange += "${washroomReservation.endMinute}${washroomReservation.endTimeOfDay}"

            Text(
                text = timeRange,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = " Washroom #${washroomReservation.washroomNumber}",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = " ${washroomReservation.name}",
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun NoWashroomReservationsView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.size(70.dp))

        Text(
            text = "There are no Washroom Reservations!",
            fontFamily = HeaderFont,
            fontSize = 23.sp
        )

        Text(
            text = "Click the Plus icon to book a Washroom Reservation.",
            fontFamily = BodyFont,
            fontSize = 14.sp
        )

    }
}
