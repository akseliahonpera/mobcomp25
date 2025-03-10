package com.example.mobcomp25.ui.screens

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.UriPermission
import android.content.pm.PackageManager
import android.health.connect.datatypes.units.Length
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.AlarmManagerCompat.setAlarmClock
import androidx.core.app.AlarmManagerCompat.setExact
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.mobcomp25.data.AppDatabase
import com.example.mobcomp25.data.Note
import com.example.mobcomp25.services.AlarmReceiver
import com.example.mobcomp25.services.ApplicationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects

//https://developer.android.com/develop/ui/compose/components/time-pickers
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  NewNote(navController:NavController){
    val db = ApplicationServices.getInstance()?.getDatabase()
    //ei voi olla  nulli

    var noteString by remember { mutableStateOf("")}
    val noteDao = db!!.noteDao()
    val coroutineScope = rememberCoroutineScope() // shitgpt gave this advice, check if actually usable
    var imgUri : String? = null
    val appContext = LocalContext.current.applicationContext
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }
    var showTimePicker by remember { mutableStateOf(false) }

    var noteLocation by remember { mutableStateOf<Pair<Double,Double>?>(null) }
    var longitude = 0.0
    var latitude = 0.0

    val LocationshowLibraryPermissionDialog = remember { mutableStateOf(false) }
    if (LocationshowLibraryPermissionDialog.value) {
        AlertDialog(
            onDismissRequest = { LocationshowLibraryPermissionDialog.value = false },
            title = { Text(text = "Location permission", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Text(
                    "Permission to access location is needed for use of this functionality",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        LocationshowLibraryPermissionDialog.value = false
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(context, intent, null)
                    }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        LocationshowLibraryPermissionDialog.value = false
                    }) {
                    Text("Cancel")
                }
            },
        )
    }
    val LocationpermissionLauncherLibrary = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Lupa annettu", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(context, "Lupa evätty", Toast.LENGTH_SHORT).show()
            LocationshowLibraryPermissionDialog.value = true
        }
    }

    //////////////kameran toiminnallisuuksia alkaa
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

    //////////////kameran toiminnallisuuksia päättyy

    ////////////////////////////////////////////////testiä


    ////////////////////////lupa kirjaston käyttöön alkaa
    val showLibraryPermissionDialog = remember { mutableStateOf(false) }
    if (showLibraryPermissionDialog.value) {
        AlertDialog(
            onDismissRequest = { showLibraryPermissionDialog.value = false },
            title = { Text(text = "Library permission", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Text(
                    "Permission to access library is needed for use of this functionality",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLibraryPermissionDialog.value = false
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(context, intent, null)
                    }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLibraryPermissionDialog.value = false
                    }) {
                    Text("Cancel")
                }
            },
        )
    }
    val permissionLauncherLibrary = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Lupa annettu", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(context, "Lupa evätty", Toast.LENGTH_SHORT).show()
            showLibraryPermissionDialog.value = true
        }
    }
    ////////////////////////lupa kirjaston käyttöön päättyy

    ////////////////////////lupa ilmoitusten käyttöön alkaa
    val notifiCationsshowPermissionDialog = remember { mutableStateOf(false) }
    if (notifiCationsshowPermissionDialog.value) {
        AlertDialog(
            onDismissRequest = { notifiCationsshowPermissionDialog.value = false },
            title = { Text(text = "Notifications permission", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Text(
                    "Permission to use notifications is needed for use of this functionality",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        notifiCationsshowPermissionDialog.value = false
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(context, intent, null)
                    }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        notifiCationsshowPermissionDialog.value = false
                    }) {
                    Text("Cancel")
                }
            },
        )
    }
    val notifiCationspermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Lupa annettu", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(context, "Lupa evätty", Toast.LENGTH_SHORT).show()
            notifiCationsshowPermissionDialog.value = true
        }
    }
    ////////////////////////lupa ilmoitusten käyttöön päättyy

    ////////////////////////lupa kameran käyttöön alkaa
    val showPermissionDialog = remember { mutableStateOf(false) }
    if (showPermissionDialog.value) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog.value = false },
            title = { Text(text = "Camera permission", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Text(
                    "Permission to use camera is needed for use of this functionality",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog.value = false
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(context, intent, null)
                    }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog.value = false
                    }) {
                    Text("Cancel")
                }
            },
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Lupa annettu", Toast.LENGTH_SHORT).show()
            simpleCameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Lupa evätty", Toast.LENGTH_SHORT).show()
            showPermissionDialog.value = true
        }
    }
    ////////////////////////lupa kameran käyttöön päättyy

    ////////////////////////lupa sijainnin käyttöön alkaa

    ////////////////////////lupa sijainnin käyttöön päättyy


////////////////////////////////////////////////testiä

    ///////////////////////////////////// heavily influenced by this
//https://medium.com/@chiragthummar16/requesting-permission-jetpack-compose-the-complete-guide-03e8aaa1f4a0

    ////////////////////////////////////////

    LazyColumn(
        Modifier
            .padding(10.dp)
    ) {
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
        item {
            Text(text = "Tee uusi muistiinpano",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }
        item{
            Text(text = "Aseta muistutus")
        }
        item{
            if(showDatePicker){
                DatePickerModalInput(onDateSelected = { date ->
                    selectedDate = date
                    showDatePicker = false

                }, onDismiss = {
                    showDatePicker = false
                }
                )
            }
            Button(onClick = {showDatePicker = true}) {
                Text(text = "Valitse päivä")
            }
            if(selectedDate!=null){
                val date = Date(selectedDate!!)
                val dateFormatted = SimpleDateFormat("dd, MM, yyyy", Locale.getDefault()).format(date)
                Text(text = "Date:  $dateFormatted")
            }else {
                Text(text = "You have not chosen a date")
            }
        }
        item {
            if (showTimePicker) {
                setTime(onConfirm = { time ->
                    selectedTime = time
                    showTimePicker = false
                }, onDismiss = {
                    showTimePicker = false
                },
                )
            }
            Button(onClick = {showTimePicker = true}) {
                Text(text = "Valitse kellonaika")
            }
            if (selectedTime != null) {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, selectedTime!!.hour)
                cal.set(Calendar.MINUTE, selectedTime!!.minute)
                cal.isLenient = false
                val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                Text("Selected time = ${formatter.format(cal.time)}")


            } else {
                Text("No time selected.")
            }

        }
        item{
            TextField(value = noteString, onValueChange = { noteString = it }
            ,modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp))
        }
        item{
            Button(onClick = {
                val permissionCheckResult = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    simpleCameraLauncher.launch(uri)
                } else {
                    // Request a permission
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
                

            }) {
                Text(text = "Ota kuva")
            }
            imgUri=newImageUri.toString()
            previewImage(uri = newImageUri)
        //    AsyncImage(model = newImageUri, contentDescription = null)
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
            imageUriAddress?.let { previewImage(it) }
          // AsyncImage(model = imageUriAddress, contentDescription = null, modifier = Modifier.padding(20.dp))



            Row {
            Button(onClick = {

                val permissionCheckResult = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_MEDIA_IMAGES)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    launcher.launch("image/*")
                } else {
                    // Request a permission
                    permissionLauncherLibrary.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }


            }) {
                Text(text = "Valitse kuva kirjastosta")
            }
                Button(onClick = {
                    Log.i("Lokaatio","nappi painettu")
                    val permissionCheckResult = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    Log.i("Lokaatio","permissionCheckResult")
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        Log.i("Lokaatio","permission granted")
                        getCurrentLocation(
                            onGetCurrentLocationSuccess = {location ->
                                Log.i("Lokaatio","success")
                                noteLocation=location

                            },
                            onGetCurrentLocationFailed = {
                                Log.i("Lokaatio","failure")
                                Toast.makeText(context,"Sijainnin haku epäonnistui",Toast.LENGTH_SHORT)
                            }, context = appContext

                        )

                    } else {
                        // Request a permission
                        LocationpermissionLauncherLibrary.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }) {
                    Text(text = "Lisää sijainti")
                }
            }
        }

        item{

            Button(onClick = {
                coroutineScope.launch{
                    if(noteString.isNotBlank() || !imgUri.isNullOrEmpty()){ //lets have something to save

                        if(selectedDate!=null && selectedTime!=null) {

                           val calendar = Calendar.getInstance().apply {
                               timeInMillis = selectedDate as Long
                               set(Calendar.HOUR_OF_DAY, selectedTime!!.hour)
                               set(Calendar.MINUTE, selectedTime!!.minute)
                           }
                           var notl = noteLocation.toString()
                           Log.i("tallesnus","${notl}")
                           print(noteLocation)

                           noteString = noteString+ "\n"+" Muistutus asetettu: "+ "\n"+calendar.time +"\n " + noteLocation.toString()
                       }
                        noteString = noteString+ "\n"+" Sijainti "+ "\n" + noteLocation.toString()

                        val newNote = Note(noteContents = noteString, imageUri = imgUri!!, location = noteLocation.toString())
                        noteDao.insertNote(newNote)
                        noteString = "" //clear textfield after save
                        imgUri = null


                            alarm(context = appContext,
                                selectedDate = selectedDate,
                                selectedTime = selectedTime
                            )

                        navController.popBackStack()
                    }
                }}
            )
            {
                Text(text = "Tallenna muistiinpano")
            }
        }
    }

}
@Composable
fun previewImage(uri:Uri){
    AsyncImage(model = uri, contentDescription = null)
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

@OptIn(ExperimentalMaterial3Api::class)
fun alarm(context: Context, selectedDate: Long?, selectedTime: TimePickerState?) {
    if (selectedDate == null || selectedTime == null) {
        return; // no null alarms pls
    }
    if(PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context,android.Manifest.permission.POST_NOTIFICATIONS)){

        Toast.makeText(context,"Ei o luppaa hälyjen laittoon, tallennetaan ilman muistutusta, asetukset->sovellukset",Toast.LENGTH_SHORT).show()
        return
    }


    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    //manager as alarmanager and cast
    val calendar = Calendar.getInstance().apply {
        timeInMillis = selectedDate
        set(Calendar.HOUR_OF_DAY, selectedTime.hour)
        set(Calendar.MINUTE, selectedTime.minute)
    }
    //time and date for the alarm
    val alarmIntent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        alarmIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
    Toast.makeText(context,"Muistutus asetettu ${calendar.time}", Toast.LENGTH_LONG).show() //esitetään graafinen palaute että muistutus asetettuc
}

//https://developer.android.com/develop/background-work/services/alarms/schedule

// borrowed//https://medium.com/@dheerubhadoria/capturing-images-from-camera-in-android-with-jetpack-compose-a-step-by-step-guide-64cd7f52e5de
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
//https://medium.com/@munbonecci/how-to-get-your-location-in-jetpack-compose-f085031df4c1 borrowed from here, doesnt work, idc anymore, maybe fixing it later
/**
 * Retrieves the current user location asynchronously.
 *
 * @param onGetCurrentLocationSuccess Callback function invoked when the current location is successfully retrieved.
 *        It provides a Pair representing latitude and longitude.
 * @param onGetCurrentLocationFailed Callback function invoked when an error occurs while retrieving the current location.
 *        It provides the Exception that occurred.
 * @param priority Indicates the desired accuracy of the location retrieval. Default is high accuracy.
 *        If set to false, it uses balanced power accuracy.
 */
@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetCurrentLocationFailed: (Exception) -> Unit,
    priority: Boolean = true
    ,context:Context
) {
     val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    // Determine the accuracy priority based on the 'priority' parameter
    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
    else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    // Check if location permissions are granted
        // Retrieve the current location asynchronously
    if (fusedLocationProviderClient != null) {
        fusedLocationProviderClient.getCurrentLocation(
            accuracy, CancellationTokenSource().token,
        ).addOnSuccessListener { location ->
            location?.let {
                // If location is not null, invoke the success callback with latitude and longitude
                onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
            }
        }.addOnFailureListener { exception ->
            // If an error occurs, invoke the failure callback with the exception
            onGetCurrentLocationFailed(exception)
        }
    }

}