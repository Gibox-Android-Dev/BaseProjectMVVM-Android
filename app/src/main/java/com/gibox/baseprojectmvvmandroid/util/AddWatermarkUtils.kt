/*
 * Created by Muhammad Riza
 * Senin, 31/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.gibox.baseprojectmvvmandroid.R
import android.graphics.LightingColorFilter

import android.graphics.ColorFilter

import android.graphics.drawable.Drawable
import android.widget.TextView


class AddWatermarkUtils(context:Context) {
    var c: Canvas? = null
    var bmp: Bitmap? = null
    var scale: Float = 0.0f
    var r: RectF?= null
    // Create the new bitmap
    fun setWatermark(res: Resources?, source: Bitmap,view:TextView,type:Int):Bitmap?{
        val w: Int = source.width
        val h: Int = source.height
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
        // Copy the original bitmap into the new one
        c = Canvas(bmp!!)
        c?.drawBitmap(source, 0f, 0f, paint)
        // Load the watermark
        /*val myIcon: Drawable = res?.getDrawable(R.drawable.giboxdigital)!!
        val filter: ColorFilter = LightingColorFilter(Color.BLACK, Color.BLACK)
        myIcon.colorFilter = filter*/

        val bm = Bitmap.createBitmap(view.width, view.height,Bitmap.Config.ARGB_8888)
        val cn = Canvas(bm)
        view.draw(cn)
        val watermark: Bitmap = bm
        // Scale the watermark to be approximately 40% of the source image height
        val matrix: Matrix = Matrix()
        val margin = (h.toFloat()*0.01).toFloat()
        if (type==0){
            scale = (h.toFloat() * 0.20 / watermark.height.toFloat()).toFloat()
            // Create the matrix
            matrix.postScale(scale, scale)
            // Determine the post-scaled size of the watermark
            r = RectF(0f,  watermark.height.toFloat(), watermark.width.toFloat(), watermark.height.toFloat())
            matrix.mapRect(r)
            // Move the watermark to the bottom right corner
            //matrix.postTranslate(w - r!!.width(), r!!.height())
            matrix.postTranslate(w-r!!.width()-margin, r!!.height()+margin)
        }else if(type==1){
            //scale = (h.toFloat() * 0.20 / watermark.height.toFloat()).toFloat()
            // Create the matrix
            //matrix.postScale(scale, scale)
            //matrix.postScale(watermark.width.toFloat(),watermark.height.toFloat())
            // Determine the post-scaled size of the watermark
            r = RectF(0f,  0f, watermark.width.toFloat(), watermark.height.toFloat())
            matrix.mapRect(r)
            // Move the watermark to the bottom right corner
            //matrix.postTranslate(w - r!!.width(), r!!.height())

            matrix.postTranslate(w-r!!.width()-margin, h-r!!.height()-margin)
        }
        // Draw the watermark
        c?.drawBitmap(watermark, matrix, paint)
        // Free up the bitmap memory
        watermark.recycle()
        return bmp
    }
}