package com.bejussi.tipcalculator.data.tip.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tip")
data class TipData(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "base", typeAffinity = ColumnInfo.REAL)
    val base: Double,
    @ColumnInfo(name = "tip", typeAffinity = ColumnInfo.REAL)
    val tip: Double,
    @ColumnInfo(name = "person")
    val person: Int,
    @ColumnInfo(name = "perPerson", typeAffinity = ColumnInfo.REAL)
    val perPerson: Double,
    @ColumnInfo(name = "total", typeAffinity = ColumnInfo.REAL)
    val total: Double,
    @ColumnInfo(name = "date")
    val date: Date
)
