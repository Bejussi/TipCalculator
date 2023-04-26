package com.bejussi.tipcalculator.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bejussi.tipcalculator.domain.settings.SettingsDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
): ViewModel() {

    val getTheme = settingsDataStoreRepository.getTheme().asLiveData(Dispatchers.IO)
    val getLanguage = settingsDataStoreRepository.getLanguage().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsDataStoreRepository.setTheme(isDarkMode)
        }
    }

    fun setLanguage(language : String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsDataStoreRepository.setLanguage(language)
        }
    }
}