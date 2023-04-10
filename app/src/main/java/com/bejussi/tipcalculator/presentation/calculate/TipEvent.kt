package com.bejussi.tipcalculator.presentation.calculate

sealed interface TipEvent {
    object SaveTip: TipEvent
    data class SetBase(val base: Double): TipEvent
    data class SetTipPercent(val tipPercent: TipPercent): TipEvent
    data class SetSplit(val person: Int): TipEvent
    data class SetRounding(val rounding: RoundingType): TipEvent
}