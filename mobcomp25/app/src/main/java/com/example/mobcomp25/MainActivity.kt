package com.example.mobcomp25

/*
https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room#8
https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#4
https://www.youtube.com/watch?v=0FKoeJmlRaU
https://stackoverflow.com/questions/68046535/lazycolumn-and-mutable-list-how-to-update
https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room#7
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobcomp25.data.DataBaseHost
import com.example.mobcomp25.ui.screens.Home
import com.example.mobcomp25.ui.screens.Navigation
import com.example.mobcomp25.ui.theme.Mobcomp25Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mobcomp25Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val db = DataBaseHost.getDataBase(this) // testi1
                    Navigation(db) //pass db as parameter
                }
            }
        }
    }
}
