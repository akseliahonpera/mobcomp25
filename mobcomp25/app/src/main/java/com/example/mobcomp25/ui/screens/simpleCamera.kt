package com.example.mobcomp25.ui.screens
//https://medium.com/@dheerubhadoria/capturing-images-from-camera-in-android-with-jetpack-compose-a-step-by-step-guide-64cd7f52e5de
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil3.Image
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@Composable
fun SimpleCamera(){
    val context = LocalContext.current //get context
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile( //create uri for the new image file
        Objects.requireNonNull(context),
        "com.example.mobcomp25" + ".provider", file
    )

    var newImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY) //placeholder for new image uri, for convinience
    }
    val simpleCameraLauncher = //initialize launcher for camera, vrt intent
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            newImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            simpleCameraLauncher.launch(uri) //launch  camera and save the image to uri  address
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    //run permission check if our composable is called
    val permissionCheckResult =
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        simpleCameraLauncher.launch(uri)
    } else {
        // Request a permission
        permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                simpleCameraLauncher.launch(uri)
            } else {
                // Request a permission
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }) {
            Text(text = "Capture Image From Camera")
        }
    }

    if (newImageUri.path?.isNotEmpty() == true) {
        AsyncImage(
            modifier = Modifier
                .padding(16.dp, 8.dp),
            model = newImageUri,
            contentDescription = null
        )
    }


}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}



/*
@Composable
fun SimpleCamera(){
    val context = LocalContext.current //get context
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile( //create uri for the new image file
        Objects.requireNonNull(context),
        "com.example.mobcomp25" + ".provider", file
    )

    var newImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY) //placeholder for new image uri, for convinience
    }
    val simpleCameraLauncher = //initialize launcher for camera, vrt intent
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            newImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            simpleCameraLauncher.launch(uri) //launch  camera and save the image to uri  address
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    //run permission check if our composable is called
    val permissionCheckResult =
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        simpleCameraLauncher.launch(uri)
    } else {
        // Request a permission
        permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }
    /*
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    simpleCameraLauncher.launch(uri)
                } else {
                    // Request a permission
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }) {
                Text(text = "Capture Image From Camera")
            }
        }

        if (newImageUri.path?.isNotEmpty() == true) {
            AsyncImage(
                modifier = Modifier
                    .padding(16.dp, 8.dp),
                model = newImageUri,
                contentDescription = null
            )
        }
        */

}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

*/