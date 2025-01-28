package com.example.mobcomp25.ui.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobcomp25.data.AppDatabase
import com.example.mobcomp25.data.DataBaseHost
import com.example.mobcomp25.data.NoteRepoTest


@Composable
fun  Navigation(db: AppDatabase) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home"){
        composable("home") {
            Home(navController, db)
        }
        composable("newNote") {
            NewNote(navController, db)
        }
    }
}


/* Varmuuskopsu
@Composable
fun  Navigation(context: Context) {
    val navController = rememberNavController()
    val db = DataBaseHost.getDataBase(context)
    NavHost(navController = navController, startDestination = "home"){
        composable("home") {
            Home(navController, db)
        }
        composable("newNote") {
            NewNote(navController, db)
        }
    }
}
 */