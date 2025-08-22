package com.kyokosawada.data

import android.app.Application
import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

import com.kyokosawada.data.product.ProductRepository
import com.kyokosawada.data.product.ProductViewModel

/**
 * Koin DI module for inventory/data layer.
 */
val dataModule = module {
    single { Room.databaseBuilder(get<Application>(), AppDatabase::class.java, "pos-db").build() }
    single { get<AppDatabase>().productDao() }
    single { ProductRepository(get()) }
    factory { ProductViewModel(get()) }
}
