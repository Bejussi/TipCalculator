package com.bejussi.tipcalculator.domain.tip

import com.bejussi.tipcalculator.domain.tip.model.Tip
import kotlinx.coroutines.flow.Flow
import java.util.*


interface TipRepository {

    suspend fun insertTip(tip: Tip)

    fun getAllTips(): Flow<List<Tip>>

    suspend fun deleteTip(tip: Tip)
}