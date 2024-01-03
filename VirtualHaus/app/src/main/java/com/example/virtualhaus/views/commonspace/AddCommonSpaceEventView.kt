package com.example.virtualhaus.views

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.models.CommonSpaceEvent
import com.example.virtualhaus.models.DatabaseManager
import com.example.virtualhaus.models.UserManager
import com.example.virtualhaus.ui.theme.BluePrimary
import com.example.virtualhaus.ui.theme.BodyFont
import com.example.virtualhaus.viewmodels.CommonSpaceViewModel
import com.example.virtualhaus.views.common.DatePicker
import com.example.virtualhaus.views.common.DropdownPicker
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.destinations.CommonSpaceScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Destination
@Composable
fun AddCommonSpaceEventScreen(
        event: CommonSpaceEvent? = null,
        navigator: DestinationsNavigator,
        viewModel: CommonSpaceViewModel = viewModel()
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val hosts = getAllRoommates()

    val homeId = event?.homeId ?: ""
    val name = event?.name ?: ""
    val host = event?.host ?: hosts[0]
    val date = event?.date ?: Date()
    val startTime = event?.startTime ?: LocalTime.now()
    val endTime = event?.endTime ?: LocalTime.now()
    val guestNameList = event?.guestNameList ?: arrayListOf()
    val guestSTNList = event?.guestSTNList ?: arrayListOf()

    Column(
        modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(30.dp)
    ) {

        var eventName by remember { mutableStateOf(name) }
        var eventHost by remember { mutableStateOf(host) }
        var eventDate by remember { mutableStateOf(date) }
        var eventStartTime by remember { mutableStateOf(startTime) }
        var eventEndTime by remember { mutableStateOf(endTime) }
        var eventGuestNameList by remember { mutableStateOf(guestNameList) }
        var eventGuestSTNList by remember { mutableStateOf(guestSTNList) }

        OutlinedTextField(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text(text = "Event Name", fontFamily = BodyFont) },
                singleLine = true,
        )

        DropdownPicker(label = "Host Name", items = hosts, onSelect = {eventHost = it}, modifier = Modifier.width(screenWidth))

        Spacer(Modifier.padding(bottom = 10.dp))
        DatePicker(initialDate = eventDate){ eventDate = it }

        val startTimePickerDialog = TimePickerDialog(
                LocalContext.current,
                {_, hour, minute ->
                    eventStartTime = LocalTime.of(hour, minute)
                }, eventStartTime.hour, eventStartTime.minute, false
        )

        val endTimePickerDialog = TimePickerDialog(
                LocalContext.current,
                {_, hour, minute ->
                    eventEndTime = LocalTime.of(hour, minute)
                }, eventEndTime.hour, eventEndTime.minute, false
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Start Time: ${eventStartTime.formatted()}", fontFamily = BodyFont)
            PrimaryButton(text = "Edit Time", onClick = {startTimePickerDialog.show()})
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "End Time: ${eventEndTime.formatted()}", fontFamily = BodyFont)
            PrimaryButton(text = "Edit Time", onClick = {endTimePickerDialog.show()})
        }

        Spacer(Modifier.padding(bottom = 10.dp))
        GuestListPrompt()

        Column(
                Modifier
                        .fillMaxWidth()
                        .height(screenHeight - 485.dp)
                        .verticalScroll(rememberScrollState())
        ) {
            var guestCount by remember { mutableStateOf(0) }
            repeat(guestCount){ index ->
                var guestName by remember { mutableStateOf(eventGuestNameList[index]) }
                var stayTheNight by remember { mutableStateOf(eventGuestSTNList[index]) }
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    OutlinedTextField(
                            modifier = Modifier.width(screenWidth - 150.dp),
                            value = guestName,
                            onValueChange = { eventGuestNameList[index] = it
                                            guestName = it},
                            label = { Text(text = "Guest Name") },
                            singleLine = true,
                    )
                    Checkbox(
                            checked = stayTheNight,
                            onCheckedChange = { eventGuestSTNList[index] = it
                                              stayTheNight = it},
                    )
                }
            }

            Button(
                    onClick = {
                        eventGuestNameList.add("")
                        eventGuestSTNList.add(false)
                        guestCount++
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = BluePrimary),
                    content = {Text(text = "Add Guest", color = White, fontFamily = BodyFont)},
            )
        }

        val context = LocalContext.current
        val text = "Common space event added!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)

        Row(
                modifier = Modifier
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
        ){
            PrimaryButton(text = "Cancel", onClick = { navigator.navigate(CommonSpaceScreenDestination()) })
            Spacer(Modifier.padding(end = 10.dp))
            PrimaryButton(text = "Add", enabled = notBlankEventName(eventName), onClick = {
                navigator.navigate(CommonSpaceScreenDestination())
                val temp = CommonSpaceEvent(
                        homeId = homeId,
                        name = eventName,
                        host = eventHost,
                        date = eventDate,
                        startTime = eventStartTime,
                        endTime = eventEndTime,
                        guestNameList = eventGuestNameList,
                        guestSTNList = eventGuestSTNList,
                )
                viewModel.addCommonSpaceEvent(temp)
                toast.show()
            })
        }
    }
}

@Composable
fun GuestListPrompt(){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = "Guest List:", fontSize = 18.sp, fontFamily = BodyFont)
        Text(text = "Stay the Night", fontSize = 18.sp, fontFamily = BodyFont)
    }
}

@Composable
fun getAllRoommates(): List<String> {
    val dbManager = DatabaseManager.shared
    val userManager = UserManager.shared
    val context = LocalContext.current
    userManager.loadUserInfo(context)
    val usersInHouse = dbManager.getUsersInHouse(userManager.homeId!!)

    //Get Current user
    val userId = UserManager.shared.userId!!
    val userData = DatabaseManager.shared.getUserData(userId)
    val userName = userData?.get("name")

    val users = mutableListOf<String>()
    for (user in usersInHouse) {
        if (user["name"] != null && user["name"] != userName) {
            users.add(user["name"].toString())
        }
    }
    return users.toList()
}

private fun LocalTime.formatted(): String {
    val timeFormat = DateTimeFormatter.ofPattern("hh:mm a")
    return timeFormat.format(this)
}

private fun notBlankEventName(name: String): Boolean{
    return name.isNotBlank()
}
