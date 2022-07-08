/*
 * Created by Muhamad Syafii
 * Monday, 17/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.util.map

import android.app.Activity
import android.graphics.Color
import android.os.StrictMode
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import com.gibox.baseprojectmvvmandroid.R
import com.gibox.baseprojectmvvmandroid.util.ItemClickListener
import com.gibox.baseprojectmvvmandroid.util.constant.MAX_ZOOM
import com.gibox.baseprojectmvvmandroid.util.constant.MIN_ZOOM
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.osmdroid.config.Configuration
import org.osmdroid.events.*
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*

class MapsController(
    private val activity: Activity,
    private val mapsViewOSM: MapView,
    private val bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout?>?
) : MapEventsReceiver, MapListener {

    var geoPoint: GeoPoint? = null

    private lateinit var listener : ItemClickListener<GeoPoint>

    private var myLocation: MyLocationNewOverlay =
        MyLocationNewOverlay(GpsMyLocationProvider(activity), mapsViewOSM)
    private val mapEventsOverlay = MapEventsOverlay(this)

    fun setClickDetailMarker(onClickListener : ItemClickListener<GeoPoint>){
        this.listener = onClickListener
    }


    fun setMapsDefault(point: GeoPoint) {
        this.geoPoint = point
        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Configuration.getInstance().userAgentValue = activity.packageName
        Configuration.getInstance()
            .load(activity, PreferenceManager.getDefaultSharedPreferences(activity))
        mapsViewOSM.maxZoomLevel = MAX_ZOOM
        mapsViewOSM.minZoomLevel = MIN_ZOOM
        mapsViewOSM.setMultiTouchControls(true)
        mapsViewOSM.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        myLocation.enableMyLocation()
        myLocation.isDrawAccuracyEnabled = true
        mapsViewOSM.addMapListener(DelayedMapListener(this, 1000))
        val marker = Marker(mapsViewOSM)
        marker.position = point
        marker.infoWindow =
            MapDialog(activity, mapsViewOSM, geoPoint, object : ItemClickListener<GeoPoint> {
                override fun onClick(data: GeoPoint) {
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }
            })
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon =
            ResourcesCompat.getDrawable(activity.resources, R.drawable.ic_marker_blue, null)
        mapsViewOSM.overlays.add(0, mapEventsOverlay)
        mapsViewOSM.overlays.add(marker)
        mapsViewOSM.invalidate()
    }

    fun setMapsSatellite(point: GeoPoint) {
        this.geoPoint = point
        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Configuration.getInstance().userAgentValue = activity.packageName
        Configuration.getInstance()
            .load(activity, PreferenceManager.getDefaultSharedPreferences(activity))
        mapsViewOSM.maxZoomLevel = MAX_ZOOM
        mapsViewOSM.minZoomLevel = MIN_ZOOM
        mapsViewOSM.setMultiTouchControls(true)
        mapsViewOSM.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        myLocation.enableMyLocation()
        myLocation.isDrawAccuracyEnabled = true
        val tile = object : XYTileSource(
            "unfixed_screen",
            8,
            23,
            256,
            ".png",
            arrayOf("http://mt1.google.com/vt/lyrs=s&x={x}&y={y}&z={z}&language=id")
        ) {
            override fun getTileURLString(aTile: Long): String {
                var copyUrl = "http://mt1.google.com/vt/lyrs=s&x={x}&y={y}&z={z}&language=id"
                copyUrl = copyUrl.replace("$", "")
                copyUrl = copyUrl.replace("{z}", MapTileIndex.getZoom(aTile).toString())
                copyUrl = copyUrl.replace("{x}", MapTileIndex.getX(aTile).toString())
                copyUrl = copyUrl.replace("{y}", MapTileIndex.getY(aTile).toString())
                copyUrl = copyUrl.replace("{s}", "a")
                return copyUrl
            }
        }
        val provider = MapTileProviderBasic(activity, tile)
        val layer = TilesOverlay(provider, activity)
        layer.loadingBackgroundColor = Color.WHITE
        layer.loadingLineColor = Color.TRANSPARENT
        mapsViewOSM.overlays.add(layer)
        val marker = Marker(mapsViewOSM)
        marker.position = geoPoint
        marker.infoWindow =
            MapDialog(activity, mapsViewOSM, geoPoint, object : ItemClickListener<GeoPoint> {
                override fun onClick(data: GeoPoint) {
                    listener.onClick(data)
//                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }

            })
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon =
            ResourcesCompat.getDrawable(activity.resources, R.drawable.ic_marker_blue, null)

        mapsViewOSM.overlays.add(0, mapEventsOverlay)
        mapsViewOSM.overlays.add(marker)
        mapsViewOSM.invalidate()
    }


    //TODO Still development : for switch to Satellite
    fun switchMapSatellite() {
        mapsViewOSM.tileProvider.clearTileCache()
        val tile = object : XYTileSource(
            "unfixed_screen",
            8,
            23,
            256,
            ".png",
            arrayOf("http://mt1.google.com/vt/lyrs=s&x={x}&y={y}&z={z}&language=id")
        ) {
            override fun getTileURLString(aTile: Long): String {
                var copyUrl = "http://mt1.google.com/vt/lyrs=s&x={x}&y={y}&z={z}&language=id"
                copyUrl = copyUrl.replace("$", "")
                copyUrl = copyUrl.replace("{z}", MapTileIndex.getZoom(aTile).toString())
                copyUrl = copyUrl.replace("{x}", MapTileIndex.getX(aTile).toString())
                copyUrl = copyUrl.replace("{y}", MapTileIndex.getY(aTile).toString())
                copyUrl = copyUrl.replace("{s}", "a")
                return copyUrl
            }
        }
        val provider = MapTileProviderBasic(activity, tile)
        val layer = TilesOverlay(provider, activity)
        layer.loadingBackgroundColor = Color.WHITE
        layer.loadingLineColor = Color.TRANSPARENT
        mapsViewOSM.overlays.add(layer)
        val marker = Marker(mapsViewOSM)
        marker.position = geoPoint
        marker.infoWindow =
            MapDialog(activity, mapsViewOSM, geoPoint, object : ItemClickListener<GeoPoint> {
                override fun onClick(data: GeoPoint) {
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }

            })
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon =
            ResourcesCompat.getDrawable(activity.resources, R.drawable.ic_marker_blue, null)
        mapsViewOSM.overlays.add(0, mapEventsOverlay)
        mapsViewOSM.overlays.add(marker)
        mapsViewOSM.invalidate()
    }

    fun onResume() {
        Configuration.getInstance()
            .load(activity, PreferenceManager.getDefaultSharedPreferences(activity))
        mapsViewOSM.onResume()
    }

    fun onPause() {
        Configuration.getInstance()
            .save(activity, PreferenceManager.getDefaultSharedPreferences(activity))
        mapsViewOSM.onPause()
    }

    fun zoomIn() {
        mapsViewOSM.controller.zoomIn()
    }

    fun zoomOut() {
        mapsViewOSM.controller.zoomOut()
    }

    fun zoomToMyLocation() {
        mapsViewOSM.controller.animateTo(myLocation.myLocation)
    }


    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        return true
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        return true
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return true
    }

}