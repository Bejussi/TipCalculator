package com.bejussi.tipcalculator.presentation.history

import com.bejussi.tipcalculator.domain.tip.model.Tip

interface TipActionListener {

    fun shareTip(tip: Tip)
}