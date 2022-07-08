/*
 * Created by Muhamad Syafii
 * Thursday, 20/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util.map

import android.content.Context
import android.location.Geocoder
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gibox.baseprojectmvvmandroid.R
import com.gibox.baseprojectmvvmandroid.util.ItemClickListener
import com.google.gson.Gson
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow
import java.util.*

class MapDialog(
    private val context : Context,
    private val mapOsm : MapView,
    private val location : GeoPoint?,
    private val listener : ItemClickListener<GeoPoint>
) : InfoWindow(R.layout.item_info_window, mapOsm){
    private val content = mView.findViewById<ConstraintLayout>(R.id.cl_root)
    private val tvName : TextView = mView.findViewById(R.id.tv_name)
    private val tvLatLong : TextView = mView.findViewById(R.id.tv_lat_long)
    private val tvDetail : TextView = mView.findViewById(R.id.tv_detail)


    override fun onOpen(item: Any?) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address = geocoder.getFromLocation(location!!.latitude, location.longitude, 1)

        tvName.text = address[0].getAddressLine(0)
        tvLatLong.text = "${location!!.latitude} , ${location.longitude}"

        content.setOnClickListener {
            close()
        }

        tvDetail.setOnClickListener {
            listener.onClick(location)
            close()
        }
    }

    override fun onClose() {}
}