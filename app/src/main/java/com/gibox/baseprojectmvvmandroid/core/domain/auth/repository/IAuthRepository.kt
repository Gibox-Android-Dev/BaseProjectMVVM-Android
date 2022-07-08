/*
 * Created by Muhamad Riza
 * , 14/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.core.domain.auth.repository

import androidx.paging.PagingData
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.request.LoginRequest
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.response.DataItem
import com.gibox.baseprojectmvvmandroid.core.data.vo.Resource
import com.gibox.baseprojectmvvmandroid.core.domain.auth.model.LoginEntityDomain
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun doLogin(dataLogin:LoginRequest): Flow<Resource<LoginEntityDomain>>
    fun getUserList():Flow<PagingData<DataItem>>
}