/*
 * Created by Muhamad Riza
 * , 17/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.listuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.response.DataItem
import com.gibox.baseprojectmvvmandroid.databinding.ItemUserBinding
import com.gibox.baseprojectmvvmandroid.view.ui.home.MainViewModel

class ListUserPagedAdapter:PagingDataAdapter<MainViewModel.UiUserModel.DetailListUserItem, RecyclerView.ViewHolder>(
    UIMODEL_COMPARATOR
) {

    companion object{
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<MainViewModel.UiUserModel.DetailListUserItem>(){
            override fun areItemsTheSame(oldItem: MainViewModel.UiUserModel.DetailListUserItem, newItem: MainViewModel.UiUserModel.DetailListUserItem): Boolean {
                return (oldItem is MainViewModel.UiUserModel.DetailListUserItem && newItem is MainViewModel.UiUserModel.DetailListUserItem && oldItem.dataListUser.id == newItem.dataListUser.id && oldItem.dataListUser.email == newItem.dataListUser.email)
            }

            override fun areContentsTheSame(oldItem: MainViewModel.UiUserModel.DetailListUserItem, newItem: MainViewModel.UiUserModel.DetailListUserItem): Boolean = oldItem==newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        (holder as UserListViewHolder).bind(uiModel?.dataListUser!!)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserListViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    inner class UserListViewHolder(private val binding: ItemUserBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem) {
            showRepoData(item)
        }

        private fun showRepoData(data: DataItem) {
            binding.run {
                txtName.text = data.firstName
                txtEmail.text = data.email

                Glide.with(itemView)
                    .load(data.avatar)
                    .into(imgImage)
            }

        }
    }
}