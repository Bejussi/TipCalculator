package com.bejussi.tipcalculator.presentation.calculate

data class TipState(
    val base: Double = 0.0,
    val tip: Double = 0.0,
    val person: Int = 1,
    val perPerson: Double = 0.0,
    val total: Double = 0.0,
    val rounding: RoundingType = RoundingType.UP,
    val percent: TipPercent = TipPercent.TEN
)
