package com.sv.edu.ufg.fis.amb.parcial3.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {



    fun createVideoFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "VID_$timeStamp.mp4"
        val storageDir = context.getExternalFilesDir(null)
        return File(storageDir, fileName)
    }

    fun getVideoFiles(context: Context): List<File> {
        val directory = context.getExternalFilesDir(null)
        return directory?.listFiles { _, name -> name.endsWith(".mp4") }?.toList() ?: emptyList()
    }

    fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "com.sv.edu.ufg.fis.amb.reproductorapp.fileprovider",
            file
        )
    }
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = context.getExternalFilesDir("Pictures")
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    fun getAllImageFiles(context: Context): List<File> {
        val storageDir: File? = context.getExternalFilesDir("Pictures")
        return storageDir?.listFiles()?.filter { it.isFile && it.extension == "jpg" } ?: emptyList()
    }

}