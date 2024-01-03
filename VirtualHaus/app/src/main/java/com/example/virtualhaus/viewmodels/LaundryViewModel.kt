package com.example.virtualhaus.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.DatabaseManager
import com.example.virtualhaus.models.LaundryService
import com.example.virtualhaus.models.LaundryServiceImpl
import com.example.virtualhaus.models.UserManager
import com.google.firebase.Timestamp
import java.util.concurrent.TimeUnit

class LaundryViewModel : ViewModel() {
    private val homeId = UserManager.shared.homeId!!
    private val laundryService: LaundryService = LaundryServiceImpl()

    var countDownTimerWasher: CountDownTimer? = null

    var countDownTimerDryer: CountDownTimer? = null

    var currentWasherIndex = 0
    var currentDryerIndex = 0
    var numWashers = 1L
        private set

    var numDryers = 1L
        private set

    //washer states
    private val _washerTime = MutableLiveData(TIME_COUNTDOWN.formatTime())
    val washerTime: LiveData<String> = _washerTime

    private val _washerRunning = MutableLiveData(false)
    val washerRunning: LiveData<Boolean> = _washerRunning

    private val _washerIndex = MutableLiveData(0)
    val washerIndex: LiveData<Int> = _washerIndex

    //dryer state
    private val _dryerTime = MutableLiveData(DRYER_COUNTDOWN.formatTime())
    val dryerTime: LiveData<String> = _dryerTime

    private val _dryerRunning = MutableLiveData(false)
    val dryerRunning: LiveData<Boolean> = _dryerRunning

    private val _dryerIndex = MutableLiveData(0)
    val dryerIndex: LiveData<Int> = _dryerIndex

    init {
        val laundryData = DatabaseManager.shared.getLaundryData(homeId)
        numWashers = laundryData.first
        numDryers = laundryData.second
        getWasherInfo(MachineType.WASHER)
        getWasherInfo(MachineType.DRYER)
    }

    private fun getWasherInfo(type: MachineType) {
        val index = if (type == MachineType.WASHER) currentWasherIndex else currentDryerIndex
        laundryService.getWasherData(index, type) {
            val start = it.startTime.seconds
            val timer = it.timer
            val washerStopTime = start + timer
            val currentTime = Timestamp.now().seconds

            val running = when (type) {
                MachineType.WASHER -> _washerRunning
                MachineType.DRYER -> _dryerRunning
            }
            val time = when (type) {
                MachineType.WASHER -> _washerTime
                MachineType.DRYER -> _dryerTime
            }
            if (currentTime >= washerStopTime) {
                running.value = false
                time.value =
                    if (type == MachineType.WASHER) TIME_COUNTDOWN.formatTime() else DRYER_COUNTDOWN.formatTime()
            } else {
                startTimer((washerStopTime - currentTime) * 1000, running, time, type)
            }
        }

    }

    fun incrementIndex(type: MachineType) {
        when (type) {
            MachineType.WASHER -> {
                countDownTimerWasher?.cancel()
                countDownTimerWasher = null
                if (currentWasherIndex < numWashers - 1) {
                    currentWasherIndex++
                    _washerIndex.value = currentWasherIndex
                }
            }
            MachineType.DRYER -> {
                countDownTimerDryer?.cancel()
                countDownTimerDryer = null
                if (currentDryerIndex < numDryers - 1) {
                    currentDryerIndex++
                    _dryerIndex.value = currentDryerIndex
                }
            }
        }
        getWasherInfo(type)
    }

    fun decrementIndex(type: MachineType) {
        when (type) {
            MachineType.WASHER -> {
                countDownTimerWasher?.cancel()
                countDownTimerWasher = null
                if (currentWasherIndex > 0) {
                    currentWasherIndex--
                    _washerIndex.value = currentWasherIndex
                }
            }
            MachineType.DRYER -> {
                countDownTimerDryer?.cancel()
                countDownTimerDryer = null
                if (currentDryerIndex > 0) {
                    currentDryerIndex--
                    _dryerIndex.value = currentDryerIndex
                }
            }
        }
        getWasherInfo(type)
    }


    //set timer function called to start the respective timers.
    fun setTimer(time: Long, type: MachineType) {
        when (type) {
            MachineType.WASHER -> {
                laundryService.updateWasherData(
                    currentWasherIndex,
                    time.toSeconds(),
                    MachineType.WASHER
                ) {
                    getWasherInfo(MachineType.WASHER)
                }
            }
            MachineType.DRYER -> {
                laundryService.updateWasherData(
                    currentDryerIndex,
                    time.toSeconds(),
                    MachineType.DRYER
                ) {
                    getWasherInfo(MachineType.DRYER)
                }
            }
        }
    }

    //handles the logic behind decreasing timer using the Countdown Timer, included as a part of android.os package
    private fun startTimer(
        time: Long,
        running: MutableLiveData<Boolean>,
        value: MutableLiveData<String>,
        type: MachineType
    ) {
        //running.value = true
        val countDownTimer = object : CountDownTimer(time, 1000L) {
            override fun onTick(millisRemaining: Long) {
                handleTimerValues(true, millisRemaining.formatTime(), running, value)
            }

            override fun onFinish() {
                cancel()
                handleTimerValues(false, TIME_COUNTDOWN.formatTime(), running, value)
            }
        }.start()
        if (type == MachineType.WASHER) {
            countDownTimerWasher?.cancel()
            countDownTimerWasher = countDownTimer
        } else {
            countDownTimerDryer?.cancel()
            countDownTimerDryer = countDownTimer
        }
    }

    private fun handleTimerValues(
        isPlaying: Boolean,
        text: String,
        running: MutableLiveData<Boolean>,
        value: MutableLiveData<String>
    ) {
        running.value = isPlaying
        value.value = text
    }

    companion object {
        const val TIME_COUNTDOWN: Long = 2100000L
        const val DRYER_COUNTDOWN: Long = 3600000L
        const val TIME_FORMAT = "%02d:%02d"
    }

    //enum class to represent which machine to start.
    enum class MachineType {
        WASHER,
        DRYER,
    }

}

//convert time to milli seconds
fun Long.formatTime(): String = String.format(
    LaundryViewModel.TIME_FORMAT,
    TimeUnit.MILLISECONDS.toMinutes(this),
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)

fun Long.toSeconds() = run { this * 60 }