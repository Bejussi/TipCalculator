package com.bejussi.tipcalculator.data.tip.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bejussi.tipcalculator.data.tip.room.model.TipData
import kotlinx.coroutines.flow.Flow

@Dao
interface TipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: TipData)

    @Query("SELECT * FROM tip")
    fun getAllTips(): Flow<List<TipData>>
}