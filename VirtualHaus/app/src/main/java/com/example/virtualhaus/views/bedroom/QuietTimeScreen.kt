package com.example.virtualhaus.views.bedroom

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
import com.example.virtualhaus.models.QuietTimeReservation
import com.example.virtualhaus.ui.theme.*
import com.example.virtualhaus.viewmodels.QuietTimeViewModel
import com.example.virtualhaus.views.common.ActionButton
import com.example.virtualhaus.views.common.toDate
import com.example.virtualhaus.views.destinations.AddQuietTimeReservationScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDate
import java.util.*

@Destination
@Composable
fun QuietTimeScreen(navigator: DestinationsNavigator, viewModel: QuietTimeViewModel = viewModel()) {

    var selectedDate by remember { mutableStateOf(Date()) }
    val quietTimeReservations by viewModel.getQuietTimeReservations(selectedDate)
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
                text = "Quiet Time Requests",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            if (quietTimeReservations.isEmpty()) NoQuietTimeReservationsView()

            LazyColumn(Modifier.fillMaxSize()) {
                items(quietTimeReservations) { quietTimeReservation ->
                    if (quietTimeReservation.name == viewModel.name) {
                        QuietTimeReservationView(quietTimeReservation = quietTimeReservation,
                            onClick = {
                                navigator.navigate(
                                    AddQuietTimeReservationScreenDestination(
                                        quietTimeReservation
                                    )
                                )
                            })
                    } else {
                        QuietTimeReservationView(quietTimeReservation = quietTimeReservation,
                            backgroundColor = Teal200,
                            onClick = {})
                    }
                }
            }

        }

        Row(modifier = Modifier.padding(10.dp)) {

            ActionButton(iconId = R.drawable.ic_plus) {
                navigator.navigate(AddQuietTimeReservationScreenDestination())
            }

        }

    }
}


@Composable
fun QuietTimeReservationView(
    quietTimeReservation: QuietTimeReservation,
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
            var timeRange = " ${quietTimeReservation.startHour}:"
            if (quietTimeReservation.startMinute < 10) timeRange += "0"
            timeRange += "${quietTimeReservation.startMinute}${quietTimeReservation.startTimeOfDay} " +
                    "- ${quietTimeReservation.endHour}:"
            if (quietTimeReservation.endMinute < 10) timeRange += "0"
            timeRange += "${quietTimeReservation.endMinute}${quietTimeReservation.endTimeOfDay}"

            Text(
                text = timeRange,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = " ${quietTimeReservation.name}",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )

            if (quietTimeReservation.reason != "") {
                Text(
                    text = " Reason: ${quietTimeReservation.reason}",
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun NoQuietTimeReservationsView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.size(70.dp))

        Text(
            text = "There are no Quiet Time Requests!",
            fontFamily = HeaderFont,
            fontSize = 23.sp
        )

        Text(
            text = "Click the Plus icon to book a Quiet Time Request.",
            fontFamily = BodyFont,
            fontSize = 14.sp
        )

    }
}
