package com.example.virtualhaus.views.laundry

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.R
import com.example.virtualhaus.ui.theme.BluePrimary
import com.example.virtualhaus.viewmodels.LaundryViewModel
import com.example.virtualhaus.viewmodels.formatTime
import com.example.virtualhaus.views.common.PrimaryButton
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun LaundryScreen(
    viewModel: LaundryViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    // washer state
    val washerTime by viewModel.washerTime.observeAsState(LaundryViewModel.TIME_COUNTDOWN.formatTime())
    val isWasherRunning by viewModel.washerRunning.observeAsState(false)

    //dryer state
    val dryerTime by viewModel.dryerTime.observeAsState(LaundryViewModel.DRYER_COUNTDOWN.formatTime())
    val isDryerRunning by viewModel.dryerRunning.observeAsState(false)

    val currentWasherIndex by viewModel.washerIndex.observeAsState(0)
    val currentDryerIndex by viewModel.dryerIndex.observeAsState(0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        WashingMachine(
            isRunning = isWasherRunning,
            time = washerTime,
            index = currentWasherIndex + 1,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            { time -> viewModel.setTimer(time, LaundryViewModel.MachineType.WASHER) },
            onLeft = { viewModel.decrementIndex(LaundryViewModel.MachineType.WASHER) },
            onRight = { viewModel.incrementIndex(LaundryViewModel.MachineType.WASHER) })
        Dryer(
            isRunning = isDryerRunning,
            time = dryerTime,
            index = currentDryerIndex + 1,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            { time -> viewModel.setTimer(time, LaundryViewModel.MachineType.DRYER) },
            onLeft = { viewModel.decrementIndex(LaundryViewModel.MachineType.DRYER) },
            onRight = { viewModel.incrementIndex(LaundryViewModel.MachineType.DRYER) })
    }
}

@Composable
fun WashingMachine(
    isRunning: Boolean,
    time: String,
    index: Int,
    modifier: Modifier = Modifier,
    setTimer: (time: Long) -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(id = R.string.washer, index), fontSize = 30.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            LeftArrowButton {
                onLeft()
            }
            MachineImage(
                id = R.drawable.washer,
                Modifier
                    .size(150.dp)
                    .weight(1f)
            )
            RightArrowButton {
                onRight()
            }
        }

        if (isRunning) {
            Timer(time = time)
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var text by rememberSaveable { mutableStateOf(LaundryViewModel.TIME_COUNTDOWN / 60000) }
                Text(text = stringResource(id = R.string.free_to_use))
                QuantityEditor(time = text.toString(), up = {
                    text++
                }, down = { if (text > 1) text-- })
                PrimaryButton(text = stringResource(id = R.string.start),
                    onClick = {
                        setTimer((text))
                    }
                )
            }
        }

    }
}

@Composable
fun Dryer(
    isRunning: Boolean,
    time: String,
    index: Int,
    modifier: Modifier = Modifier,
    setTimer: (time: Long) -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(id = R.string.dryer, index), fontSize = 30.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            LeftArrowButton {
                onLeft()
            }
            MachineImage(
                id = R.drawable.dryer,
                Modifier
                    .size(150.dp)
                    .weight(1f)
            )
            RightArrowButton {
                onRight()
            }
        }
        if (isRunning) {
            Timer(time = time)
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var text by rememberSaveable { mutableStateOf(LaundryViewModel.DRYER_COUNTDOWN / 60000) }
                Text(text = stringResource(id = R.string.free_to_use))
                QuantityEditor(time = text.toString(), up = {
                    text++
                }, down = { if (text > 1) text-- })
                PrimaryButton(text = stringResource(id = R.string.start),
                    onClick = {
                        setTimer((text))
                    }
                )

            }
        }

    }
}

@Composable
private fun MachineImage(@DrawableRes id: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(8.dp), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = id),
            contentDescription = "Machine Image",
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun Timer(time: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = stringResource(id = R.string.in_use))
        Text(text = stringResource(id = R.string.time_left, time))
    }

}

@Composable
private fun QuantityEditor(
    time: String,
    modifier: Modifier = Modifier,
    up: () -> Unit,
    down: () -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedButton(
            onClick = down,
            border = BorderStroke(1.dp, Color.Black),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(20.dp)
        ) {
            Text(text = stringResource(id = R.string.minus), color = Color.Black)
        }
        Text(text = time)
        OutlinedButton(
            onClick = up,
            border = BorderStroke(1.dp, Color.Black),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(20.dp)
        ) {
            Text(text = stringResource(id = R.string.plus), color = Color.Black)
        }
    }
}

@Composable
fun LeftArrowButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.size(35.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = BluePrimary),
        shape = LeftTriangle,
        onClick = {
            onClick()
        }
    ){}
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

@Composable
fun RightArrowButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.size(35.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = BluePrimary),
        shape = RightTriangle,
        onClick = {
            onClick()
        }
    ){}
}

@Preview
@Composable
fun PreviewArrow() {
    LeftArrowButton({})
}