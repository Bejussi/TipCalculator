package com.bejussi.tipcalculator.presentation.history.model

import com.bejussi.tipcalculator.domain.tip.model.Tip
import java.util.*

sealed interface HistoryTipEvent {
    data class SearchTip(val data: Date): HistoryTipEvent
    object CancelSearchTip: HistoryTipEvent
    data class DeleteTip(val tip: Tip): HistoryTipEvent
}