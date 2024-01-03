package com.example.virtualhaus.views.login

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.virtualhaus.models.DatabaseManager
import com.example.virtualhaus.models.UserManager
import com.example.virtualhaus.views.common.IntegerTextField
import com.example.virtualhaus.views.common.NameTextField
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.common.Title
import com.example.virtualhaus.views.destinations.UniqueHomeCodeScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun CreateHomeScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val userManager = UserManager.shared
    val dbManager = DatabaseManager.shared

    var houseName by rememberSaveable { mutableStateOf("") }
    var houseNameIsValid by rememberSaveable { mutableStateOf(false) }
    var ownerName by rememberSaveable { mutableStateOf("") }
    var ownerNameIsValid by rememberSaveable { mutableStateOf(false) }
    var roomNumber by rememberSaveable { mutableStateOf("") }
    var roomNumberIsValid by rememberSaveable { mutableStateOf(false) }
    var bathNumber by rememberSaveable { mutableStateOf("1") }
    var bathNumberIsValid by rememberSaveable { mutableStateOf(true) }
    var washerNumber by rememberSaveable { mutableStateOf("1") }
    var washerNumberIsValid by rememberSaveable { mutableStateOf(true) }
    var dryerNumber by rememberSaveable { mutableStateOf("1") }
    var dryerNumberIsValid by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Title(text = "Create House")

        NameTextField(
            label = "Name of House",
            value = houseName,
            isValid = houseNameIsValid,
            onValueChange = { text, isValid -> houseName = text; houseNameIsValid = isValid },
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )
        NameTextField(
            label = "Your name",
            value = ownerName,
            isValid = ownerNameIsValid,
            onValueChange = { text, isValid -> ownerName = text; ownerNameIsValid = isValid },
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )
        IntegerTextField(
            label = "Room number",
            value = roomNumber,
            isValid = roomNumberIsValid,
            onValueChange = { text, isValid -> roomNumber = text; roomNumberIsValid = isValid },
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )
        IntegerTextField(
            label = "Number of bathrooms",
            value = bathNumber,
            isValid = bathNumberIsValid,
            onValueChange = { text, isValid -> bathNumber = text; bathNumberIsValid = isValid },
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )
        IntegerTextField(
            label = "Number of washers",
            value = washerNumber,
            isValid = washerNumberIsValid,
            onValueChange = { text, isValid -> washerNumber = text; washerNumberIsValid = isValid },
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )
        IntegerTextField(
            label = "Number of dryers",
            value = dryerNumber,
            isValid = dryerNumberIsValid,
            onValueChange = { text, isValid -> dryerNumber = text; dryerNumberIsValid = isValid },
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )

        Row(modifier = Modifier.padding(20.dp)) {
            PrimaryButton(text = "Cancel") { navigator.navigateUp() }

            Spacer(modifier = Modifier.width(10.dp))

            val inputsAreValid = houseNameIsValid &&
                    ownerNameIsValid &&
                    roomNumberIsValid &&
                    bathNumberIsValid &&
                    washerNumberIsValid &&
                    dryerNumberIsValid

            PrimaryButton(text = "Create Home", enabled = inputsAreValid) {
                val homeId = dbManager.createHome(
                    houseName = houseName,
                    numberOfWashrooms = bathNumber.toInt(),
                    numberOfWashers = washerNumber.toInt(),
                    numberOfDryers = dryerNumber.toInt(),
                )
                val userId = dbManager.addNewUser(
                    homeId = homeId,
                    name = ownerName,
                    roomNumber = roomNumber.toInt(),
                    owner = true,
                )

                userManager.setHomeId(context, homeId)
                userManager.setUserId(context, userId)

                navigator.navigate(UniqueHomeCodeScreenDestination)
            }
        }
    }
}
