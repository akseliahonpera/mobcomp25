package com.example.mobcomp25.ui.screens

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.UriPermission
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.AlarmManagerCompat.setAlarmClock
import androidx.core.app.AlarmManagerCompat.setExact
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.mobcomp25.data.AppDatabase
import com.example.mobcomp25.data.Note
import com.example.mobcomp25.services.AlarmReceiver
import com.example.mobcomp25.services.ApplicationServices
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
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }
    var showTimePicker by remember { mutableStateOf(false) }

    var noteLocation : Pair<Double,Double>
  //  var noteLocationString: String

    noteLocation = Pair(0.0,0.0) //alustetaan nollaan
  //  noteLocationString = noteLocation.toString()

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
            Button(onClick = {  navController.navigate("simple_camera")  }) {
                Text(text = "Ota kuva")
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
            Row {


            Button(onClick = {
                launcher.launch("image/*")
            }) {
                Text(text = "Valitse kuva kirjastosta")
            }
                Button(onClick = { }) {
                    Text(text = "Lisää sijainti")
                }
            }
        }

        item{

            Button(onClick = {
                coroutineScope.launch{
                    if(noteString.isNotBlank()&&!imgUri.isNullOrEmpty()){ //lets not save empty strings
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
fun alarm(context: Context, selectedDate: Long?, selectedTime: TimePickerState?){
    if(selectedDate==null ||selectedTime==null){
        return; // no null alarms pls
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
    /*
    alarmManager.setAlarmClock(
        AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent),
        pendingIntent
    )*/
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )

}
//https://developer.android.com/develop/background-work/services/alarms/schedule