package com.example.virtualhaus.views.login

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.common.Title
import com.example.virtualhaus.views.destinations.CreateHomeScreenDestination
import com.example.virtualhaus.views.destinations.JoinHomeScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun InitialWelcomeScreen(navigator: DestinationsNavigator) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1F))

        WelcomeMessage()

        Spacer(modifier = Modifier.size(30.dp))

        Row {
            PrimaryButton(text = "Create Home") {
                navigator.navigate(CreateHomeScreenDestination)
            }

            Spacer(modifier = Modifier.size(10.dp))

            PrimaryButton(text = "Join Home") {
                navigator.navigate(JoinHomeScreenDestination)
            }
        }

        Spacer(modifier = Modifier.weight(1F))
    }
}

@Composable
fun WelcomeMessage() {
    Title(text = "Welcome")
    Title(text = "to VirtualHaus")
}
