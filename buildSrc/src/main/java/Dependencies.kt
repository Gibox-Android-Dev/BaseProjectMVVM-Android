/*
 * Created by Muhamad Syafii
 * Thursday, 13/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

object Versions {

    //kotlin libs
    const val kotlin = "1.6.0"

    //libs
    const val timber = "4.7.1"
    const val gson = "2.8.7"
    const val retrofit = "2.9.0"
    const val okHttp = "4.9.1"
    const val koin = "2.1.6"
    const val glide = "4.12.0"
    const val coroutine = "1.3.9"
    const val viewModel = "2.4.0"
    const val activityKtx = "1.4.0"
    const val coroutines_version = "1.3.9"

    //osm maps
    const val osm = "6.1.10"
    const val osmBonusPack = "6.7.0"

    //paging
    const val paging_version = "3.0.0-alpha03"

    //google & firebase
    const val gmsTask = "18.0.1"
    const val gmsLocation = "19.0.1"
    const val firebaseBOM = "29.0.3"

    //preference
    const val pref = "1.1.1"

    //shimmer loader
    const val shimmerVersion = "0.5.0"

    //navigation
    const val androidxNavigationFragmentKtx = "2.3.5"

    //sdp
    const val sdpVersion = "1.0.6"

    //camera and mlkit
    const val face = "16.1.4"
    const val camera2 = "1.0.0-beta07"
    const val cameraCore = "1.0.0-beta07"
    const val cameraLifecycle = "1.0.0-beta07"
    const val cameraView = "1.0.0-alpha14"
    const val textRecognition = "18.0.0"
    const val objectDetection = "17.0.0"

    //rootBeer
    const val rootBeer = "0.1.0"

    //VideoView
    const val youtubePlayer = "11.0.1"
    const val exoPlayer = "2.16.1"

    //imageView
    const val photoView = "2.0.0"

}

object Libs {

    //Timber log
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    //network
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val okHttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"

    //koin DI
    const val koinCore = "org.koin:koin-core:${Versions.koin}"
    const val koinAndroid = "org.koin:koin-android:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-android-viewmodel:${Versions.koin}"

    //glide
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"

    //coroutine
    const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine}"
    const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutine}"

    //view model
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewModel}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtx}"


    //osm map
    const val osmMap = "org.osmdroid:osmdroid-android:${Versions.osm}"
    const val osmBonusPack = "com.github.MKergall:osmbonuspack:${Versions.osmBonusPack}"

    //Paging
    const val paging ="androidx.paging:paging-runtime:${Versions.paging_version}"

    //google play service && firebase
    const val gms = "com.google.android.gms:play-services-tasks:${Versions.gmsTask}"
    const val googleLocation = "com.google.android.gms:play-services-location:${Versions.gmsLocation}"
    const val firebaseBOM = "com.google.firebase:firebase-bom:${Versions.firebaseBOM}"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"

    //preference
    const val preference = "androidx.preference:preference:${Versions.pref}"

    //shimmer loader
    const val shimmerLoader = "com.facebook.shimmer:shimmer:${Versions.shimmerVersion}"

    //navigation
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.androidxNavigationFragmentKtx}"
    const val navigationUiKtx =
        "androidx.navigation:navigation-ui-ktx:${Versions.androidxNavigationFragmentKtx}"

    //sdp
    const val sdp = "com.intuit.sdp:sdp-android:${Versions.sdpVersion}"

    //mlkit
    const val faceDetection = "com.google.mlkit:face-detection:${Versions.face}"
    const val textRecognition = "com.google.android.gms:play-services-mlkit-text-recognition:${Versions.textRecognition}"
    const val objectDetection = "com.google.mlkit:object-detection:${Versions.objectDetection}"

    //cameraX
    const val camera2 = "androidx.camera:camera-camera2:${Versions.camera2}"
    const val cameraCore = "androidx.camera:camera-core:${Versions.cameraCore}"
    const val cameraLifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraLifecycle}"
    const val cameraView = "androidx.camera:camera-view:${Versions.cameraView}"

    //checking root or not
    const val rootBeer = "com.scottyab:rootbeer-lib:${Versions.rootBeer}"
    //video view
    const val exoPlayer = "com.google.android.exoplayer:exoplayer:${Versions.exoPlayer}"
    const val exoPlayerUi = "com.google.android.exoplayer:exoplayer-ui:${Versions.exoPlayer}"

    //Image View
    const val photoView = "com.github.chrisbanes:PhotoView:${Versions.photoView}"
}
