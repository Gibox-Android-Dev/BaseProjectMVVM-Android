/*
 * Created by Muhamad Syafii
 * Thursday, 3/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.textrecognition

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.gibox.baseprojectmvvmandroid.R
import com.gibox.baseprojectmvvmandroid.databinding.ActivityTextRecognitionBinding
import com.gibox.baseprojectmvvmandroid.util.PermissionUtils
import com.gibox.baseprojectmvvmandroid.util.camerax.CameraManager
import com.gibox.baseprojectmvvmandroid.util.closeActivity
import com.gibox.baseprojectmvvmandroid.util.constant.PERMISSION_CAMERA
import com.gibox.baseprojectmvvmandroid.util.showToast

class TextRecognitionActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTextRecognitionBinding.inflate(layoutInflater) }
    private lateinit var cameraManager: CameraManager
    private var isFlash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!PermissionUtils.isPermissionCameraGranted(this) && !PermissionUtils.isPermissionStorageGranted(
                this
            )
        ) {
            PermissionUtils.requestPermissionCamera(this)
            PermissionUtils.requestPermissionStorage(this)
        } else {
            createCameraManager()
            cameraManager.startCameraText()
            checkFlash()
        }
        initView()
    }

    private fun initView() {

        binding.imBack.setOnClickListener {
            closeActivity()
        }

        binding.imFlash.setOnClickListener {
            isFlash = if (isFlash) {
                cameraManager.flashOn()
                binding.imFlash.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_flash_off_white, null))
                false
            } else {
                cameraManager.flashOff()
                binding.imFlash.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_flash_on_white, null))
                true
            }
        }

    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            this,
            binding.previewViewFinder,
            this,
            binding.graphicOverlayFinder
        )
    }

    private fun checkFlash() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                showToast("This device has no flash")
            }
        } else {
            showToast("This device has no camera")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CAMERA && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cameraManager.startCameraText()
        } else {
            showToast("Ijin menggunakan kamera dibutuhkan")
            finish()
        }

    }
}