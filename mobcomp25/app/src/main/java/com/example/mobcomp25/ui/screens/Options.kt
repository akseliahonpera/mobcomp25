package com.example.mobcomp25.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.mobcomp25.services.ApplicationServices
import kotlinx.coroutines.launch

@Composable
fun Options(navController: NavController){
    val db = ApplicationServices.getInstance()?.getDatabase()
    val dao = db!!.noteDao() //db is created on application startup
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center


    ) {
        Button(onClick = {
            coroutineScope.launch { db.clearAllTables() }
            Toast.makeText(context,"Muistiinpanot poistettu",Toast.LENGTH_LONG).show()}) {
            Text(text = "Poista kaikki tehdyt muistiinpanot")
        }
        Button(onClick = { navController.navigate("home"){
            popUpTo("home"){
                inclusive = true
            }
        } }) {
            Text(text = "Palaa kotisivulle")
        }
    }
}

//https://coldfusion-example.blogspot.com/2025/01/how-to-safely-delete-all-room-data-in.html