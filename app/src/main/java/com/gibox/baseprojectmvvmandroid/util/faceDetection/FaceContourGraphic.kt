/*
 * Created by Muhamad Syafii
 * Wednesday, 2/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util.faceDetection

import android.graphics.*
import androidx.annotation.ColorInt
import com.gibox.baseprojectmvvmandroid.util.camerax.GraphicOverlay
import com.gibox.baseprojectmvvmandroid.util.constant.BOX_STROKE_WIDTH
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour

class FaceContourGraphic(
    overlay: GraphicOverlay,
    private val face: Face,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay){

    private val facePositionPaint: Paint
    private val idPaint: Paint
    private val boxPaint: Paint

    init {
        val selectedColor = Color.WHITE

        facePositionPaint = Paint()
        facePositionPaint.color = selectedColor

        idPaint = Paint()
        idPaint.color = selectedColor
        idPaint.textSize = 30.0f

        boxPaint = Paint()
        boxPaint.color = selectedColor
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = BOX_STROKE_WIDTH
    }

    private fun Canvas.drawFace(facePosition: Int, @ColorInt selectedColor: Int) {
        val contour = face.getContour(facePosition)
        val path = Path()
        contour?.points?.forEachIndexed { index, pointF ->
            if (index == 0) {
                path.moveTo(
                    translateX(pointF.x),
                    translateY(pointF.y)
                )
            }
            path.lineTo(
                translateX(pointF.x),
                translateY(pointF.y)
            )
        }
        val paint = Paint().apply {
            color = selectedColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        }
        drawPath(path, paint)
    }

    override fun draw(canvas: Canvas) {
       val rect = calculateRect(
           imageRect.height().toFloat(),
           imageRect.width().toFloat(),
           face.boundingBox
       )
        canvas.drawRect(rect, boxPaint)

        val contours = face.allContours

        contours.forEach {
            it.points.forEach { point ->
                val px = translateX(point.x)
                val py = translateY(point.y)
                canvas.drawCircle(px, py, 4.0f, facePositionPaint)
            }
        }

        // face
        canvas.drawFace(FaceContour.FACE, Color.BLUE)

        // left eye
        canvas.drawFace(FaceContour.LEFT_EYEBROW_TOP, Color.RED)
        canvas.drawFace(FaceContour.LEFT_EYE, Color.BLACK)
        canvas.drawFace(FaceContour.LEFT_EYEBROW_BOTTOM, Color.CYAN)

        // right eye
        canvas.drawFace(FaceContour.RIGHT_EYE, Color.DKGRAY)
        canvas.drawFace(FaceContour.RIGHT_EYEBROW_BOTTOM, Color.GRAY)
        canvas.drawFace(FaceContour.RIGHT_EYEBROW_TOP, Color.GREEN)

        // nose
        canvas.drawFace(FaceContour.NOSE_BOTTOM, Color.LTGRAY)
        canvas.drawFace(FaceContour.NOSE_BRIDGE, Color.MAGENTA)

        // rip
        canvas.drawFace(FaceContour.LOWER_LIP_BOTTOM, Color.WHITE)
        canvas.drawFace(FaceContour.LOWER_LIP_TOP, Color.YELLOW)
        canvas.drawFace(FaceContour.UPPER_LIP_BOTTOM, Color.GREEN)
        canvas.drawFace(FaceContour.UPPER_LIP_TOP, Color.CYAN)
    }
}