/*
 * Created by Muhamad Riza
 * , 14/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.core.data.persistences.mapper.auth

import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.response.LoginResponse
import com.gibox.baseprojectmvvmandroid.core.domain.auth.model.LoginEntityDomain

object AuthDataMapper {
    fun mapResponseToDomain(input:LoginResponse):LoginEntityDomain{
        val loginEntityDomain =LoginEntityDomain(
            token = input.token,
            page = input.page
        )
        return loginEntityDomain
    }
}