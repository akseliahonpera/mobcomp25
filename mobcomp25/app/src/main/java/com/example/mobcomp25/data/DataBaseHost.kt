package com.example.mobcomp25.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.internal.synchronizedImpl
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DataBaseHost {

    private var dbInstance: AppDatabase? = null


    @OptIn(InternalCoroutinesApi::class)
    fun getDataBase(context: Context): AppDatabase {
            if (dbInstance == null) {
                System.out.println("iis datapase1");
                synchronized(this) {
                    dbInstance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java, "NoteDB"
                    ).build()
                    if(dbInstance==null){
                        Log.i("paskaa","db kusahti")
                    }

                }
            }
        return dbInstance!!
    }
}