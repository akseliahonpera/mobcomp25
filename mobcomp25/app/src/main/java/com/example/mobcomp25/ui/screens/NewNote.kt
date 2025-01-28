package com.example.mobcomp25.ui.screens

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobcomp25.data.AppDatabase
import com.example.mobcomp25.data.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun  NewNote(navController:NavController,db: AppDatabase){

    var noteString by remember { mutableStateOf("")}
    val noteDao = db.noteDao()
    val coroutineScope = rememberCoroutineScope() // shitgpt gave this advice, check if actually usable
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

        item{

            Button(onClick = {
                coroutineScope.launch{
                if(noteString.isNotBlank()){ //lets not save empty strings
                    val newNote = Note(noteContents = noteString)
                    noteDao.insertNote(newNote)
                    noteString = "" //clear textfield after save
                }
            }}
            )
            {
                Text(text = "Save note")
            }
        }
    }
}