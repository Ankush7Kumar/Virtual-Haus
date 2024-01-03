package com.example.virtualhaus.views.login

import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import com.example.virtualhaus.models.UserManager
import com.example.virtualhaus.views.common.INITIAL_DESTINATION_AFTER_LOGIN
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.common.TextBox
import com.example.virtualhaus.views.common.Title
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

private const val MILLIS_IN_SECOND = 1000

@Destination
@Composable
fun UniqueHomeCodeScreen(navigator: DestinationsNavigator) {
    UniqueHomeCodeContent(header = "House Confirmation") { modifier ->
        PrimaryButton(text = "Proceed to VirtualHaus", modifier = modifier) {
            navigator.navigate(INITIAL_DESTINATION_AFTER_LOGIN)
        }
    }
}

@Composable
fun UniqueHomeCodeContent(header: String? = null, actionButton: @Composable (modifier: Modifier) -> Unit) {
    val homeId = UserManager.shared.homeId ?: ""
    val context = LocalContext.current

    var snackbarMessage: String? by remember { mutableStateOf(null) }
    val scaffoldState = rememberScaffoldState()

    snackbarMessage?.let {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(it)
            delay(2L * MILLIS_IN_SECOND) // delay 2 seconds then dismiss
            snackbarMessage = null
        }
    }

    Scaffold(scaffoldState = scaffoldState, modifier = Modifier.padding(20.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            header?.let { Title(text = it) }

            TextBox(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                value = homeId,
                label = "Your unique home ID is:",
            )

            Row {
                PrimaryButton(text = "Copy to Clipboard", modifier = Modifier.heightIn(min = 60.dp)) {
                    val clipboardManager = getSystemService(context, ClipboardManager::class.java)

                    // assignment to snackbarMessage will cause the scaffold state to pop a snackbar
                    snackbarMessage = if (clipboardManager == null) {
                        "There was an error while copying your Home ID."
                    } else {
                        val clipData = ClipData.newPlainText("Home ID", homeId)
                        clipboardManager.setPrimaryClip(clipData)
                        "Your Home ID was copied to the clipboard."
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                actionButton(modifier = Modifier.heightIn(min = 60.dp))
            }
        }
    }
}
