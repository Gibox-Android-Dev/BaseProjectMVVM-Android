/*
 * Created by Muhammad Riza
 * Thursday, 3/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.watermark.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.gibox.baseprojectmvvmandroid.R
import com.gibox.baseprojectmvvmandroid.databinding.FragmentAllBinding
import com.gibox.baseprojectmvvmandroid.databinding.FragmentImageBinding
import com.gibox.baseprojectmvvmandroid.databinding.FragmentVideoBinding
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.ActivityImageView
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.GaleryActivity
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.VideoViewActivity
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.adapter.MediaAdapter
import java.io.File

class VideoFragment : Fragment() {


    var dialogView: View? = null
    var previewDialog: AlertDialog? = null

    var listfile:ArrayList<File> = arrayListOf()

    lateinit var activity: GaleryActivity

    private lateinit var binding: FragmentVideoBinding
    val mediaAdapter = MediaAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity = getActivity() as GaleryActivity

        binding.rvItem.setHasFixedSize(true)
        binding.rvItem.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvItem.adapter = mediaAdapter

        setVMAction()
    }
    private fun setVMAction() {
        activity.viewModel.dataMedia.observe(viewLifecycleOwner,{data->
            if (data!=null){

                var listData:ArrayList<File?> = arrayListOf()

                for(item in data){
                    if (item?.name?.endsWith(".mp4")==true){
                        listData.add(item)
                    }
                }

                mediaAdapter.setData(listData)
                mediaAdapter.setImageCallbackListener(object : MediaAdapter.ImageCallbackListener{
                    override fun onItemClick(file: File) {
                        val intent = Intent(requireContext(), VideoViewActivity::class.java)
                        intent.putExtra(VideoViewActivity.ARG_FILE,file.absolutePath)
                        startActivity(intent)
                    }

                    override fun onItemChoice(file: File) {
                        listfile.add(file)
                        Log.d("ukuranfile",listfile.size.toString())
                        activity.binding.txtSum.setText(listfile.size.toString())
                    }

                    override fun onItemChoiceState() {
                        setImageMultipleChoice(true)
                    }

                    override fun ontemCancelChoice(file: File) {
                        listfile.removeAll {
                            it == file
                        }
                        activity.setSumChoiceFile(listfile.size)
                    }

                })
            }
        })

        activity.viewModel.backEvent.observe(viewLifecycleOwner,{status->
            if (status){
                mediaAdapter.restartData()
                listfile.clear()
            }
        })


    }

    override fun onResume() {
        super.onResume()
    }



    fun setImageMultipleChoice(status:Boolean) {
        if (!status) {
            mediaAdapter.restartData()
            listfile.clear()
        } else {
            activity.setStateMultipleChoice(status)
        }
    }
}