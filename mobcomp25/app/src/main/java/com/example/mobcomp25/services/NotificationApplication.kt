package com.example.mobcomp25.services
//https://meetpatadia9.medium.com/local-notification-in-android-with-jetpack-compose-437b430710f3
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class NotificationApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel("notification",
            "notification",NotificationManager.IMPORTANCE_HIGH
        )

    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


    notificationManager.createNotificationChannel(notificationChannel)
    }
}