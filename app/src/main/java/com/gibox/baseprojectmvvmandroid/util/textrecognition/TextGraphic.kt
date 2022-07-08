/*
 * Created by Muhamad Syafii
 * Thursday, 3/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util.textrecognition

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.gibox.baseprojectmvvmandroid.util.camerax.GraphicOverlay
import com.google.mlkit.vision.text.Text

class TextGraphic (
    overlay : GraphicOverlay,
    private val text : Text.TextBlock,
    private val imageRect : Rect
) : GraphicOverlay.Graphic(overlay){

    private val rectPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        alpha = 50
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 54.0f
    }


    override fun draw(canvas: Canvas) {
        text.boundingBox.let { box ->
            val rect = calculateRect(imageRect.height().toFloat(), imageRect.width().toFloat(), box!!)
            canvas.drawRoundRect(rect, 8F, 8F, rectPaint)
            canvas.drawText(text.text, rect.left, rect.bottom, textPaint)
        }
    }
}