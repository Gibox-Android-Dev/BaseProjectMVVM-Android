/*
 * Created by Muhamad Syafii
 * Tuesday, 8/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util.objectdetection

import android.graphics.Rect
import com.gibox.baseprojectmvvmandroid.util.camerax.BaseImageAnalyzer
import com.gibox.baseprojectmvvmandroid.util.camerax.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import timber.log.Timber

class ObjectDetectionProcess (private val view : GraphicOverlay) :
    BaseImageAnalyzer<List<DetectedObject>>() {

    private val TAG = ObjectDetectionProcess::class.java.simpleName

    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableClassification()
        .build()

    private val detector = ObjectDetection.getClient(options)


    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun detectInImage(image: InputImage): Task<List<DetectedObject>> {
        return detector.process(image)
    }

    override fun stop() {
       try {
           detector.close()
       }catch (e : Exception){
           Timber.tag(TAG).e("$e")
       }
    }

    override fun onSuccess(
        results: List<DetectedObject>,
        graphicOverlay: GraphicOverlay,
        rect: Rect
    ) {
        graphicOverlay.clear()
        results.forEach {
            val labelGraphic = ObjectGraphic(graphicOverlay, it, rect)
            graphicOverlay.add(labelGraphic)
        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
        Timber.tag(TAG).e("$e")
    }
}