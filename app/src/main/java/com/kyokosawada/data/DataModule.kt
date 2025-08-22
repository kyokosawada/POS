package com.kyokosawada.data

import android.app.Application
import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

import com.kyokosawada.data.product.ProductRepository
import com.kyokosawada.data.product.ProductRepositoryImpl
import com.kyokosawada.ui.product.ProductViewModel

// Cart feature imports
import com.kyokosawada.data.cart.CartRepository
import com.kyokosawada.data.cart.CartRepositoryImpl
import com.kyokosawada.data.cart.usecase.AddToCartUseCase
import com.kyokosawada.data.cart.usecase.RemoveFromCartUseCase
import com.kyokosawada.data.cart.usecase.UpdateCartItemQuantityUseCase
import com.kyokosawada.data.cart.usecase.ClearCartUseCase
import com.kyokosawada.data.cart.usecase.CheckoutCartUseCase
import com.kyokosawada.ui.cart.CartViewModel

// Transaction feature imports
import com.kyokosawada.data.transaction.TransactionDao
import com.kyokosawada.data.transaction.TransactionRepository
import com.kyokosawada.data.transaction.TransactionRepositoryImpl
import com.kyokosawada.data.transaction.usecase.CreateTransactionUseCase
import com.kyokosawada.data.transaction.usecase.GetTransactionsUseCase
import com.kyokosawada.data.transaction.usecase.GetTransactionByIdUseCase
import com.kyokosawada.ui.transaction.TransactionHistoryViewModel

/**
 * Koin DI module for inventory/data layer.
 * Uses singletons for data/resources, factories for use cases & ViewModel.
 */
val dataModule = module {
    // Database & Product layer
    single {
        Room.databaseBuilder(get<Application>(), AppDatabase::class.java, "pos-db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().productDao() }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    factory { ProductViewModel(get()) }

    // Cart features
    single<CartRepository> { CartRepositoryImpl() }
    factory { AddToCartUseCase(get()) }
    factory { RemoveFromCartUseCase(get()) }
    factory { UpdateCartItemQuantityUseCase(get()) }
    factory { ClearCartUseCase(get()) }
    factory { CheckoutCartUseCase(get()) }
    factory {
        CartViewModel(
            get(), // CartRepository
            get(), // AddToCartUseCase
            get(), // RemoveFromCartUseCase
            get(), // UpdateCartItemQuantityUseCase
            get(), // ClearCartUseCase
            get(), // CheckoutCartUseCase
            get(), // CreateTransactionUseCase
            get()  // ProductRepository for stock updates
        )
    }

    // Transaction features
    single { get<AppDatabase>().transactionDao() }
    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
    factory { CreateTransactionUseCase(get()) }
    factory { GetTransactionsUseCase(get()) }
    factory { GetTransactionByIdUseCase(get()) }
    factory { TransactionHistoryViewModel(get(), get()) }
}
