package com.example.virtualhaus.views.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualhaus.ui.theme.Teal200
import com.example.virtualhaus.viewmodels.CookingViewModel
import com.example.virtualhaus.viewmodels.QuietTimeViewModel
import com.example.virtualhaus.viewmodels.WashroomViewModel
import com.example.virtualhaus.views.bedroom.QuietTimeReservationView
import com.example.virtualhaus.views.destinations.AddCookingReservationScreenDestination
import com.example.virtualhaus.views.destinations.AddQuietTimeReservationScreenDestination
import com.example.virtualhaus.views.destinations.AddWashroomReservationScreenDestination
import com.example.virtualhaus.views.kitchen.CookingReservationView
import com.example.virtualhaus.views.washroom.WashroomReservationView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    cookingViewModel: CookingViewModel = CookingViewModel(),
    quietTimeViewModel: QuietTimeViewModel = QuietTimeViewModel(),
    washroomViewModel: WashroomViewModel = WashroomViewModel()
) {

    val cookingReservations by cookingViewModel.getCookingReservations(Date())
        .observeAsState(initial = emptyList())
    val quietTimeReservations by quietTimeViewModel.getQuietTimeReservations(Date())
        .observeAsState(initial = emptyList())
    val washroomReservations by washroomViewModel.getWashroomReservations(Date())
        .observeAsState(initial = emptyList())

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        LazyColumn(Modifier.fillMaxSize()) {

            item {
                Text(
                    text = "  Cooking Reservations",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                )
            }

            if (cookingReservations.isEmpty()) {
                item {
                    Text(
                        text = "    There are no cooking reservations today.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
                    )
                }
            }

            items(cookingReservations) { cookingReservation ->
                if (cookingReservation.name == cookingViewModel.name) {
                    CookingReservationView(
                        cookingReservation = cookingReservation,
                        onClick = {
                            navigator.navigate(
                                AddCookingReservationScreenDestination(
                                    cookingReservation
                                )
                            )
                        })
                } else {
                    CookingReservationView(
                        cookingReservation = cookingReservation,
                        backgroundColor = Teal200,
                        onClick = {})
                }
            }

            item {
                Text(
                    text = "  Quiet Time Requests",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }

            if (quietTimeReservations.isEmpty()) {
                item {
                    Text(
                        text = "    There are no quiet time requests today.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
                    )
                }
            }

            items(quietTimeReservations) { quietTimeReservation ->
                if (quietTimeReservation.name == quietTimeViewModel.name) {
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

            item {
                Text(
                    text = "  Washroom Reservations",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }

            if (washroomReservations.isEmpty()) {
                item {
                    Text(
                        text = "    There are no washroom reservations today.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
                    )
                }
            }

            items(washroomReservations) { washroomReservation ->
                if (washroomReservation.name == washroomViewModel.name) {
                    WashroomReservationView(
                        washroomReservation = washroomReservation,
                        onClick = {
                            navigator.navigate(
                                AddWashroomReservationScreenDestination(
                                    washroomReservation
                                )
                            )
                        })
                } else {
                    WashroomReservationView(
                        washroomReservation = washroomReservation,
                        backgroundColor = Teal200,
                        onClick = {})
                }
            }

        }

    }
}