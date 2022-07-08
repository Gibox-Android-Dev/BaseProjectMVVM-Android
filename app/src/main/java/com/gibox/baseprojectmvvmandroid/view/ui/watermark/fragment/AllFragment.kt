/*
 * Created by Muhammad Riza
 * Thursday, 3/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.watermark.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.ActivityImageView
import com.gibox.baseprojectmvvmandroid.databinding.FragmentAllBinding
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.GaleryActivity
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.VideoViewActivity
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.adapter.MediaAdapter
import java.io.File


class AllFragment : Fragment() {


    var dialogView: View? = null
    var previewDialog: AlertDialog? = null

    var listfile:ArrayList<File> = arrayListOf()

    lateinit var activity:GaleryActivity

    private lateinit var binding:FragmentAllBinding
    val mediaAdapter = MediaAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllBinding.inflate(inflater, container, false)
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
                Log.d("dataSizeFragmentAll",data.size.toString())
                mediaAdapter.setData(data)
                mediaAdapter.setImageCallbackListener(object : MediaAdapter.ImageCallbackListener{
                    override fun onItemClick(file: File) {

                        //setDialog(file)
                        //activity.setSumChoiceFile(listfile.size)
                        if (file.name.endsWith(".mp4")){
                            val intent = Intent(requireContext(),VideoViewActivity::class.java)
                            intent.putExtra(VideoViewActivity.ARG_FILE,file.absolutePath)
                            startActivity(intent)
                        }else{
                            val intent = Intent(requireContext(), ActivityImageView::class.java)
                            intent.putExtra(ActivityImageView.ARG_FILE,file.absolutePath)
                            startActivityForResult(intent,GaleryActivity.REQUEST_VIEW_PICTURE)
                        }
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


    private fun getImagesFromDCIM()
    {
        val dcimPath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM+"/BaseGallery").toString())

        binding.rvItem.getRecycledViewPool().clear();
        if (dcimPath.exists()) {
            val files = dcimPath.listFiles();
            val data:ArrayList<File?> = ArrayList()
            for (i in files.size-1 downTo 0)
                data.add(files[i])
                    }else{
            Log.d("JumlahFile","Kosong")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==GaleryActivity.REQUEST_VIEW_PICTURE && resultCode== GaleryActivity.RESPONSE_DELETE_PICTURE){
            activity.viewModel.getImageFromGalery()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    /*fun setDialog(file:File){
        dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_full_image, null)
        previewDialog = AlertDialog.Builder(requireContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen).create()
        previewDialog?.window?.attributes?.windowAnimations = R.style.DialogScale
        previewDialog?.setView(dialogView)
        var imageFull =dialogView?.findViewById<ImageView>(R.id.img_full)
        var videoFull= dialogView?.findViewById<VideoView>(R.id.vv_full)
        //var vvExoPlayer = dialogView?.findViewById<PlayerView>(R.id.vv_exoPlayer)
        if (file.name.endsWith(".mp4")){
            *//*videoFull?.visibility =View.VISIBLE
            imageFull?.visibility =View.GONE
            videoFull?.setVideoPath(file.absolutePath)
            var mediaController = MediaController(dialogView?.context)
            mediaController.setAnchorView(videoFull)
            videoFull?.setMediaController(mediaController)
            videoFull?.start()*//*

*//*
            val uri = Uri.fromFile(file)
            vvExoPlayer?.player*//*
            val intent = Intent(requireContext(),VideoViewActivity::class.java)
            intent.putExtra(VideoViewActivity.ARG_FILE,file.absolutePath)
            startActivity(intent)

        }else{
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            imageFull?.setImageBitmap(bitmap)
            videoFull?.visibility =View.GONE
        }

        previewDialog?.show()

    }*/


    fun setImageMultipleChoice(status:Boolean){
        if (!status){
            mediaAdapter.restartData()
            listfile.clear()
        }else{
            activity.setStateMultipleChoice(status)
        }
    }


}