/*
 * Created by Muhamad Riza
 * , 14/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.di

import androidx.paging.ExperimentalPagingApi
import com.gibox.baseprojectmvvmandroid.AppController
import com.gibox.baseprojectmvvmandroid.BuildConfig
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.AuthRepository
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.AuthRemoteDataSource
import com.gibox.baseprojectmvvmandroid.core.data.auth.source.remote.network.AuthService
import com.gibox.baseprojectmvvmandroid.core.domain.auth.repository.IAuthRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import org.koin.core.qualifier.named
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalPagingApi
val networkModule = module {
    single {
        OkHttpClient.Builder().apply {
            if (!BuildConfig.IS_RELEASE) addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY))
        }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single(named("Base")) {
        return@single Retrofit.Builder()
            .baseUrl(AppController().baseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()

    }

    single{
        get<Retrofit>(named("Base")).create(AuthService::class.java)
    }

}

