/*
 * Created by Muhamad Syafii
 * , 9/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.watermark

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.gibox.baseprojectmvvmandroid.databinding.ActivityVideoViewBinding
import android.R
import android.view.MotionEvent
import android.view.View
import android.view.KeyEvent


class VideoViewActivity : AppCompatActivity() , MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener, View.OnTouchListener {

    companion object{
        var ARG_FILE = "arg_file"
    }

    var isVideoStarted = false
    var mediaController:MediaController? = null

    val binding by lazy { ActivityVideoViewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var file = intent.getStringExtra(ARG_FILE)

        binding.vvVideo.setVideoPath(file)
        mediaController = object : MediaController(this){
            override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
                if (event?.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP)
                    this@VideoViewActivity.finish()
                return super.dispatchKeyEvent(event)
            }
        }
        mediaController?.setAnchorView(binding.vvVideo)
        binding.vvVideo.setMediaController(mediaController)
        binding.vvVideo.start()
        binding.vvVideo.stopPlayback()

        binding.vvVideo.setOnCompletionListener(this);
        binding.vvVideo.setOnPreparedListener(this);
        binding.vvVideo.setOnTouchListener(this);

    }

    override fun onPause() {
        super.onPause()
        binding.vvVideo.pause()
    }

    override fun onStop() {
        super.onStop()

    }

    private fun play() {
        if (!isVideoStarted) {
            isVideoStarted = true
            binding.vvVideo.start()
            //play.setImageResource(R.drawable.video_pause)
            //videoSeekBar.post(updateSeekBarRunnable)
        } else if (isVideoStarted) {
            isVideoStarted = false
            pause()
        }
    }



    fun pause(){
        binding.vvVideo.pause();
    }
    private fun stopPlayback() {
        binding.vvVideo.stopPlayback()
    }

    override fun onPrepared(p0: MediaPlayer?) {
        play()
    }

    override fun onCompletion(p0: MediaPlayer?) {

    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}