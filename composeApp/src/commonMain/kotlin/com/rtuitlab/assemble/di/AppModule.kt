package com.rtuitlab.assemble.di

import com.arkivanov.mvikotlin.core.store.StoreEventType
import com.arkivanov.mvikotlin.logging.logger.LogFormatter
import com.arkivanov.mvikotlin.logging.logger.Logger
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.rtuitlab.assemble.AssembleStoreFactory
import com.rtuitlab.assemble.data.api.AssembleApi
import com.rtuitlab.assemble.data.api.ContainerApi
import com.rtuitlab.assemble.data.api.WebClient
import com.rtuitlab.assemble.data.repositores.AssembliesRepository
import com.rtuitlab.assemble.data.repositores.ContainersRepository
import com.rtuitlab.assemble.domain.usecases.CreateAssembleUseCase
import com.rtuitlab.assemble.domain.usecases.DeleteAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.GenerateSoundByIdUseCase
import com.rtuitlab.assemble.domain.usecases.GetAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.GetAssembliesUseCase
import com.rtuitlab.assemble.domain.usecases.GetComponentsUseCase
import com.rtuitlab.assemble.domain.usecases.GetContainerByNumberUseCase
import com.rtuitlab.assemble.domain.usecases.GetContainersUseCase
import com.rtuitlab.assemble.domain.usecases.UpdateAssembleUseCase
import com.rtuitlab.assemble.ui.container.store.ContainerStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val koinModule = module {

    singleOf(::WebClient)

    singleOf(::AssembleApi)
    singleOf(::ContainerApi)

    singleOf(::AssembliesRepository)
    singleOf(::ContainersRepository)

    singleOf(::CreateAssembleUseCase)
    singleOf(::UpdateAssembleUseCase)
    singleOf(::GetAssembleByIdUseCase)
    singleOf(::DeleteAssembleByIdUseCase)
    singleOf(::GetAssembliesUseCase)
    singleOf(::GetComponentsUseCase)
    singleOf(::GenerateSoundByIdUseCase)
    singleOf(::GetContainersUseCase)
    singleOf(::GetContainerByNumberUseCase)

    single {
        AssembleStoreFactory(
            storeFactory = LoggingStoreFactory(
                delegate = DefaultStoreFactory(),
                logger = object : Logger {
                    override fun log(text: String) {
                        println(text)
                    }
                },
                logFormatter = object : LogFormatter {
                    override fun format(
                        storeName: String,
                        eventType: StoreEventType,
                        value: Any?
                    ): String? {
                        return "${eventType.name}: ${value.toString()}"
                    }

                }
            ),
            getAssembliesUseCase = get(),
            getAssembleByIdUseCase = get(),
            getComponentsUseCase = get(),
            createAssembleUseCase = get(),
            updateAssembleUseCase = get(),
            deleteAssembleByIdUseCase = get(),
            generateSoundByIdUseCase = get()
        ).create()
    }

    single {
        ContainerStoreFactory(
            storeFactory = LoggingStoreFactory(
                delegate = DefaultStoreFactory(),
                logger = object : Logger {
                    override fun log(text: String) {
                        println(text)
                    }
                },
                logFormatter = object : LogFormatter {
                    override fun format(
                        storeName: String,
                        eventType: StoreEventType,
                        value: Any?
                    ): String? {
                        return "${eventType.name}: ${value.toString()}"
                    }

                }
            ),
            getContainersUseCase = get(),
            getContainerByNumberUseCase = get(),
        ).create()
    }

}