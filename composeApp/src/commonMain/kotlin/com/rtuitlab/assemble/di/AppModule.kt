package com.rtuitlab.assemble.di

import com.arkivanov.mvikotlin.core.store.StoreEventType
import com.arkivanov.mvikotlin.logging.logger.LogFormatter
import com.arkivanov.mvikotlin.logging.logger.Logger
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.rtuitlab.assemble.AssembleStoreFactory
import com.rtuitlab.assemble.data.WebClient
import com.rtuitlab.assemble.data.api.AssembleApi
import com.rtuitlab.assemble.data.api.ComponentApi
import com.rtuitlab.assemble.data.api.ContainerApi
import com.rtuitlab.assemble.data.repositories.AssembliesRepository
import com.rtuitlab.assemble.data.repositories.ComponentsRepository
import com.rtuitlab.assemble.data.repositories.ContainersRepository
import com.rtuitlab.assemble.domain.usecases.GenerateSoundByIdUseCase
import com.rtuitlab.assemble.domain.usecases.assemblies.CreateAssembleUseCase
import com.rtuitlab.assemble.domain.usecases.assemblies.DeleteAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.assemblies.GetAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.assemblies.GetAssembliesUseCase
import com.rtuitlab.assemble.domain.usecases.assemblies.UpdateAssembleUseCase
import com.rtuitlab.assemble.domain.usecases.components.GetComponentsUseCase
import com.rtuitlab.assemble.domain.usecases.containers.CreateContainerUseCase
import com.rtuitlab.assemble.domain.usecases.containers.DeleteContainerByNumberUseCase
import com.rtuitlab.assemble.domain.usecases.containers.GetContainerByNumberUseCase
import com.rtuitlab.assemble.domain.usecases.containers.GetContainersUseCase
import com.rtuitlab.assemble.domain.usecases.containers.UpdateContainerByIdUseCase
import com.rtuitlab.assemble.ui.container.store.ContainerStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val koinModule = module {

    singleOf(::WebClient)
    // api
    singleOf(::AssembleApi)
    singleOf(::ContainerApi)
    singleOf(::ComponentApi)
    // repository
    singleOf(::AssembliesRepository)
    singleOf(::ContainersRepository)
    singleOf(::ComponentsRepository)
    // usecases assemble
    singleOf(::CreateAssembleUseCase)
    singleOf(::UpdateAssembleUseCase)
    singleOf(::GetAssembleByIdUseCase)
    singleOf(::DeleteAssembleByIdUseCase)
    singleOf(::GetAssembliesUseCase)
    // usecases container
    singleOf(::GetContainersUseCase)
    singleOf(::GetContainerByNumberUseCase)
    singleOf(::CreateContainerUseCase)
    singleOf(::UpdateContainerByIdUseCase)
    singleOf(::DeleteContainerByNumberUseCase)
    // usecases component
    singleOf(::GetComponentsUseCase)
    // usecases sound
    singleOf(::GenerateSoundByIdUseCase)

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
            getComponentsUseCase = get(),
            createContainerUseCase = get(),
            updateContainerUseCase = get(),
            deleteContainerByNumberUseCase = get(),
        ).create()
    }

}