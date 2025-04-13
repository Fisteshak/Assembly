package com.rtuitlab.assembly.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.rtuitlab.assembly.MainStore
import com.rtuitlab.assembly.MainStoreFactory

// manual DI
// TODO следить за проблемами с жизненным циклом
//  (без понятия есть ли они и как это работает в вебе)
internal object AppModule {
    init {
        println("Created AppModule")
    }

    private var mainStore: MainStore? = null

    internal fun getMainStore(): MainStore {

        return mainStore
            ?: MainStoreFactory(DefaultStoreFactory()).create()
                .also {
                    mainStore = it
                    println("Created MainStore")
                }
    }
}
