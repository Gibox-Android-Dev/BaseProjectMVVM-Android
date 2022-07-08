/*
 * Created by Muhamad Syafii
 * Thursday, 3/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util.textrecognition

import android.graphics.Rect
import android.util.Log
import com.gibox.baseprojectmvvmandroid.util.camerax.BaseImageAnalyzer
import com.gibox.baseprojectmvvmandroid.util.camerax.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import okio.IOException
import timber.log.Timber


class TextDetectionProcessor (private val view: GraphicOverlay) : BaseImageAnalyzer<Text>(){

    private val TAG = TextDetectionProcessor::class.java.simpleName

    private val realTimeOpts = TextRecognizerOptions.Builder()
        .build()

    private val recognizer by lazy { TextRecognition.getClient(realTimeOpts)}
    override val graphicOverlay: GraphicOverlay
        get() = view


    override fun stop() {
       try {
           recognizer.close()
       }catch (e : IOException){
           Timber.tag(TAG).e("Exception thrown while trying to close Face Detector : $e")
       }
    }

    override fun onFailure(e: Exception) {
        Timber.tag(TAG).e(TAG, "onFailure: $e")
    }

    override fun detectInImage(image: InputImage): Task<Text> {
       return recognizer.process(image)
    }

    override fun onSuccess(results: Text, graphicOverlay: GraphicOverlay, rect: Rect) {
       graphicOverlay.clear()
        results.textBlocks.forEach {
            val textGraphic = TextGraphic(graphicOverlay, it, rect)
            graphicOverlay.add(textGraphic)
        }

        Timber.tag(TAG).e(TAG, "onSuccess: ${results.text}")
        graphicOverlay.postInvalidate()
    }


}