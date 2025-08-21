package com.kyokosawada

import android.app.Application
import com.kyokosawada.data.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Application class for Koin startup and DI registry.
 */
class POSApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@POSApplication)
            modules(dataModule)
        }
    }
}
