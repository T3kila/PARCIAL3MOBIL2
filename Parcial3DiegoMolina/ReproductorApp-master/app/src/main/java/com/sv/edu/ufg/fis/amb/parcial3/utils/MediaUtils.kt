package com.sv.edu.ufg.fis.amb.parcial3.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

object MediaUtils {
    fun getLastVideoFromGallery(context: Context): Uri? {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATE_TAKEN
        )

        val sortOrder = "${MediaStore.Video.Media.DATE_TAKEN} DESC"

        val query = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        query?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val id = cursor.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id.toString())
                return contentUri
            }
        }
        return null
    }
}