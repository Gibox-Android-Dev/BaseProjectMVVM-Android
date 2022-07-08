/*
 * Created by Muhamad Syafii
 * , 26/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.map

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.gibox.baseprojectmvvmandroid.R
import com.gibox.baseprojectmvvmandroid.databinding.ActivityMapsBinding
import com.gibox.baseprojectmvvmandroid.util.closeActivity
import com.gibox.baseprojectmvvmandroid.util.constant.SET_ZOOM
import com.gibox.baseprojectmvvmandroid.util.map.LocationUtils
import com.gibox.baseprojectmvvmandroid.util.map.MapsController
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.osmdroid.util.GeoPoint

class MapsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMapsBinding
    private var mapUtils:MapsController? = null
    var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout?>? = null
    private var geoPoint : GeoPoint? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomSheetDetailMarker()
        initOSMMap()
        initView()
    }

    private fun initView() {
        binding.imBack.setOnClickListener {
            closeActivity()
        }
    }

    private fun initOSMMap() {
        LocationUtils().getCurrentLocation(this, object : OnSuccessListener<Location?>{
            override fun onSuccess(p0: Location?) {
                if (p0 == null){
                    return
                }else{
                    val geoLocation = GeoPoint(p0.latitude, p0.longitude)
                    geoPoint = GeoPoint(p0.latitude, p0.longitude)
                    binding.mapOsm.controller.setZoom(SET_ZOOM)
                    binding.mapOsm.controller.setCenter(geoLocation)
                    mapUtils?.setMapsDefault(geoLocation)
                }
            }
        })


        binding.fabLocation.setOnClickListener {
            mapUtils?.switchMapSatellite()
        }

        binding.fabToLocation.setOnClickListener {
           binding.mapOsm.controller.animateTo(geoPoint)
        }

    }

    override fun onResume() {
        super.onResume()
        mapUtils?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapUtils?.onPause()
    }

    private fun setBottomSheetDetailMarker() {
        val bottomSheetDetailMarker: ConstraintLayout = findViewById(R.id.bottomsheet_detail_marker)
        val bottomSheet = BottomSheetBehavior.from(bottomSheetDetailMarker)


        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bg.visibility = View.INVISIBLE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.bg.visibility = View.VISIBLE
                binding.bg.alpha = slideOffset
            }
        })

        bottomSheetBehavior= bottomSheet
        mapUtils = MapsController(this, binding.mapOsm,bottomSheetBehavior)
    }


    override fun onBackPressed() {
        if (bottomSheetBehavior?.state==BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }else{
            super.onBackPressed()
        }
    }

}