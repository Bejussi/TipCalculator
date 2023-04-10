package com.bejussi.tipcalculator.data.tip.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.bejussi.tipcalculator.data.tip.room.model.TipData

@Dao
interface TipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: TipData)
}