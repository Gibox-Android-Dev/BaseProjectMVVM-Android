/*
 * Created by Muhamad Syafii
 * Wednesday, 26/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.di

import com.gibox.baseprojectmvvmandroid.core.session.SessionManager
import com.gibox.baseprojectmvvmandroid.core.session.SessionRepository
import org.koin.dsl.module

val sessionModule = module {
    single {
        SessionManager(get())
    }
    single {
        SessionRepository(sesi = get())
    }
}