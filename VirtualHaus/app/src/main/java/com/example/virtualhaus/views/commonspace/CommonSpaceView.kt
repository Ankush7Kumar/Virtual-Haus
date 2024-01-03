package com.example.virtualhaus.views.commonspace

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.R
import com.example.virtualhaus.models.CommonSpaceEvent
import com.example.virtualhaus.ui.theme.BlueBackground
import com.example.virtualhaus.ui.theme.BluePrimary
import com.example.virtualhaus.ui.theme.BodyFont
import com.example.virtualhaus.ui.theme.HeaderFont
import com.example.virtualhaus.viewmodels.CommonSpaceViewModel
import com.example.virtualhaus.views.destinations.AddCommonSpaceEventScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Destination
@Composable
fun CommonSpaceScreen(navigator: DestinationsNavigator, viewModel: CommonSpaceViewModel = viewModel()) {
    val commonSpaceEvents by viewModel.getCommonSpaceEvents().observeAsState(initial = emptyList())
    Column(
            Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
    ) {
        CommonSpaceEventDisplay(navigator, commonSpaceEvents)
    }
}

@Composable
fun AddEventButton(navigator: DestinationsNavigator) {
    Button(
            onClick = {navigator.navigate(AddCommonSpaceEventScreenDestination())},
            colors = ButtonDefaults.buttonColors(backgroundColor = BluePrimary),
    )
    {
        Text(text = "Add New Event", color = Color.White, fontFamily = BodyFont)
    }
}

@Composable
fun CommonSpaceEventDisplay(navigator: DestinationsNavigator, commonSpaceEvents: List<CommonSpaceEvent>) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val images: List<Int> = listOf(R.drawable.generic_event_0, R.drawable.generic_event_1, R.drawable.generic_event_2)

    var index by remember { mutableStateOf(0) }

    if(commonSpaceEvents.isNotEmpty()){
        Row(
                Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
        ){
            Column(
                    modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 10.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
            ) {
                Button(
                        modifier = Modifier.size(35.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = BluePrimary),
                        shape = LeftTriangle,
                        onClick = {
                            if(index > 0){
                                index--
                            }else{
                                index = commonSpaceEvents.size - 1
                            }
                        }
                ){}
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Box(modifier = Modifier
                        .width(screenWidth - 90.dp)
                        .height(screenHeight - 200.dp)
                        .padding(top = 50.dp, bottom = 40.dp, start = 10.dp, end = 10.dp)
                        .background(BlueBackground)
                ){
                    Column(
                            modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                    ){
                        Image(
                                painter = painterResource(id = images[index % images.size]),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth()
                        )
                        DisplayEvent(commonSpaceEvents[index])
                    }
                }
                AddEventButton(navigator)
            }



            Column(
                    modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 10.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
            ) {
                Button(
                        modifier = Modifier.size(35.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = BluePrimary),
                        shape = RightTriangle,
                        onClick = {
                            if(index < commonSpaceEvents.size - 1){
                                index++
                            }else{
                                index = 0
                            }
                        }
                ){}
            }
        }
    }else{
        EmptyEventsDisplay(navigator)
    }
}

@Composable
private fun DisplayEvent(event: CommonSpaceEvent){

    Column(verticalArrangement = Arrangement.Center){
        Text(text = event.name, fontSize = 24.sp, fontFamily = HeaderFont)
        Text(text = "Hosted by ${event.host}", fontSize = 15.sp, fontFamily = BodyFont)
        Text(text = "${event.date.formatted()} @ ${event.startTime.formatted()} - ${event.endTime.formatted()}", fontSize = 15.sp, modifier = Modifier.padding(bottom = 10.dp), fontFamily = BodyFont)
    }

    val guestNameList = event.guestNameList
    val guestSTNList = event.guestSTNList
    if(guestNameList.isNotEmpty()){

        Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Guest List:", fontFamily = BodyFont, fontWeight = FontWeight.Bold)
            Text(text = "Stay the Night", fontFamily = BodyFont, fontWeight = FontWeight.Bold)
        }

        Column(Modifier.verticalScroll(rememberScrollState())){
            for(i in guestNameList.indices){
                Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = guestNameList[i], fontFamily = BodyFont)
                    if(guestSTNList[i]){
                        Text(text = "Yes", fontFamily = BodyFont)
                    }else{
                        Text(text = "No", fontFamily = BodyFont)
                    }
                }
            }
        }

    }
}

@Composable
fun EmptyEventsDisplay(navigator: DestinationsNavigator){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                    text = "You have no events to display!",
                    fontFamily = HeaderFont,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
            )
            Text(
                    text = "Add events using the add button.",
                    fontFamily = BodyFont,
                    fontSize = 14.sp,
            )

            Spacer(Modifier.padding(bottom = 50.dp))
            AddEventButton(navigator)
        }
    }
}

private val LeftTriangle = GenericShape { size, _ ->
    moveTo(size.width, size.height)
    lineTo(size.width, 0f)
    lineTo(0f, size.height / 2f)
}

private val RightTriangle = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(0f, size.height)
    lineTo(size.width, size.height / 2f)
}

private fun Date.formatted(): String {
    val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

private fun LocalTime.formatted(): String {
    val timeFormat = DateTimeFormatter.ofPattern("hh:mm a")
    return timeFormat.format(this)
}
