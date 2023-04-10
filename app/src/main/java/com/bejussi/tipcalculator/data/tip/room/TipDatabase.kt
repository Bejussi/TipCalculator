package com.bejussi.tipcalculator.data.tip.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bejussi.tipcalculator.data.tip.room.model.TipData

@Database(
    entities = [TipData::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class TipDatabase: RoomDatabase() {
    abstract fun tipDao(): TipDao

    companion object {
        const val DATABASE_NAME = "tip_database"
    }
}