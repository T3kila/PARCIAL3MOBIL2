package com.sv.edu.ufg.fis.amb.parcial3.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainPage(
    onRecordVideo: () -> Unit,
    onViewVideos: () -> Unit,
    onPlayLastVideo: () -> Unit,
    onOpenCamera: () -> Unit, // Nuevo parámetro para abrir la cámara de fotos
    onViewPhotos: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado
        Text(
            text = "Reproductor de Videos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Contenedor para los botones
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón para Grabar Video
                Button(
                    onClick = onRecordVideo,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Grabar Video", color = Color.White, fontSize = 16.sp)
                }

                // Botón para Ver Videos Guardados
                Button(
                    onClick = onViewVideos,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Ver Videos Guardados", color = Color.White, fontSize = 16.sp)
                }

                // Botón para Reproducir Último Video
                Button(
                    onClick = onPlayLastVideo,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Reproducir Último Video", color = Color.White, fontSize = 16.sp)
                }

                // Botón para Abrir Cámara para Fotos
                Button(
                    onClick = onOpenCamera,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Abrir Cámara para Foto", color = Color.White, fontSize = 16.sp)
                }

                Button(
                    onClick = onViewPhotos,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Ver Fotos Tomadas", color = Color.White, fontSize = 16.sp)
                }            }
        }
    }
}
