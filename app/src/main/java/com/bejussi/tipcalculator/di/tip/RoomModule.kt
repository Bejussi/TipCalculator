package com.bejussi.tipcalculator.di.tip

import android.content.Context
import androidx.room.Room
import com.bejussi.tipcalculator.data.tip.TipRepositoryImpl
import com.bejussi.tipcalculator.data.tip.room.TipDao
import com.bejussi.tipcalculator.data.tip.room.TipDataToDomain
import com.bejussi.tipcalculator.data.tip.room.TipDatabase
import com.bejussi.tipcalculator.data.tip.room.TipDomainToData
import com.bejussi.tipcalculator.domain.tip.TipRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideTipDatabase(
        @ApplicationContext
        applicationContext: Context
    ) = Room.databaseBuilder(
        applicationContext,
        TipDatabase::class.java,
        TipDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideTipDao(tipDatabase: TipDatabase) = tipDatabase.tipDao()

    @Provides
    @Singleton
    fun provideTipRepository(
        tipDao: TipDao,
        tipDataToDomain: TipDataToDomain,
        tipDomainToData: TipDomainToData
    ): TipRepository = TipRepositoryImpl(
        tipDao = tipDao,
        mapperToData = tipDomainToData,
        mapperToDomain = tipDataToDomain
    )
}