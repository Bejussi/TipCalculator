package com.bejussi.tipcalculator.presentation.history.model

import com.bejussi.tipcalculator.domain.tip.model.Tip

data class HistoryTipState(
    val tipList: List<Tip> = emptyList(),
    val cancelVisibility: Boolean = false
)