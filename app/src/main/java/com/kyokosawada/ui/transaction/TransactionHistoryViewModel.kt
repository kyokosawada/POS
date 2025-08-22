package com.kyokosawada.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyokosawada.data.transaction.TransactionWithItems
import com.kyokosawada.data.transaction.usecase.GetTransactionsUseCase
import com.kyokosawada.data.transaction.usecase.GetTransactionByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for transaction history: exposes list, detail, loads via use cases.
 */
class TransactionHistoryViewModel(
    private val getTransactions: GetTransactionsUseCase,
    private val getTransactionById: GetTransactionByIdUseCase
) : ViewModel() {
    private val _transactions = MutableStateFlow<List<TransactionWithItems>>(emptyList())
    val transactions: StateFlow<List<TransactionWithItems>> = _transactions

    private val _selectedTransaction = MutableStateFlow<TransactionWithItems?>(null)
    val selectedTransaction: StateFlow<TransactionWithItems?> = _selectedTransaction

    init {
        viewModelScope.launch {
            getTransactions().collect { txs ->
                _transactions.value = txs
            }
        }
    }

    fun loadTransactionDetail(id: Long) {
        viewModelScope.launch {
            getTransactionById(id).collect { tx ->
                _selectedTransaction.value = tx
            }
        }
    }
}
