package com.bejussi.tipcalculator.data.tip.room

import androidx.room.*
import com.bejussi.tipcalculator.data.tip.room.model.TipData
import kotlinx.coroutines.flow.Flow

@Dao
interface TipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: TipData)

    @Query("SELECT * FROM tip ORDER BY id DESC")
    fun getAllTips(): Flow<List<TipData>>

    @Query("SELECT * FROM tip WHERE date = :date ORDER BY id DESC")
    fun getTipsByDate(date: String): Flow<List<TipData>>

}