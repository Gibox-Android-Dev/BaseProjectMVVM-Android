/*
 * Created by Muhamad Riza
 * , 14/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gibox.baseprojectmvvmandroid.core.ApiResponse
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.network.AuthService
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.request.LoginRequest
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.response.DataItem
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

@ExperimentalPagingApi
class AuthRemoteDataSource(private val authService: AuthService) {

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

    suspend fun doLogin(dataLogin: LoginRequest): Flow<ApiResponse<LoginResponse>> {
        return flow {
            try {
                val response = authService.authenticate(dataLogin)
                if (response.token!=null){
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: HttpException){
                if (e.code() in 400..499){
                    emit(ApiResponse.Error("Email Atau Password Salah"))
                }else if (e.code()==500){
                    emit(ApiResponse.Error("Kesalahan Server, Coba Lagi"))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e:Exception){
                emit(ApiResponse.Error("Koneksi Bermasalah, Coba Lagi"))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getListUser():Flow<PagingData<DataItem>>{
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE,enablePlaceholders = false),
            pagingSourceFactory = {UserListPagingSource(authService)}
        ).flow
    }
}