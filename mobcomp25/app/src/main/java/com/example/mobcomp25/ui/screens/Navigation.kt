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
fun  Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home"){
        composable("home") {
            Home(navController)
        }
        composable("newNote") {
            NewNote(navController)
        }
        composable("options") {
            Options(navController)
        }
        composable("simple_camera") { SimpleCamera() }
    }
}
