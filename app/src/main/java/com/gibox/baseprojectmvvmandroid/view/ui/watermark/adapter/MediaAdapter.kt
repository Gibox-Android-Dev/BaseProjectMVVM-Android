/*
 * Created by Muhamad Syafii
 * , 3/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.watermark.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gibox.baseprojectmvvmandroid.R
import com.gibox.baseprojectmvvmandroid.databinding.ItemImageBinding
import com.gibox.baseprojectmvvmandroid.util.ItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class MediaAdapter:RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    var listData:ArrayList<File?> = ArrayList()

    var imageCallback:ImageCallbackListener? = null

    var context:Context? = null

    var stateChoice = false

    var positionList:ArrayList<Int> = arrayListOf()

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    fun setData(data:ArrayList<File?>?){
        if (data==null)
            return
        positionList.clear()
        listData.clear()
        listData.addAll(data)
        for (i in data){
            positionList.add(0)
        }
        stateChoice = false
        notifyDataSetChanged()
    }

    fun setImageCallbackListener(imageCallbackListener: ImageCallbackListener){
        this.imageCallback = imageCallbackListener
    }

    fun restartData(){
        stateChoice = false
        positionList.clear()
        for (i in listData){
            positionList.add(0)
        }
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MediaAdapter.ViewHolder, position: Int) {
        holder.bind(listData[position],position)
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(private val binding: ItemImageBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(file: File?,pos:Int){

            Glide.with(binding.root)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imgImage)

            binding.crdLyt.setCardBackgroundColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.white
                )
            )
            binding.imgImage.setOnClickListener {
                if(file!=null){
                    if (!stateChoice){
                        imageCallback?.onItemClick(file)
                    }else{
                        if (positionList[pos]==0){
                            imageCallback?.onItemChoice(file)
                            binding.crdLyt.setCardBackgroundColor(
                                ContextCompat.getColor(
                                    context!!,
                                    R.color.blue
                                )
                            )
                            positionList[pos] = 1
                        }else{
                            binding.crdLyt.setCardBackgroundColor(
                                ContextCompat.getColor(
                                    context!!,
                                    R.color.white
                                )
                            )
                            positionList[pos] = 0
                            imageCallback?.ontemCancelChoice(file)
                        }
                    }
                }
            }

            binding.imgImage.setOnLongClickListener {
                if (file != null && !stateChoice) {
                    binding.crdLyt.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.blue
                        )
                    )
                    positionList[pos] = 1
                    stateChoice = true
                    imageCallback?.onItemChoice(file)
                    imageCallback?.onItemChoiceState()
                }
                true
            }
        }
    }

    interface ImageCallbackListener{
        fun onItemClick(file:File)
        fun onItemChoice(file:File)
        fun onItemChoiceState()
        fun ontemCancelChoice(file:File)
    }


}

