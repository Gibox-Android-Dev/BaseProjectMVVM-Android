/*
 * Created by Muhamad Syafii
 * Wednesday, 2/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import java.lang.reflect.Field
import java.util.*

object DeviceInfo {
    fun getDeviceOs(): String {
        val fields: Array<Field> = Build.VERSION_CODES::class.java.fields
        var mDeviceOsName = ""

        run loop@{
            fields.forEach {
                if (it.getInt(Any()) == Build.VERSION.SDK_INT) {
                    mDeviceOsName = it.name
                    return@loop
                }
            }
        }

        return mDeviceOsName
    }

    fun getDeviceName(context: Context): String {
        return (Build.MODEL ?: "Unknown") + " or " + Settings.Secure.getString(
            context.contentResolver,
            "bluetooth_name"
        )
    }

    fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }

    fun versionApp(context: Context): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Version Unknown"
        }
    }
}