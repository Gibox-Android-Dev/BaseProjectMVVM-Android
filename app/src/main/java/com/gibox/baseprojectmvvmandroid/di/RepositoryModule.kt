/*
 * Created by Muhamad Riza
 * , 14/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.di

import androidx.paging.ExperimentalPagingApi
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.AuthRepository
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.AuthRemoteDataSource
import com.gibox.baseprojectmvvmandroid.core.domain.auth.repository.IAuthRepository
import org.koin.dsl.module

@ExperimentalPagingApi
val repositoryModule = module {

    single { AuthRemoteDataSource(authService = get()) }
    single<IAuthRepository> { AuthRepository(authRemoteDataSource = get()) }

}