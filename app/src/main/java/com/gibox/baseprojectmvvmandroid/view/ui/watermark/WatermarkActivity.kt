/*
 * Created by Muhamad Riza
 * Tuesday, 31/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.watermark

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.gibox.baseprojectmvvmandroid.databinding.ActivityWatermarkBinding
import com.gibox.baseprojectmvvmandroid.util.AddWatermarkUtils
import com.gibox.baseprojectmvvmandroid.util.StorageUtils
import com.gibox.baseprojectmvvmandroid.util.map.AddTextWatermarkutils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.R
import android.content.DialogInterface
import android.provider.Settings
import android.view.View

import com.google.android.material.snackbar.Snackbar




class WatermarkActivity : AppCompatActivity() {

    private val binding by lazy { ActivityWatermarkBinding.inflate(layoutInflater) }
    var addWatermarkUtils:AddWatermarkUtils? = null
    var addTextWatermarkutils = AddTextWatermarkutils()

    var currenPhotoPath:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imagePathList: ArrayList<String>? = getGalleryImagesPath()

        addWatermarkUtils = AddWatermarkUtils(this)

        //checkPermision()

        checkPermisionStorage()

        binding.btnOpenCamera.setOnClickListener {
            val fileName = "photo"
            val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            try {
                val imageFile = File.createTempFile(fileName,".png",storageDirectory)

                currenPhotoPath = imageFile.absolutePath

                val imageUri = FileProvider.getUriForFile(this@WatermarkActivity,
                    "com.gibox.baseprojectmvvmandroid.fileProvider", imageFile)

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
                startActivityForResult(intent,1)
            }catch (e:IOException){
                e.printStackTrace()
            }


        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1 && resultCode== RESULT_OK){
            var bitmap = BitmapFactory.decodeFile(currenPhotoPath)
            //val bitmap = data?.extras?.get("data") as Bitmap
            bitmap =rotateImage(bitmap!!, 90.0F)
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat("EEE, dd-MMMM-yyyy") //or use getDateInstance()
            val formatedDate = formatter.format(date)
            binding.txtText.text = formatedDate.toString()
            binding.imgImage.setImageBitmap(bitmap)

            var watermarkImage = addWatermarkUtils?.setWatermark(resources,bitmap,binding.txtText,0)
            binding.imgImage.setImageBitmap(watermarkImage)
            watermarkImage = addWatermarkUtils?.setWatermark(resources,watermarkImage!!,binding.txtTextMaps,1)
            binding.imgImage.setImageBitmap(watermarkImage)
            val format=Bitmap.CompressFormat.PNG
            StorageUtils.saveBitmap(this,watermarkImage!!,format, "image/$formatedDate")
        }
        /*if (watermarkImage!=null){
            binding.imgImage.setImageBitmap(addTextWatermarkutils.setTextWatermark(watermarkImage,"@Riza"))
        }*/
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    /*fun checkPermision(){
        if (ContextCompat.checkSelfPermission(this@WatermarkActivity,Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@WatermarkActivity, arrayOf(Manifest.permission.CAMERA),100)
        }
    }*/

    private fun getGalleryImagesPath(): ArrayList<String> {
        val imagePathList = ArrayList<String>()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // MediaStore.MediaColumns.DATA is deprecated
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                imagePathList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)))
            }
            cursor.close()
        }
        return imagePathList

    }

    private fun getImagesFromDCIM()
    {
        /*pDialog.setMessage("Fetching images from DCIM folder...");
        pDialog.show();*/

        val dcimPath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM+"/3MGallery").toString())

        if (dcimPath.exists()) {
            val files = dcimPath.listFiles();
            if (files!=null){
                Log.d("JumlahFile",files.size.toString())
            }else{
                Log.d("JumlahFile","Kosong")
            }

            Glide.with(this)
                .load(files[0])
                .into(binding.imgImage)
        }else{
            Log.d("JumlahFile","Kosong")
        }
    }

    fun checkPermisionStorage(){
        if (ContextCompat.checkSelfPermission(this@WatermarkActivity, Manifest.permission.READ_EXTERNAL_STORAGE)!=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@WatermarkActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),100)
        }else{
            getImagesFromDCIM()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode==100 && grantResults.size>0){
            if( grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getImagesFromDCIM()
            }else{
                if (application == null) {
                    return
                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    checkPermisionStorage()
                } else {
                    val snackbar = Snackbar.make(
                        binding.imgImage,
                        "Izinkan",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction(
                        "Setting",
                        object : View.OnClickListener {
                            override fun onClick(p0: View?) {
                                if (application == null) {
                                    return
                                }
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri =
                                    Uri.fromParts("package", application.getPackageName(), null)
                                intent.data = uri
                                this@WatermarkActivity.startActivity(intent)
                            }
                        })
                    snackbar.show()
                }
            }
        }

    }



}