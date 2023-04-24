package com.bejussi.tipcalculator.domain.tip

import com.bejussi.tipcalculator.domain.tip.model.Tip
import kotlinx.coroutines.flow.Flow


interface TipRepository {

    suspend fun insertTip(tip: Tip)

    fun getAllTips(): Flow<List<Tip>>

    fun getTipsByDate(date: String): Flow<List<Tip>>
}