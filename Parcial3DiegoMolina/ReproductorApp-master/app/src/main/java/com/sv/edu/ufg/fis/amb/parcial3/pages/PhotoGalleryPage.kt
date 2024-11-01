package com.sv.edu.ufg.fis.amb.parcial3.pages

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sv.edu.ufg.fis.amb.parcial3.utils.FileUtils
import java.io.File

@Composable
fun PhotoGalleryPage(
    context: Context
) {
    val photoFiles = remember { mutableStateListOf<File>() }

    // Cargar las fotos desde el directorio de almacenamiento
    LaunchedEffect(Unit) {
        photoFiles.addAll(FileUtils.getAllImageFiles(context))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(0.dp))  // Reducir margen superior al mínimo
        }

        items(photoFiles) { file ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                // Mostrar la imagen
                Image(
                    painter = rememberAsyncImagePainter(Uri.fromFile(file)),
                    contentDescription = "Foto tomada",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
}
