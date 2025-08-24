package com.kyokosawada.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyokosawada.data.settings.SettingsRepository
import com.kyokosawada.data.settings.SettingsEntity
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Settings MVVM flow. Collects state and delegates update ops.
 */
class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {
    val settings: StateFlow<SettingsEntity> = settingsRepository.settingsFlow

    fun updateShopName(shopName: String) = viewModelScope.launch {
        settingsRepository.updateShopName(shopName)
    }

    fun updateLogoUri(logoUri: String?) = viewModelScope.launch {
        settingsRepository.updateLogoUri(logoUri)
    }

    fun updateThemeColor(themeColor: Long) = viewModelScope.launch {
        settingsRepository.updateThemeColor(themeColor)
    }

    fun updateCurrency(currency: String) = viewModelScope.launch {
        settingsRepository.updateCurrency(currency)
    }

    fun updateTaxRate(taxRate: Double) = viewModelScope.launch {
        settingsRepository.updateTaxRate(taxRate)
    }

    fun updateDarkMode(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateDarkMode(enabled)
    }

    fun updateLowStockThreshold(threshold: Int) = viewModelScope.launch {
        settingsRepository.updateLowStockThreshold(threshold)
    }

    fun updateAlertEnabled(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateAlertEnabled(enabled)
    }

    fun exportSalesCsv() = viewModelScope.launch {
        settingsRepository.exportSalesCsv()
    }

    fun resetToDefaults() = viewModelScope.launch {
        settingsRepository.resetToDefaults()
    }
}
