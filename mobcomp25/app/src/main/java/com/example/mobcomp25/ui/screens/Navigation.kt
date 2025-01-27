package com.example.mobcomp25.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun  Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "newNote"){
        composable("home") {
            Home(navController)
        }
        composable("newNote") {
            NewNote(navController)
        }
    }
}