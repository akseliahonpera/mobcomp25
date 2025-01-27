package com.example.mobcomp25.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun  NewNote(navController:NavController){

    Text(text = "NewNote Screen")
    
    
    Button(onClick = { navController.navigate("home") }) {
        
    }
}