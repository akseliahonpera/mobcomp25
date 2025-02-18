package com.example.mobcomp25.ui.screens
//https://developer.android.com/develop/ui/compose/components/time-pickers
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.util.Calendar
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun setTime(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
    ){
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    Column {
        TimePicker(
            state = timePickerState,
        )
        Button(onClick = onDismiss) {
            Text("Cancel")
        }
        Button(onClick = { onConfirm(timePickerState)}) {
            Text(text = "Confirm")
        }
    }
}

