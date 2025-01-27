package com.example.mobcomp25.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun  NewNote(navController:NavController){

    var noteString by remember { mutableStateOf("")}
    LazyColumn(
        Modifier
            .padding(10.dp)
    ) {

        item {
            Text(text = "Write a new note",
                fontSize = 30.sp
            )
        }
        item {
            Button(
                onClick = { navController.navigate("home"){
                    popUpTo("home"){
                        inclusive = true
                    }
                } },
                modifier = Modifier
                    .height(50.dp)
                    .width(200.dp)
            )
            {
                Text(text = "Palaa kotiruutuun")
            }
        }
        item{
            TextField(value = noteString, onValueChange = { noteString = it }
            ,modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp))
        }
    }
}