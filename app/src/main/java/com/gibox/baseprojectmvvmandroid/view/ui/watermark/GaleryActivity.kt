/*
 * Created by Muhammad Riza
 * Thursday, 3/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.watermark

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.gibox.baseprojectmvvmandroid.R
import com.gibox.baseprojectmvvmandroid.databinding.ActivityGaleryBinding
import com.gibox.baseprojectmvvmandroid.util.AddWatermarkUtils
import com.gibox.baseprojectmvvmandroid.util.StorageUtils
import com.gibox.baseprojectmvvmandroid.util.openActivity
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.adapter.MediaAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import java.net.URI
import android.provider.DocumentsContract

import android.content.ContentUris
import android.content.Context
import android.util.Log
import android.widget.MediaController


class GaleryActivity : AppCompatActivity() {

    companion object{
        var REQUEST_CAMERA_IMAGE = 200
        var REQUEST_CAMERA_RECORD = 201
        var REQUEST_VIEW_PICTURE = 301
        var RESPONSE_DELETE_PICTURE = 304
    }

    val viewModel: MediaViewModel by viewModels()

    //for animation
    private var showFabImage: Animation? = null
    private var hideFabImage: Animation? = null
    private var showFabVideo: Animation? = null
    private var hideFabVideo: Animation? = null
    private var isRotate = false


    var currenPhotoPath:String? = ""
    var currentVideoPath:String? = ""

    var addWatermarkUtils: AddWatermarkUtils? = null

    var imageChange = false

    val binding by lazy { ActivityGaleryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        addWatermarkUtils = AddWatermarkUtils(this)

        val sectionPagerMedia = SectionPagerMedia(this,supportFragmentManager)
        binding.fragmentContainer.adapter = sectionPagerMedia
        binding.tabs.setupWithViewPager(binding.fragmentContainer)
        setupFloatingButton()

        checkPermisionStorage()

        binding.imExit.setOnClickListener {
            setStateMultipleChoice(false)
            viewModel.isBackEvent()
        }


        viewModel.dataMedia.observe(this,{data->
            if (data!=null){
                Log.d("dataSize",data.size.toString())
            }
        })

        viewModel.getImageFromGalery()


    }

    fun checkPermision(requestCode:Int){
        if (ContextCompat.checkSelfPermission(this@GaleryActivity,Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@GaleryActivity, arrayOf(Manifest.permission.CAMERA),100)
        }else if (requestCode== REQUEST_CAMERA_IMAGE){
            val fileName = "photo"
            val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            try {
                val imageFile = File.createTempFile(fileName,".jpg",storageDirectory)

                currenPhotoPath = imageFile.absolutePath

                val imageUri = FileProvider.getUriForFile(this@GaleryActivity,
                    "com.gibox.baseprojectmvvmandroid.fileProvider", imageFile)

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
                startActivityForResult(intent,REQUEST_CAMERA_IMAGE)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }else if(requestCode== REQUEST_CAMERA_RECORD){
            val fileName = "video"
            val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_MOVIES)

            try {
                val videoFile = File.createTempFile(fileName,".mp4",storageDirectory)

                currentVideoPath = videoFile.absolutePath

                val videoUri = FileProvider.getUriForFile(this@GaleryActivity,
                    "com.gibox.baseprojectmvvmandroid.fileProvider", videoFile)

                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,videoUri)
                startActivityForResult(intent, REQUEST_CAMERA_RECORD)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("ResourceType")
    fun setupFloatingButton() {
        showFabVideo = AnimationUtils.loadAnimation(this, R.anim.fab2_show)
        hideFabVideo = AnimationUtils.loadAnimation(this, R.anim.fab2_hide)
        showFabImage = AnimationUtils.loadAnimation(this, R.anim.fab3_show)
        hideFabImage = AnimationUtils.loadAnimation(this, R.anim.fab3_hide)

        binding.run {
            //setOnclick from fab button
            fabAdd.setOnClickListener {
                isRotate = rotateFab(it, !isRotate)
                if (isRotate) {
                    //Display FAB menu
                    isRotate = true
                    expandFAB()
                } else {
                    //Close FAB menu
                    isRotate = false
                    hideFab()

                }
            }


            fabImage.setOnClickListener {
                //openActivity(WatermarkActivity::class.java)
                checkPermision(REQUEST_CAMERA_IMAGE)
                hideFab()
                rotateFab(binding.fabAdd,false)
                isRotate = false
            }

            fabVideo.setOnClickListener {
                checkPermision(REQUEST_CAMERA_RECORD)
                hideFab()
                rotateFab(binding.fabAdd,false)
                isRotate = false
            }

        }
    }


    fun hideFab() {
        binding.fabImage.startAnimation(hideFabImage)
        binding.fabImage.isClickable = false
        binding.fabVideo.startAnimation(hideFabVideo)
        binding.fabVideo.isClickable = false

    }

    fun expandFAB() {
        binding.fabImage.startAnimation(showFabImage)
        binding.fabImage.isClickable = true
        binding.fabVideo.startAnimation(showFabVideo)
        binding.fabVideo.isClickable = true
    }

    fun showLoading() {
        //TODO : Still Development
    }

    fun hideLoading() {
        //TODO : Still Development
    }

    private fun rotateFab(it: View, b: Boolean): Boolean {
        it.animate().setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                }
            })
            .rotation(if (b) 135f else 0f)
        return b
    }

    fun checkPermisionStorage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),100)
        }else{
            //viewModel.getImageFromGalery()
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CAMERA_IMAGE && resultCode== RESULT_OK){
            var bitmap = BitmapFactory.decodeFile(currenPhotoPath)
            bitmap =rotateImage(bitmap!!, 90.0F)
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat("EEE, dd-MMMM-yyyy") //or use getDateInstance()
            val formatedDate = formatter.format(date)
            binding.txtText.text = formatedDate.toString()

            var watermarkImage = addWatermarkUtils?.setWatermark(resources,bitmap,binding.txtText,0)

            watermarkImage = addWatermarkUtils?.setWatermark(resources,watermarkImage!!,binding.txtTextMaps,1)
            val format= Bitmap.CompressFormat.JPEG
            StorageUtils.saveBitmap(this,watermarkImage!!,format,"image/$formatedDate")
            imageChange = true
            viewModel.getImageFromGalery()
        }
        else if (requestCode== REQUEST_CAMERA_RECORD && resultCode == RESULT_OK){
            StorageUtils.saveVideoNewAgain(this,currentVideoPath!!,"video/videoss")
            //viewModel.getImageFromGalery()
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    override fun onBackPressed() {
        if (!binding.fabAdd.isVisible){
            setStateMultipleChoice(false)
            viewModel.isBackEvent()
            //Toast.makeText(this, "tidak ada", Toast.LENGTH_SHORT).show()
        }else{
            super.onBackPressed()
        }
    }

    fun setStateMultipleChoice(status:Boolean){
        if (status){
            hideFab()
            binding.fabAdd.visibility = View.INVISIBLE
            binding.relativeLayout.visibility = View.INVISIBLE
            binding.lytChoiceImage.visibility = View.VISIBLE
            binding.tabs.getTabAt(1)?.view?.isClickable = false
            binding.tabs.getTabAt(2)?.view?.isClickable = false
            binding.tabs.getTabAt(0)?.view?.isClickable = false

        }else{
            binding.fabAdd.visibility = View.VISIBLE
            binding.relativeLayout.visibility = View.VISIBLE
            binding.lytChoiceImage.visibility = View.INVISIBLE
            binding.tabs.getTabAt(1)?.view?.isClickable = true
            binding.tabs.getTabAt(2)?.view?.isClickable = true
            binding.tabs.getTabAt(0)?.view?.isClickable = true
        }
    }

    fun setSumChoiceFile(size:Int){
        binding.txtSum.text = size.toString()
    }

}