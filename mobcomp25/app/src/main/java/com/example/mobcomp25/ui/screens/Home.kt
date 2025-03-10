package com.example.mobcomp25.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.mobcomp25.services.ApplicationServices
import com.example.mobcomp25.services.NotificationHandler

@Composable
fun Home(navController:NavController){
    val db = ApplicationServices.getInstance()?.getDatabase()
    //ei voi olla  nulli
    val notes by db!!.noteDao().getAll().collectAsState(initial = emptyList()) //use collectasstate() to parse data into list format
    Column (
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize()
){
        Text(
            text = "Olet kotona",
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
        Button(onClick = { navController.navigate("options") }, modifier = Modifier.align(Alignment.End)) {
            Text(text = "Options")
        }
        Spacer(Modifier.width(20.dp))
            Button(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .height(50.dp),
                    onClick = {
                        navController.navigate("newNote")
                    }
                        )
            {
                Text(text = "Tee muistiinpano",
                    modifier = Modifier
                            //        .fillMaxSize(),
                        .fillMaxWidth(),
                    fontSize = 30.sp
                    )
                }
    Spacer(modifier = Modifier.size(30.dp))
    LazyColumn(
        Modifier
            .padding(10.dp)
            .background(color = Color.LightGray)
            .fillMaxWidth()
            ,
        horizontalAlignment = Alignment.Start//////////////
    ){
    items(items = (notes)){note-> //list stuff in lazycolumn
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {////////////////

        Text(text = "\n"+"Muistiinpano: \n"+note.noteContents, textAlign = TextAlign.Start)  //lorki merkkijonot note-olioista
        //Show image here
        if (note.imageUri.isNotEmpty()) {
            AsyncImage(
                model = note.imageUri,
                contentDescription = "Note Image",
                modifier = Modifier.fillMaxWidth()
            )
        }
        }////////////
    }
    }
}
}