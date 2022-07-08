/*
 * Created by Muhamad Syafii
 * , 10/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.watermark

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import com.gibox.baseprojectmvvmandroid.R
import com.gibox.baseprojectmvvmandroid.databinding.ActivityImageViewBinding
import java.io.File
import android.widget.Toast


import android.provider.MediaStore

import android.content.ContentUris

import android.net.Uri

import android.content.ContentResolver
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.gibox.baseprojectmvvmandroid.util.StorageUtils


class ActivityImageView : AppCompatActivity() {

    var bitmap:Bitmap? = null
    var fileName = ""

    companion object{
        var ARG_FILE = "arg_file"
        var REQUEST_VIEW_PICTURE = 301
        var RESPONSE_DELETE_PICTURE = 304
    }

    val binding by lazy { ActivityImageViewBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        if (intent.getStringExtra(ARG_FILE)!=null){
            fileName = intent.getStringExtra(ARG_FILE)!!
            bitmap = BitmapFactory.decodeFile(fileName)
        }
        binding.imgFull.setImageBitmap(bitmap)
        binding.imgFull.setOnClickListener {
            if (binding.lytContainerMoreFitur.root.visibility == View.VISIBLE){
                binding.lytContainerMoreFitur.root.visibility = View.GONE
            }else{
                binding.lytContainerMoreFitur.root.visibility = View.VISIBLE
            }
        }

        binding.lytContainerMoreFitur.lytDelete.setOnClickListener {
            deleteFile()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun deleteFile(){
        var file =File(fileName)
        try{
            if (StorageUtils.deleteBitmap(this,file)){
                setResult(RESPONSE_DELETE_PICTURE)
                finish()
            }
        }catch (e:Exception){
            Log.d("DeleteFile",e.message.toString())
        }
    }

}