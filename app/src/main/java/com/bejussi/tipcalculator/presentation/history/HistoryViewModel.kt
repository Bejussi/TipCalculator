package com.bejussi.tipcalculator.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bejussi.tipcalculator.R
import com.bejussi.tipcalculator.core.StringResourcesProvider
import com.bejussi.tipcalculator.domain.tip.TipRepository
import com.bejussi.tipcalculator.presentation.core.UIEvent
import com.bejussi.tipcalculator.presentation.history.model.HistoryTipEvent
import com.bejussi.tipcalculator.presentation.history.model.HistoryTipState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: TipRepository,
    private val stringResourcesProvider: StringResourcesProvider
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryTipState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),HistoryTipState())

    private val _event = MutableSharedFlow<UIEvent>()
    val event = _event.asSharedFlow()

    init {
        onEvent(HistoryTipEvent.CancelSearchTip)
    }

    fun onEvent(event: HistoryTipEvent) {
        when (event) {
            is HistoryTipEvent.SearchTip -> {
//
            }
            is HistoryTipEvent.DeleteTip -> {
                viewModelScope.launch {
                    repository.deleteTip(event.tip)
                    _event.emit(
                        UIEvent.ShowToast(
                            message = stringResourcesProvider.getString(R.string.success_delete_message)
                        )
                    )
                }
            }
            is HistoryTipEvent.CancelSearchTip -> {
                viewModelScope.launch {
                    repository.getAllTips().collect {
                        _state.emit(
                            HistoryTipState(
                                tipList = it
                            )
                        )
                    }
                }

            }
        }
    }
}