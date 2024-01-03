package com.example.virtualhaus.views.bedroom

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.models.AtHomeStatus
import com.example.virtualhaus.models.AtHomeStatus.*
import com.example.virtualhaus.models.UserStatus
import com.example.virtualhaus.ui.theme.BodyFont
import com.example.virtualhaus.viewmodels.UserStatusViewModel
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.destinations.EditStatusScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun StatusScreen(navigator: DestinationsNavigator, viewModel: UserStatusViewModel = viewModel()) {
    val currentUserStatus by viewModel.currentUserStatus.observeAsState()
    val roommateStatuses by viewModel.roommateStatuses.observeAsState(initial = listOf())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        item(currentUserStatus) {
            currentUserStatus?.let {
                PrimaryButton(text = "Change My Status") {
                    navigator.navigate(EditStatusScreenDestination)
                }

                StatusView(userStatus = it)

                Divider(modifier = Modifier.fillMaxWidth().padding(10.dp))
            }
        }

        items(roommateStatuses) { userStatus ->
            StatusView(userStatus = userStatus)
        }
    }
}

@Composable
private fun StatusView(userStatus: UserStatus) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = userStatus.name, fontFamily = BodyFont, fontSize = 18.sp)

        Spacer(modifier = Modifier.weight(1F))

        StatusIcon(status = userStatus.status)
    }
}

@Composable
fun StatusIcon(status: AtHomeStatus, modifier: Modifier = Modifier) {
    val backgroundColor = when(status) {
        NO_STATUS -> Color.Gray
        HOME -> Color.Green
        AWAY -> Color.Yellow
        NEED_QUIET -> Color.Red
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.size(80.dp),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
            Canvas(modifier = Modifier.width(60.dp)) {
                drawCircle(color = backgroundColor, radius = 60F)
            }

            Text(
                text = status.shortName,
                fontFamily = BodyFont,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
            )
        }

        Text(text = status.longName, fontFamily = BodyFont, fontSize = 12.sp)
    }
}