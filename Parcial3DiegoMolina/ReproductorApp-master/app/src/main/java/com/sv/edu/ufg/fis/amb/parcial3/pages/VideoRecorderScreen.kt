package com.sv.edu.ufg.fis.amb.parcial3.pages

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Stop
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
fun VideoRecorderPage(
    onVideoRecorded: (Uri) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor: Executor = ContextCompat.getMainExecutor(context)

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    if (permissionsState.allPermissionsGranted) {
        var recording by remember { mutableStateOf<Recording?>(null) }
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        val recorder = remember {
            Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
        }
        val videoCapture = remember { VideoCapture.withOutput(recorder) }
        val previewView = remember { androidx.camera.view.PreviewView(context) }

        // Variable para almacenar el control de la cámara
        var cameraControl by remember { mutableStateOf<androidx.camera.core.CameraControl?>(null) }
        var zoomLevel by remember { mutableStateOf(1f) }

        LaunchedEffect(cameraProviderFuture) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    videoCapture
                )
                cameraControl = camera.cameraControl  // Asigna el control de la cámara
            } catch (e: Exception) {
                Log.e("VideoRecorderPage", "Error binding camera", e)
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {

            Card(
                shape = RoundedCornerShape(7.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(4 / 9f)
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(
                onClick = {
                    if (recording == null) {
                        val file = FileUtils.createVideoFile(context)
                        val outputOptions = FileOutputOptions.Builder(file).build()
                        recording = videoCapture.output.prepareRecording(context, outputOptions)
                            .withAudioEnabled()
                            .start(executor) { event ->
                                if (event is VideoRecordEvent.Finalize) {
                                    if (!event.hasError()) {
                                        onVideoRecorded(Uri.fromFile(file))
                                        Log.d("VideoRecorderPage", "Recording successful")
                                    } else {
                                        Log.e("VideoRecorderPage", "Error saving recording")
                                    }
                                    recording = null
                                }
                            }
                    } else {
                        recording?.stop()
                        recording = null
                        Log.d("VideoRecorderPage", "Recording stopped")
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 90.dp)
                    .size(72.dp)
                    .background(
                        color = if (recording == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (recording == null) Icons.Default.FiberManualRecord else Icons.Default.Stop,
                    contentDescription = "Toggle Recording",
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
                    .align(Alignment.BottomEnd)  // Coloca el botón en la parte inferior derecha
                    .padding(end = 48.dp, bottom = 100.dp)  // Ajusta el espaciado desde el borde derecho e inferior

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
                text = "Se requieren permisos para usar la cámara y el micrófono.",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
