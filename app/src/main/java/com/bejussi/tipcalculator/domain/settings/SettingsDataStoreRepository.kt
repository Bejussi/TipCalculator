package com.bejussi.tipcalculator.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsDataStoreRepository {

    suspend fun setTheme(isDarkMode: Boolean)

    fun getTheme(): Flow<Boolean>
}