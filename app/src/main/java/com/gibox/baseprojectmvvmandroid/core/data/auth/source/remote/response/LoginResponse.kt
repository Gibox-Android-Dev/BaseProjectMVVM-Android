/*
 * Created by Muhamad Riza
 * , 14/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("token")
    val token: String?
)