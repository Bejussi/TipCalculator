package com.bejussi.tipcalculator.data.tip.room

import androidx.room.*
import com.bejussi.tipcalculator.data.tip.room.model.TipData
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: TipData)

    @Query("SELECT * FROM tip")
    fun getAllTips(): Flow<List<TipData>>

    @Query("SELECT * FROM tip WHERE date = :date")
    fun getTipsByDate(date: Date): Flow<List<TipData>>

    @Delete
    suspend fun deleteTip(tip: TipData)
}