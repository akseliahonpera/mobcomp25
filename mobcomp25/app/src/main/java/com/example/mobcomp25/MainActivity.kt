package com.example.mobcomp25

/*
https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room#8
https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#4
https://www.youtube.com/watch?v=0FKoeJmlRaU
https://stackoverflow.com/questions/68046535/lazycolumn-and-mutable-list-how-to-update
https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room#7
 */

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobcomp25.data.DataBaseHost
import com.example.mobcomp25.ui.screens.Home
import com.example.mobcomp25.ui.screens.Navigation
import com.example.mobcomp25.ui.theme.Mobcomp25Theme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){
        isGranted ->
        if (isGranted){
            Log.i("tonttu","Permission already given")
        }
        else{
            Log.i("tonttu","Permission denied")
        }
    }

    private fun requestCameraPermission(){
        when{
            ContextCompat.checkSelfPermission(
                    this,
                android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED->{
                Log.i("tonttu","Permission already given")
                    }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            )->
                Log.i("tonttu","Show camera permissions")
            else -> requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun requestImagesPermission(){
        when{
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED->{
                Log.i("tonttu","Permission already given")
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            )->
                Log.i("tonttu","Show camera permissions")
            else -> requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    private fun requestNotificationsPermission(){
        when{
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED->{
                Log.i("tonttu","Permission already given")
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )->
                Log.i("tonttu","Show camera permissions")
            else -> requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }


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
      //  requestCameraPermission()
      //  requestImagesPermission()
        requestNotificationsPermission()
    }
}
