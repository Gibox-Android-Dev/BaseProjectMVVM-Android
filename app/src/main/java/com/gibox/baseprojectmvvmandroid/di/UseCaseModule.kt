/*
 * Created by Muhamad Riza
 * , 14/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.di

import com.gibox.baseprojectmvvmandroid.core.domain.auth.usecase.AuthInteractor
import com.gibox.baseprojectmvvmandroid.core.domain.auth.usecase.AuthUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<AuthUseCase>{ AuthInteractor(get()) }
}