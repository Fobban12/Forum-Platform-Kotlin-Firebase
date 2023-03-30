package com.example.kotlin_application.viewmodel

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_application.repository.CustomCameraRepo
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject



@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repo: CustomCameraRepo
):ViewModel() {

    fun showCameraPreview(
        previewView: Preview,
        lifecycleOwner: LifecycleOwner
    ){
//        viewModelScope.launch {
//            previewView.setSurfaceProvider(previewView.)
//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    lifecycleOwner,
//                    selector,
//                    preview,
//                    imageAnalysis,
//                    imageCapture
//                )
//            }catch (e:Exception){
//                e.printStackTrace()
//            }
//        }
    }

    fun captureAndSave(context: Context, imageCapture: ImageCapture){
        viewModelScope.launch {
            val name = SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss-SSS",
                Locale.ENGLISH
            ).format(System.currentTimeMillis())


            // for storing
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME,name)
                put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg")
                if (Build.VERSION.SDK_INT > 28){
                    put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/My-Camera-App-Images")
                }
            }

            // for capture output
            val outputOptions = ImageCapture.OutputFileOptions
                .Builder(
                    context.contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                .build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Toast.makeText(
                            context,
                            "Saved image ${outputFileResults.savedUri!!}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            context,
                            "some error occurred ${exception.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
        }
    }


}