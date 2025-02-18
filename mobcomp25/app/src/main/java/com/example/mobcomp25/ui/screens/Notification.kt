package com.example.mobcomp25.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.example.mobcomp25.R


@Composable
fun notification(){
    val appContext = LocalContext.current.applicationContext


    var builder = NotificationCompat.Builder(appContext, "notificationChannel_id")
        .setSmallIcon(R.drawable.placeholder)
        .setContentTitle("notTest")
        .setContentText("notftestContentti"
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
}

