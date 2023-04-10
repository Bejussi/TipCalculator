package com.bejussi.tipcalculator.domain.tip

import com.bejussi.tipcalculator.domain.tip.model.Tip


interface TipRepository {

    suspend fun insertTip(tip: Tip)
}