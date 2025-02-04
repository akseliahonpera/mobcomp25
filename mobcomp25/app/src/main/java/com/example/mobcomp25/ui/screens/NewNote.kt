package com.example.mobcomp25.ui.screens

import android.content.ContentResolver
import android.content.Context
import android.content.UriPermission
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.mobcomp25.data.AppDatabase
import com.example.mobcomp25.data.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import java.io.File


@Composable
fun  NewNote(navController:NavController,db: AppDatabase){

    var noteString by remember { mutableStateOf("")}
    val noteDao = db.noteDao()
    val coroutineScope = rememberCoroutineScope() // shitgpt gave this advice, check if actually usable
    var imgUri : String? = null
    val appContext = LocalContext.current.applicationContext

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
                    .height(350.dp))
        }


        item{
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Take a photo _WIP")
            }
        }
        item {

            var imageUriAddress by remember {
                mutableStateOf<Uri?>(null) // Uri osoitteen alustus
            }
            val launcher = rememberLauncherForActivityResult(
                contract =
                ActivityResultContracts.GetContent() //pick content from storage
            ) { uri: Uri? ->
                imageUriAddress =  uri //uri address set to imageuriaddress if not null
                imgUri = imageUriAddress.toString()

                if (uri != null) {
                    val saveDestination = saveImgToInternal(context = appContext, uri=uri)
                    imgUri = saveDestination.toString()
                }
            }
            // Open a specific media item using InputStream.

            AsyncImage(model = imageUriAddress, contentDescription = null,
                modifier = Modifier
                    .padding(20.dp)
            )
            Button(onClick = {
                launcher.launch("image/*")
            }) {
                Text(text = "Choose image from library")
            }
        }

        item{

            Button(onClick = {
                coroutineScope.launch{
                    if(noteString.isNotBlank()&&!imgUri.isNullOrEmpty()){ //lets not save empty strings
                        val newNote = Note(noteContents = noteString, imageUri = imgUri!!)
                        noteDao.insertNote(newNote)
                        noteString = "" //clear textfield after save
                        imgUri = null
                        navController.popBackStack()
                    }
                }}
            )
            {
                Text(text = "Save note")
            }
        }
    }
}

fun saveImgToInternal(context: Context,uri: Uri): Uri {
    val iStream = context.contentResolver.openInputStream(uri)  //stackoverflowsta apinoitu osittain, osittain dokumentaatiosta
    val fName = "saved_image_${System.currentTimeMillis()}.jpg" //erotetaan filut toisistaan//shitgpt idea
    val oStream = context.openFileOutput(fName,Context.MODE_PRIVATE)

    iStream?.use{input->
        oStream.use{
            output->
            input.copyTo(output)
        }
    }
    val file = File(context.filesDir,fName)
    return Uri.fromFile(file)
}
