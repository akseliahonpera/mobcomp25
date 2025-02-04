package com.example.mobcomp25.ui.screens

//https://www.youtube.com/watch?v=GRHQcl496P4 mallia otettu täältä

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.Directory
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat

import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private fun pickPhoto(
    filenameFormat:String,
    imageCapture:ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri)->Unit,
    onError:(ImageCaptureException)->Unit
    ){

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat,Locale("Finnish")).format(System.currentTimeMillis())+".jpg"
    )
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions,executor,object:ImageCapture.OnImageSavedCallback{
        override fun onError(exception: ImageCaptureException?) {
            Log.i("orava"
            ,"Kamerhommasa joku kusee")
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults?) {
            val savedUri = Uri.fromFile(photoFile)
            onImageCaptured(savedUri)
        }
    })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also{
        cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        },ContextCompat.getMainExecutor(this))
    }
}