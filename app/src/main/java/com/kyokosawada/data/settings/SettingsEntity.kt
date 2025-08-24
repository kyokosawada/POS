package com.kyokosawada.data.settings

/**
 * SettingsEntity holds all user/shop-configurable preferences.
 * Persisted via DataStore (or Room if expanded).
 */
data class SettingsEntity(
    val shopName: String = "QuickPOS Shop",
    val logoUri: String? = null,
    val themeColor: Long = 0xFF2196F3, // Default Material Blue
    val currency: String = "PHP",
    val taxRate: Double = 0.0,
    val darkMode: Boolean = false,
    val lowStockThreshold: Int = 5,
    val alertEnabled: Boolean = true
)
