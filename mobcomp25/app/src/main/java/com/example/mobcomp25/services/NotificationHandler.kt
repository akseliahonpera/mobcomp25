package com.example.mobcomp25.services

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.mobcomp25.R
import com.example.mobcomp25.ui.screens.notification
import kotlin.random.Random

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification"

    fun showNotification(notificationTitle:String, notificationContent:String){
        var notification = NotificationCompat.Builder(context.applicationContext, notificationChannelID)
            .setSmallIcon(R.drawable.placeholder)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }

}