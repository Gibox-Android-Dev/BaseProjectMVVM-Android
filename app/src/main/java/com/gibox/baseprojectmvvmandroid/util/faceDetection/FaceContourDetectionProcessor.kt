/*
 * Created by Muhamad Syafii
 * Wednesday, 2/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util.faceDetection

import android.graphics.Rect
import com.gibox.baseprojectmvvmandroid.util.camerax.BaseImageAnalyzer
import com.gibox.baseprojectmvvmandroid.util.camerax.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import okio.IOException
import timber.log.Timber

class FaceContourDetectionProcessor (private val view: GraphicOverlay) : BaseImageAnalyzer<List<Face>>() {

    private val TAG = FaceContourDetectionProcessor::class.java.simpleName


    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .build()

    private val detector = FaceDetection.getClient(realTimeOpts)

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun detectInImage(image: InputImage): Task<List<Face>> {
       return detector.process(image)
    }

    override fun stop() {
      try {
          detector.close()
      }catch (e : IOException){
          Timber.tag(TAG).e("Exception thrown while trying to close Face Detector: $e")
      }
    }

    override fun onSuccess(results: List<Face>, graphicOverlay: GraphicOverlay, rect: Rect) {
        graphicOverlay.clear()
        results.forEach {
            val faceGraphic = FaceContourGraphic(graphicOverlay, it , rect)
            graphicOverlay.add(faceGraphic)
        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
        Timber.tag(TAG).w("Face Detector failed.$e")
    }


}