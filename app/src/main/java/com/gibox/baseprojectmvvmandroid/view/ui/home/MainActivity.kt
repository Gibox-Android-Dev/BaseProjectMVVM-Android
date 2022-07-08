package com.gibox.baseprojectmvvmandroid.view.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.request.LoginRequest
import com.gibox.baseprojectmvvmandroid.databinding.ActivityMainBinding
import com.gibox.baseprojectmvvmandroid.util.openActivity
import com.gibox.baseprojectmvvmandroid.view.ui.facedetection.FaceDetectionActivity
import com.gibox.baseprojectmvvmandroid.view.ui.listuser.ListUserActivity
import com.gibox.baseprojectmvvmandroid.view.ui.login.LoginActivity
import com.gibox.baseprojectmvvmandroid.view.ui.map.MapSatelliteActivity
import com.gibox.baseprojectmvvmandroid.view.ui.map.MapsActivity
import com.gibox.baseprojectmvvmandroid.view.ui.objectdetection.ObjectDetectionActivity
import com.gibox.baseprojectmvvmandroid.view.ui.textrecognition.TextRecognitionActivity
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.GaleryActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnPaging.setOnClickListener {
            val intent = Intent(this, ListUserActivity::class.java)
            startActivity(intent)
        }
        binding.btnMaps.setOnClickListener {
            openActivity(MapsActivity::class.java)
        }

        binding.btnLogin.setOnClickListener {
            openActivity(LoginActivity::class.java)
        }

        binding.btnMapsSatellite.setOnClickListener {
            openActivity(MapSatelliteActivity::class.java)
        }

        binding.btnFaceDetection.setOnClickListener {
            openActivity(FaceDetectionActivity::class.java)
        }
        binding.btnImageWatermark.setOnClickListener {
            openActivity(GaleryActivity::class.java)
        }

        binding.btnTextRecognition.setOnClickListener {
            openActivity(TextRecognitionActivity::class.java)
        }

        binding.btnObjectDetection.setOnClickListener {
            openActivity(ObjectDetectionActivity::class.java)
        }

        viewModelEvent()
    }

    private fun viewModelEvent() {
        viewModel.dataRequestLogin.observe(this) { data ->
            if (data != null) {
                if (data.token != null) {
                    binding.txtText.text = data.token.toString()
                }
            }
        }
        viewModel.requestLogin(LoginRequest(email = "eve.holt@reqres.in", password = "cityslicka"))
    }
}