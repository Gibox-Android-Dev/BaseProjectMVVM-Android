/*
 * Created by Muhamad Syafii
 * Wednesday, 2/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gibox.baseprojectmvvmandroid.util.constant.PERMISSION_CAMERA
import com.gibox.baseprojectmvvmandroid.util.constant.PERMISSION_STORAGE

object PermissionUtils {
    fun isPermissionCameraGranted(mContext: Context): Boolean{
        return  (ContextCompat.checkSelfPermission(mContext,
            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    fun isPermissionStorageGranted(mContext: Context): Boolean{
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            isPermissionStorageGrantedBelowQ(mContext)
        else isPermissionStorageGrantedQ(mContext)
    }

    private fun isPermissionStorageGrantedBelowQ(mContext: Context): Boolean{
        return (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun isPermissionStorageGrantedQ(mContext: Context) =
        (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

    fun isSDKNougat(): Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    fun requestPermissionStorage(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) else
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ), PERMISSION_STORAGE
        )
    }

    fun requestPermissionStorage(fragment: Fragment){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            fragment.requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), PERMISSION_STORAGE
            )
        } else {
            fragment.requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ), PERMISSION_STORAGE
            )
        }
    }

    fun requestPermissionStorageAndCamera(activity: Activity){
        ActivityCompat.requestPermissions(
            activity,
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ) else
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), PERMISSION_STORAGE
        )
    }

    fun requestPermissionCamera(activity: Activity){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.CAMERA
            ), PERMISSION_CAMERA
        )
    }
}