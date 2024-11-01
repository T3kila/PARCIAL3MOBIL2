package com.sv.edu.ufg.fis.amb.parcial3.pages

import android.Manifest
import android.net.Uri
import android.util.Log
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sv.edu.ufg.fis.amb.parcial3.utils.FileUtils
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhotoCapturePage(
    onPhotoCaptured: (Uri) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor: Executor = ContextCompat.getMainExecutor(context)

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA
        )
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    if (permissionsState.allPermissionsGranted) {
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        val imageCapture = remember { ImageCapture.Builder().build() }
        val previewView = remember { androidx.camera.view.PreviewView(context) }

        // Controlador de la cámara para manejar el zoom
        var cameraControl by remember { mutableStateOf<androidx.camera.core.CameraControl?>(null) }
        var zoomLevel by remember { mutableStateOf(1f) }

        LaunchedEffect(cameraProviderFuture) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                cameraControl = camera.cameraControl  // Asigna el control de la cámara
            } catch (e: Exception) {
                Log.e("PhotoCapturePage", "Error binding camera", e)
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {

            Card(
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(5 / 9f)
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Botón de captura de foto
            IconButton(
                onClick = {
                    // Ajusta la rotación de la captura de imagen
                    val displayRotation = previewView.display?.rotation ?: Surface.ROTATION_0
                    imageCapture.targetRotation = displayRotation

                    val photoFile = FileUtils.createImageFile(context)
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            onPhotoCaptured(Uri.fromFile(photoFile))
                            Log.d("PhotoCapturePage", "Photo captured successfully")
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("PhotoCapturePage", "Error capturing photo", exception)
                        }
                    })
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 90.dp)
                    .size(72.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Capture Photo",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            // Botón flotante para hacer zoom
            FloatingActionButton(
                onClick = {
                    zoomLevel = if (zoomLevel < 2f) zoomLevel + 0.5f else 1f  // Incrementa el zoom hasta 2x, luego reinicia a 1x
                    cameraControl?.setLinearZoom(zoomLevel / 2f)  // Ajusta el nivel de zoom
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 100.dp)
            ) {
                Text(
                    text = if (zoomLevel < 2f) "+" else "-",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Se requieren permisos para usar la cámara.",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
