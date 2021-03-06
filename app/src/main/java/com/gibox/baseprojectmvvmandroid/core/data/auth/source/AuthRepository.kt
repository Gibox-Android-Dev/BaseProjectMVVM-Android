/*
 * Created by Muhamad Riza
 * , 14/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.core.data.auth.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.gibox.baseprojectmvvmandroid.core.ApiResponse
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.AuthRemoteDataSource
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.request.LoginRequest
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.response.DataItem
import com.gibox.baseprojectmvvmandroid.core.data.persistences.mapper.auth.AuthDataMapper
import com.gibox.baseprojectmvvmandroid.core.data.vo.Resource
import com.gibox.baseprojectmvvmandroid.core.domain.auth.model.LoginEntityDomain
import com.gibox.baseprojectmvvmandroid.core.domain.auth.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

@ExperimentalPagingApi
class AuthRepository(private val authRemoteDataSource: AuthRemoteDataSource): IAuthRepository {


    override fun doLogin(dataLogin: LoginRequest): Flow<Resource<LoginEntityDomain>> = flow {
        emit(Resource.Loading())
        when(val apiResponse = authRemoteDataSource.doLogin(dataLogin).first()){
            is ApiResponse.Success->{
                val response= AuthDataMapper.mapResponseToDomain(apiResponse.data)
                emit(Resource.Success(response))
            }
            is ApiResponse.Empty->{
                emit(Resource.Error<LoginEntityDomain>("Something went wrong"))
            }
            is ApiResponse.Error->{
                emit(Resource.Error<LoginEntityDomain>(apiResponse.errorMessage))
            }
        }
    }

    override fun getUserList(): Flow<PagingData<DataItem>> {
        return authRemoteDataSource.getListUser()
    }


}