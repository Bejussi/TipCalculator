package com.bejussi.tipcalculator.di.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import com.bejussi.tipcalculator.data.settings.SettingsDataStoreRepositoryImpl
import com.bejussi.tipcalculator.domain.settings.SettingsDataStoreRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SETTINGS")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> {
        return applicationContext.dataStore
    }

    @Provides
    @Singleton
    fun provideSettingsDataStoreRepository(
        dataStore: DataStore<Preferences>
    ): SettingsDataStoreRepository = SettingsDataStoreRepositoryImpl(
        dataStore = dataStore
    )
}