package com.example.mobcomp25.services
//https://stackoverflow.com/questions/77434691/kotlin-alarmmanager-setexact-never-fires
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.material3.Text

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context!=null) {
            NotificationHandler(context)
                .showNotification("Muistutus", "testileipä")
        }
    }
}


