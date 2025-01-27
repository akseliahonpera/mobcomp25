package com.example.mobcomp25.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobcomp25.R
import java.time.format.TextStyle


@Composable
fun Home(navController:NavController){

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
        Spacer(Modifier.width(20.dp))
            Button(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = Color.Gray)
                    .height(50.dp),
                    onClick = {
                        navController.navigate("newNote")

                    }
                        )

            {
                Text(text = "Write a note",
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth(),
                    fontSize = 30.sp
                    )
                }

    LazyColumn(
        Modifier
            .padding(10.dp)
            .background(color = Color.LightGray)
    ){

        item{
            Text(text = "Esine1")
        }
        item{
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }
        item {
            Text(text = "Esine1")
        }


    }






}
}