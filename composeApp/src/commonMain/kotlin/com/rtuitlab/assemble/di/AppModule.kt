package com.rtuitlab.assemble.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.rtuitlab.assemble.MainStoreFactory
import com.rtuitlab.assemble.data.repositores.AssembleApi
import com.rtuitlab.assemble.data.repositores.AssembliesRepository
import com.rtuitlab.assemble.domain.usecases.CreateAssembleUseCase
import com.rtuitlab.assemble.domain.usecases.GetAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.GetAssembliesUseCase
import com.rtuitlab.assemble.domain.usecases.GetComponentsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val koinModule = module {


    singleOf(::AssembleApi)

    singleOf(::AssembliesRepository)

    singleOf(::CreateAssembleUseCase)
    singleOf(::GetAssembleByIdUseCase)
    singleOf(::GetAssembliesUseCase)
    singleOf(::GetComponentsUseCase)

    single {
        MainStoreFactory(
            storeFactory = DefaultStoreFactory(),
            getAssembliesUseCase = get(),
            getAssembleByIdUseCase = get(),
            getComponentsUseCase = get(),
        ).create()
    }
}