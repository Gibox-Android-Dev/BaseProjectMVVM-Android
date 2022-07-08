/*
 * Created by Muhamad Riza
 * , 17/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.listuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gibox.baseprojectmvvmandroid.databinding.ActivityListUserBinding
import com.gibox.baseprojectmvvmandroid.view.adapter.pagingadapter.ReposLoadStateAdapter
import com.gibox.baseprojectmvvmandroid.util.closeActivity
import com.gibox.baseprojectmvvmandroid.view.ui.home.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class ListUserActivity : AppCompatActivity() {

    private val binding by lazy { ActivityListUserBinding.inflate(layoutInflater) }

    private val viewModel: MainViewModel by viewModel()

    private var showJob: Job? = null

    var adapter: ListUserPagedAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = ListUserPagedAdapter()

        binding.rvItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvItem.adapter = adapter
        binding.retryButton.setOnClickListener {
            adapter?.retry()
        }
        lifecycleScope.launch {
            adapter?.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                ?.distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                ?.filter { it.refresh is LoadState.NotLoading }
                ?.collect { binding.rvItem.scrollToPosition(0) }
        }

        binding.imBack.setOnClickListener {
            closeActivity()
        }

        initAdapter()
        getData()


    }


    /**
     * fungsi getData untuk merequest data baru , misal paging data melakukan filter data sehingga
     * harus megirim data dan mengambil ulang data sesuai filteran
     *
     * @showjob = variabel ini digunakan untuk menghentikan asyn request
     */

    private fun getData(){
        showJob?.cancel()
        showJob = lifecycleScope.launch {
            viewModel.getDataListUser()
                .collectLatest {
                    adapter?.submitData(it)
                    adapter?.notifyDataSetChanged()
                }
        }

    }


    /**
     * fungsi initAdapter digunakan untuk manipulasi adapter , seperti menambah layout footer , mendeteksi
     * kalau data pada adapter kosong , mendeteksi apakah adapter sedang loading atau tidak dan sebagainya
     */

    private fun initAdapter(){
        binding.rvItem.adapter = adapter?.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter{adapter?.retry()},
            footer = ReposLoadStateAdapter{adapter?.retry()}
        )

        adapter?.addLoadStateListener { loadState->
            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter?.itemCount == 0
            // Only show the list if refresh succeeds.
            //binding.list.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            if (loadState.refresh is LoadState.NotLoading){
                //binding.rvItem.visibility = View.VISIBLE
                binding.pbLoading.visibility = View.GONE
            }
            binding.pbLoading.isVisible = loadState.refresh is LoadState.Loading
            binding.rvItem.isVisible = loadState.refresh is LoadState.NotLoading
            if (binding.rvItem.isVisible && binding.rvItem.adapter?.itemCount == 0){
                Log.d("isiAdapterMasuk","Masuk")
                //emptyImage(true)
            }
            if(binding.rvItem.isVisible == false){
                //emptyImage(false)
            }
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Wooops, Koneksi bermasalah.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


}