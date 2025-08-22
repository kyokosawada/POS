package com.kyokosawada.data.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ProductViewModel for inventory feature, MVVM, Compose-ready.
 */
class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    val products: StateFlow<List<ProductEntity>> = _products.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        observeProducts()
    }

    private fun observeProducts() {
        viewModelScope.launch {
            repository.getProducts().catch { e -> _error.value = e.localizedMessage }
                .collect { _products.value = it }
        }
    }

    fun searchProducts(query: String) {
        _loading.value = true
        viewModelScope.launch {
            repository.searchProducts(query)
                .onEach { _products.value = it }
                .catch { _error.value = it.localizedMessage }
                .onCompletion { _loading.value = false }
                .collect()
        }
    }

    fun addProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.insertProduct(product)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.updateProduct(product)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.deleteProduct(product)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _loading.value = false
            }
        }
    }
}
