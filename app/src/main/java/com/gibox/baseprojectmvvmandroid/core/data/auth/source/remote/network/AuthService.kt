/*
 * Created by Muhamad Riza
 * , 14/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.network

import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.request.LoginRequest
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.response.LoginResponse
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.response.ResponseListUser
import retrofit2.http.*

interface AuthService {
    @POST("/api/login")
    suspend fun authenticate(@Body body: LoginRequest?): LoginResponse

    @GET("/api/users")
    suspend fun dataListUser(
        @Query("page") Page: Int
    ):ResponseListUser
}