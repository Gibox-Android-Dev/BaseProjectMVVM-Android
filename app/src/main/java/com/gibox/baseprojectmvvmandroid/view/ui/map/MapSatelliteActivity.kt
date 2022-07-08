/*
 * Created by Muhamad Syafii
 * Tuesday, 25/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.map

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.gibox.baseprojectmvvmandroid.R
import com.gibox.baseprojectmvvmandroid.databinding.ActivityMapSatelliteBinding
import com.gibox.baseprojectmvvmandroid.databinding.BottomsheetDetailMarkerBinding
import com.gibox.baseprojectmvvmandroid.util.ItemClickListener
import com.gibox.baseprojectmvvmandroid.util.closeActivity
import com.gibox.baseprojectmvvmandroid.util.constant.SET_ZOOM
import com.gibox.baseprojectmvvmandroid.util.map.LocationUtils
import com.gibox.baseprojectmvvmandroid.util.map.MapsController
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.osmdroid.util.GeoPoint
import java.util.*

class MapSatelliteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMapSatelliteBinding.inflate(layoutInflater) }
    private lateinit var bottomSheetDetail : BottomSheetDialog
    private val mapController by lazy { MapsController(this, binding.mapOsm, null) }
    private var geoPoint : GeoPoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initOsmMaps()
        initView()

    }

    private fun initView() {
        binding.imBack.setOnClickListener {
            closeActivity()
        }
    }


    private fun initOsmMaps() {
        binding.run {
            LocationUtils().getCurrentLocation(this@MapSatelliteActivity, object : OnSuccessListener<Location?>{
                override fun onSuccess(location : Location?) {
                    if (location == null){
                        return
                    }else{
                        val geoLocation = GeoPoint(location.latitude, location.longitude)
                        geoPoint = GeoPoint(location.latitude, location.longitude)
                        mapOsm.controller.setZoom(SET_ZOOM)
                        mapOsm.controller.setCenter(geoLocation)
                        mapController.setMapsSatellite(geoLocation)
                    }
                }

            })

            fabToLocation.setOnClickListener {
                binding.mapOsm.controller.animateTo(geoPoint)
            }
            mapController.setClickDetailMarker(object : ItemClickListener<GeoPoint>{
                override fun onClick(data: GeoPoint) {
                   setupBottomDialogDetailMarker(data)
                }

            })
        }
    }

    override fun onResume() {
        super.onResume()
        mapController.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapController.onPause()
    }

    fun setupBottomDialogDetailMarker(geoPoint: GeoPoint){
        val inflater = LayoutInflater.from(this)
        val bindingBot = BottomsheetDetailMarkerBinding.inflate(inflater)
        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)

        bindingBot.txtAgentCoordinate.text = "${geoPoint.latitude}, ${geoPoint.longitude}"
        bindingBot.txtAgentLocation.text = address[0].getAddressLine(0)


        bottomSheetDetail = BottomSheetDialog(this, R.style.CustomBottomSheetDialog)
        bottomSheetDetail.setContentView(bindingBot.root)
        bottomSheetDetail.show()
    }
}