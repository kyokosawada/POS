package com.kyokosawada.data.settings

import kotlinx.coroutines.flow.StateFlow

/**
 * Repository contract for settings/preferences.
 * Follows MVVM best practices.
 */
interface SettingsRepository {
    val settingsFlow: StateFlow<SettingsEntity>
    suspend fun updateShopName(shopName: String)
    suspend fun updateLogoUri(logoUri: String?)
    suspend fun updateThemeColor(themeColor: Long)
    suspend fun updateCurrency(currency: String)
    suspend fun updateTaxRate(taxRate: Double)
    suspend fun updateDarkMode(enabled: Boolean)
    suspend fun updateLowStockThreshold(threshold: Int)
    suspend fun updateAlertEnabled(enabled: Boolean)
    suspend fun exportSalesCsv()
    suspend fun resetToDefaults()
}
