/*
 * Created by Muhamad Riza
 * , 17/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.request.LoginRequest
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.response.DataItem
import com.gibox.baseprojectmvvmandroid.core.data.vo.Resource
import com.gibox.baseprojectmvvmandroid.core.domain.auth.model.LoginEntityDomain
import com.gibox.baseprojectmvvmandroid.core.domain.auth.usecase.AuthUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val authUseCase:AuthUseCase):ViewModel() {

    private val _isErrorRequestLogin = MutableLiveData<String>()
    private val _dataRequestLogin = MutableLiveData<LoginEntityDomain>()
    private val _isLoadingRequestLogin = MutableLiveData<Boolean>()

    val isErrorRequestLogin = _isErrorRequestLogin
    val dataRequestLogin = _dataRequestLogin
    val isLoadingRequestLogin = _isLoadingRequestLogin

    fun requestLogin(loginRequest:LoginRequest){
        viewModelScope.launch {
            authUseCase.doLogin(loginRequest)
                .onStart { _isLoadingRequestLogin.postValue(true) }
                .onCompletion { _isLoadingRequestLogin.postValue(false) }
                .collect {data->
                    when(data){
                        is Resource.Loading->_isLoadingRequestLogin.postValue(true)
                        is Resource.Success->{
                            _dataRequestLogin.postValue(data.data)
                        }
                        is Resource.Error->{
                            _isErrorRequestLogin.postValue(data.message)
                        }
                    }
                }

        }
    }

    fun getDataListUser(): Flow<PagingData<UiUserModel.DetailListUserItem>> {
        return authUseCase.getUserList()
            .map { pagingData->pagingData.map { UiUserModel.DetailListUserItem(it) } }
            .cachedIn(viewModelScope)
    }

    sealed class UiUserModel {
        data class DetailListUserItem(val dataListUser: DataItem) : UiUserModel()
    }


}