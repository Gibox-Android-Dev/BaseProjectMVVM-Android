/*
 * Created by Muhamad Syafii
 * , 3/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.watermark

import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class MediaViewModel: ViewModel() {
    private val _dataMedia = MutableLiveData<ArrayList<File?>>()
    val dataMedia = _dataMedia


    private val _backEvent = MutableLiveData<Boolean>()
    val backEvent = _backEvent

    fun getImageFromGalery(){
        val dcimPath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM+"/BaseGallery").toString())
        Log.d("pathh",dcimPath.toString())
        if (dcimPath.exists()) {
            val files = dcimPath.listFiles();
            if (files!=null){
                Log.d("JumlahFile",files.size.toString())
            }else{
                Log.d("JumlahFile","Kosong")
            }

            val data:ArrayList<File?> = ArrayList()
            data.addAll(files.reversed())
            _dataMedia.postValue(data)
        }
    }

    fun isBackEvent(){
        _backEvent.postValue(true)
    }



}