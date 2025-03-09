package com.example.mobcomp25.services
//https://meetpatadia9.medium.com/local-notification-in-android-with-jetpack-compose-437b430710f3
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.mobcomp25.data.AppDatabase
import com.example.mobcomp25.data.DataBaseHost


class ApplicationServices :Application(){ //initialize studd here so that we can access them ewhere
    companion object{ //static initialization
        @Volatile
        private var instance: ApplicationServices? = null
        fun getInstance(): ApplicationServices? {
            return instance
        }
    }

    private var database: AppDatabase? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        val notificationChannel = NotificationChannel("notification",
            "notification",NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        database = DataBaseHost.getDataBase(this)
    }

    fun getDatabase(): AppDatabase? {
        return database
    }

}