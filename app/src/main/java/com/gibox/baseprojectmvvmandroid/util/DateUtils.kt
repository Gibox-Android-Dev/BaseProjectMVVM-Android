/*
 * Created by Muhamad Syafii
 * Wednesday, 2/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun formatDateToString(time: Date?, pattern: String?): String? {
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.format(time)
    }

    fun parseToDate(date: String?, pattern: String?): Date? {
        val dateFormat = SimpleDateFormat(pattern)
        try {
            return dateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date()
    }
}