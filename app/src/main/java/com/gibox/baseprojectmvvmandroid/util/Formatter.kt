/*
 * Created by Muhamad Syafii
 * Wednesday, 2/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util

import android.graphics.Bitmap
import java.text.NumberFormat
import java.util.*

object Formatter {
    fun formatRupiah(rupiah : String) : String {
        val localeID = Locale("in", "ID")
        val rupiahFormat = NumberFormat.getCurrencyInstance(localeID)
        rupiahFormat.maximumFractionDigits = 0
        return rupiahFormat.format(rupiah.toDouble())
    }

    fun resizeBitmap(mBitmap : Bitmap, maxLength: Int) : Bitmap{
        try {
            if (mBitmap.height >= mBitmap.width){
                if (mBitmap.height <= maxLength){
                    return mBitmap
                }

                val aspectRatio = mBitmap.width.toDouble() / mBitmap.height.toDouble()
                val targetWidth = (maxLength * aspectRatio).toInt()
                return Bitmap.createScaledBitmap(mBitmap, targetWidth, maxLength, false)
            }else{
                if (mBitmap.width <= maxLength){
                    return mBitmap
                }

                val aspectRatio = mBitmap.width.toDouble() / mBitmap.height.toDouble()
                val targetHeight = (maxLength * aspectRatio).toInt()

                return Bitmap.createScaledBitmap(mBitmap, maxLength, targetHeight, false)
            }

        }catch (e : Exception){
            return mBitmap
        }
    }
}