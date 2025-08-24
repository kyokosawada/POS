package com.kyokosawada.data.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.settingsDataStore by preferencesDataStore(name = "settings_prefs")

// Prefs keys for each setting
private val SHOP_NAME_KEY = stringPreferencesKey("shop_name")
private val LOGO_URI_KEY = stringPreferencesKey("logo_uri")
private val THEME_COLOR_KEY = stringPreferencesKey("theme_color")
private val CURRENCY_KEY = stringPreferencesKey("currency")
private val TAX_RATE_KEY = doublePreferencesKey("tax_rate")
private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
private val LOW_STOCK_THRESHOLD_KEY = intPreferencesKey("low_stock_threshold")
private val ALERT_ENABLED_KEY = booleanPreferencesKey("alert_enabled")

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {
    private val _settingsFlow = MutableStateFlow(SettingsEntity())
    override val settingsFlow: StateFlow<SettingsEntity> = _settingsFlow.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            context.settingsDataStore.data
                .map { prefs ->
                    SettingsEntity(
                        shopName = prefs[SHOP_NAME_KEY] ?: "QuickPOS Shop",
                        logoUri = prefs[LOGO_URI_KEY],
                        themeColor = prefs[THEME_COLOR_KEY]?.toLongOrNull() ?: 0xFF2196F3,
                        currency = prefs[CURRENCY_KEY] ?: "PHP",
                        taxRate = prefs[TAX_RATE_KEY] ?: 0.0,
                        darkMode = prefs[DARK_MODE_KEY] ?: false,
                        lowStockThreshold = prefs[LOW_STOCK_THRESHOLD_KEY] ?: 5,
                        alertEnabled = prefs[ALERT_ENABLED_KEY] ?: true
                    )
                }
                .collect { _settingsFlow.value = it }
        }
    }

    override suspend fun updateShopName(shopName: String) {
        context.settingsDataStore.edit { prefs -> prefs[SHOP_NAME_KEY] = shopName }
    }
    override suspend fun updateLogoUri(logoUri: String?) {
        context.settingsDataStore.edit { prefs ->
            if (logoUri == null) prefs.remove(LOGO_URI_KEY) else prefs[LOGO_URI_KEY] = logoUri
        }
    }
    override suspend fun updateThemeColor(themeColor: Long) {
        context.settingsDataStore.edit { prefs -> prefs[THEME_COLOR_KEY] = themeColor.toString() }
    }
    override suspend fun updateCurrency(currency: String) {
        context.settingsDataStore.edit { prefs -> prefs[CURRENCY_KEY] = currency }
    }
    override suspend fun updateTaxRate(taxRate: Double) {
        context.settingsDataStore.edit { prefs -> prefs[TAX_RATE_KEY] = taxRate }
    }
    override suspend fun updateDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { prefs -> prefs[DARK_MODE_KEY] = enabled }
    }
    override suspend fun updateLowStockThreshold(threshold: Int) {
        context.settingsDataStore.edit { prefs -> prefs[LOW_STOCK_THRESHOLD_KEY] = threshold }
    }
    override suspend fun updateAlertEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { prefs -> prefs[ALERT_ENABLED_KEY] = enabled }
    }
    override suspend fun exportSalesCsv() { /* Unchanged - still TODO */
    }
    override suspend fun resetToDefaults() {
        context.settingsDataStore.edit { it.clear() }
        _settingsFlow.value = SettingsEntity()
    }
}
