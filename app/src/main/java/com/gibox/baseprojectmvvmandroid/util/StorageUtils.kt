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
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.util.Log
import androidx.loader.content.CursorLoader
import java.lang.Exception
import java.lang.System.`in`
import android.os.ParcelFileDescriptor
import java.io.*
import android.content.ContentUris

object StorageUtils {
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
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/BaseGallery")
        val resolver: ContentResolver = context.getContentResolver()
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
                            100,
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
    fun deleteBitmap(
        context: Context,
        file: File
    ):Boolean {
        try {
            // Set up the projection (we only need the ID)
            // Set up the projection (we only need the ID)
            val projection = arrayOf(MediaStore.Images.Media._ID)

            // Match on the file path

            // Match on the file path
            val selection = MediaStore.Images.Media.DATA + " = ?"
            val selectionArgs = arrayOf<String>(file.getAbsolutePath())

            // Query for the ID of the media matching the file path

            // Query for the ID of the media matching the file path
            val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val contentResolver: ContentResolver = context.getContentResolver()
            val c = contentResolver.query(queryUri, projection, selection, selectionArgs, null)
            if (c!!.moveToFirst()) {
                // We found the ID. Deleting the item via the content provider will also remove the file
                val id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val deleteUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                contentResolver.delete(deleteUri, null, null)
            } else {
                // File not found in media store DB
            }
            c.close()
            return true
        }catch (e:Exception){
            Log.d("ErrorDeleteImage",e.message.toString())
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveVideo(
        context: Context,
        videoFile: File,
        displayName: String
    ): Uri?{
        try {
            val values = ContentValues()
            values.put(MediaStore.Video.Media.TITLE, displayName)
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            values.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/BaseGallery")
            values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Video.Media.IS_PENDING, 1)
            values.put(MediaStore.Video.Media.DATA,videoFile.absolutePath)
            val resolver: ContentResolver = context.getContentResolver()
            val contentUriList: List<Uri> = getContentUrisVideo(context)
            var uri: Uri? = null
            /*try {
                uri = resolver.insert(collection, values)
                if (uri == null) throw IOException("Failed to create new MediaStore record.")
                resolver.openOutputStream(uri).use { stream ->
                    if (stream == null) throw IOException("Failed to open output stream.")
                }
                return uri
            } catch (e: IOException) {
                Timber.e("Failed to save in volume: $collection")
                if (uri != null) {
                    // Don't leave an orphan entry in the MediaStore
                    resolver.delete(uri, null, null)
                }
                // Do not throw, and try the next volume
            }*/
            for (contentUri in contentUriList) {
                var uri: Uri? = null
                Log.d("ErrorSaveUtilslongData",contentUriList.size.toString())
                try {
                    uri = resolver.insert(contentUri, values)
                    if (uri == null) throw IOException("Failed to create new MediaStore record.")
                    resolver.openOutputStream(uri).use { stream->
                        if (stream == null) throw IOException("Failed to open output stream.")
                        // Transfer bytes from in to out
                        // Transfer bytes from in to out
                        val buf = ByteArray(1024)
                        var len: Int
                        while (`in`.read(buf).also { len = it } > 0) {
                            stream.write(buf, 0, len)
                        }
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
        }catch (e:Exception){
            Log.d("ErrorSaveUtils",e.message.toString())
        }

        return null
    }



    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveVideoNewAgain(
        context: Context,
        videoFile: String,
        displayName: String
    ){
        try {
            val values = ContentValues()
            values.put(MediaStore.Video.Media.TITLE, displayName)
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            values.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/BaseGallery")
            values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Video.Media.IS_PENDING, 1)
            values.put(MediaStore.Video.Media.DATA,videoFile)
            val resolver: ContentResolver = context.getContentResolver()
            val collection =
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val uriSavedVideo = resolver.insert(collection, values)
            val pfd: ParcelFileDescriptor

            try {
                pfd = context.getContentResolver().openFileDescriptor(uriSavedVideo!!, "rw")!!
                val out = FileOutputStream(pfd.fileDescriptor)
                val imageFile = File(videoFile)
                if (!imageFile.exists()){
                    imageFile.mkdirs()
                }
                val `in` = FileInputStream(imageFile)
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
                out.close()
                `in`.close()
                pfd.close()
            } catch (e: Exception) {
                Log.d("ErrorSaveUtils2",e.message.toString())
                e.printStackTrace()
            }

            values.clear()
            values.put(MediaStore.Video.Media.IS_PENDING, 0)
            context.getContentResolver().update(uriSavedVideo!!, values, null, null)
        }catch (e:Exception){
            Log.d("ErrorSaveUtils",e.message.toString())
            e.printStackTrace()
        }
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

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getContentUrisVideo(context: Context): List<Uri> {
        val allVolumes: MutableList<String> = ArrayList()
        allVolumes.add(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        allVolumes.add(MediaStore.VOLUME_EXTERNAL)

        val externalVolumeNames = MediaStore.getExternalVolumeNames(context)
        for (entry in externalVolumeNames) {
            if (!allVolumes.contains(entry)) allVolumes.add(0, entry)
        }

        val output: MutableList<Uri> = ArrayList()
        for (entry in allVolumes) {
            output.add(MediaStore.Video.Media.getContentUri(entry))
        }
        return output
    }

}