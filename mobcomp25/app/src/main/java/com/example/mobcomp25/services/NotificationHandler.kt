package com.example.mobcomp25.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mobcomp25.MainActivity
import com.example.mobcomp25.R
import com.example.mobcomp25.ui.screens.notification
import kotlin.random.Random

class NotificationHandler(private val context: Context) {
    //notificationhandler initialization
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification"



    fun showNotification(notificationTitle:String, notificationContent:String){

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        var notification = NotificationCompat.Builder(context.applicationContext, notificationChannelID)
            .setSmallIcon(R.drawable.placeholder)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()


        notificationManager.notify(Random.nextInt(), notification)
    }

}