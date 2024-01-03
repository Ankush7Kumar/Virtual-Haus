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
import com.example.virtualhaus.views.common.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun JoinHomeScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val userManager = UserManager.shared
    val dbManager = DatabaseManager.shared

    var name by rememberSaveable { mutableStateOf("") }
    var nameIsValid by rememberSaveable { mutableStateOf(false) }
    var roomNumber by rememberSaveable { mutableStateOf("") }
    var roomNumberIsValid by rememberSaveable { mutableStateOf(false) }
    var uniqueHomeCode by rememberSaveable { mutableStateOf("") }
    var uniqueHomeCodeIsValid by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Title(text = "Join House")

        NameTextField(
            label = "Name",
            value = name,
            isValid = nameIsValid,
            onValueChange = { text, isValid -> name = text; nameIsValid = isValid },
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )
        IntegerTextField(
            label = "Room number",
            value = roomNumber,
            isValid = roomNumberIsValid,
            onValueChange = { text, isValid -> roomNumber = text; roomNumberIsValid = isValid },
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )
        NonEmptyTextField(
            label = "Unique Home Code",
            value = uniqueHomeCode,
            isValid = uniqueHomeCodeIsValid,
            onValueChange = { text, isValid -> uniqueHomeCode = text; uniqueHomeCodeIsValid = isValid },
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )

        Row(modifier = Modifier.padding(20.dp)) {
            PrimaryButton(text = "Cancel") { navigator.navigateUp() }

            Spacer(modifier = Modifier.width(10.dp))

            val inputIsValid = nameIsValid && roomNumberIsValid && uniqueHomeCodeIsValid
            PrimaryButton(text = "Join", enabled = inputIsValid) {
                val userId = dbManager.addNewUser(
                    homeId = uniqueHomeCode,
                    name = name,
                    roomNumber = roomNumber.toInt(),
                    owner = false,
                )

                userManager.setHomeId(context, uniqueHomeCode)
                userManager.setUserId(context, userId)

                navigator.navigate(INITIAL_DESTINATION_AFTER_LOGIN)
            }
        }
    }
}