package com.bejussi.tipcalculator.presentation.history.model


sealed interface HistoryTipEvent {
    data class SearchTip(val date: String): HistoryTipEvent
    object CancelSearchTip: HistoryTipEvent
}