package com.example.virtualhaus.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.virtualhaus.models.UserManager
import com.example.virtualhaus.ui.theme.BodyFont
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.destinations.InitialWelcomeScreenDestination
import com.example.virtualhaus.views.login.UniqueHomeCodeContent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SettingScreen(navigator: DestinationsNavigator){
    val context = LocalContext.current
    val userManager = UserManager.shared
    var shouldShowLeaveConfirmation by remember { mutableStateOf(false) }

    if (shouldShowLeaveConfirmation) {
        AlertDialog(
            onDismissRequest = { shouldShowLeaveConfirmation = false },
            title = {
                Text(
                    text = "Are you sure you want to leave the house?",
                    fontFamily = BodyFont,
                    fontSize = 20.sp,
                )
            },
            text = {
                Text(text = "This will permanently delete all your data.",  fontFamily = BodyFont)
            },
            confirmButton = {
                Button(
                    content = { Text(text = "Leave", fontFamily = BodyFont, color = Color.White) },
                    onClick = {
                        userManager.setHomeId(context, null)
                        navigator.navigate(InitialWelcomeScreenDestination)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    modifier = Modifier.fillMaxWidth(0.49F),
                )
            },
            dismissButton = {
                PrimaryButton(
                    text = "Cancel",
                    modifier = Modifier.fillMaxWidth(0.48F),
                    onClick = { shouldShowLeaveConfirmation = false },
                )
            },
        )
    }

    UniqueHomeCodeContent { modifier ->
        Button(
            onClick = { shouldShowLeaveConfirmation = true },
            modifier = modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        ) {
            Text(text = "Leave House", fontFamily = BodyFont, color = Color.White)
        }
    }
}