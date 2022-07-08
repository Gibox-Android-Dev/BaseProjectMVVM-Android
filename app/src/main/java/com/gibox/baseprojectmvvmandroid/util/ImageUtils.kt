/*
 * Created by Muhamad Syafii
 * Wednesday, 2/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import timber.log.Timber
import java.io.IOException

object ImageUtils {
    //this is save image to gallery
    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveBitmap(
        context: Context,
        bitmap: Bitmap,
        format: Bitmap.CompressFormat,
        displayName: String
    ): Uri? {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/3MGallery")
        val resolver: ContentResolver = context.contentResolver
        val contentUriList: List<Uri> = getContentUris(context)
        for (contentUri in contentUriList) {
            var uri: Uri? = null
            try {
                uri = resolver.insert(contentUri, values)
                if (uri == null) throw IOException("Failed to create new MediaStore record.")
                resolver.openOutputStream(uri).use { stream ->
                    if (stream == null) throw IOException("Failed to open output stream.")
                    if (!bitmap.compress(
                            format,
                            90,
                            stream
                        )
                    ) throw IOException("Failed to save bitmap.")
                }
                return uri
            } catch (e: IOException) {
                Timber.e("Failed to save in volume: $contentUri")
                if (uri != null) {
                    // Don't leave an orphan entry in the MediaStore
                    resolver.delete(uri, null, null)
                }
                // Do not throw, and try the next volume
            }
        }
        return null
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun getContentUris(context: Context): List<Uri> {
        val allVolumes: MutableList<String> = ArrayList()
        allVolumes.add(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        allVolumes.add(MediaStore.VOLUME_EXTERNAL)

        val externalVolumeNames = MediaStore.getExternalVolumeNames(context)
        for (entry in externalVolumeNames) {

            if (!allVolumes.contains(entry)) allVolumes.add(0, entry)
        }

        val output: MutableList<Uri> = ArrayList()
        for (entry in allVolumes) {
            output.add(MediaStore.Images.Media.getContentUri(entry))
        }
        return output
    }
}