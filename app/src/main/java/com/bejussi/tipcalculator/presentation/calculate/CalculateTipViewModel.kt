package com.bejussi.tipcalculator.presentation.calculate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bejussi.tipcalculator.R
import com.bejussi.tipcalculator.core.StringResourcesProvider
import com.bejussi.tipcalculator.domain.tip.TipRepository
import com.bejussi.tipcalculator.domain.tip.model.Tip
import com.bejussi.tipcalculator.presentation.calculate.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalculateTipViewModel @Inject constructor(
    private val repository: TipRepository,
    private val stringResourcesProvider: StringResourcesProvider
) : ViewModel() {

    private val _state = MutableStateFlow(TipState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TipState())

    private val _event = MutableSharedFlow<UIEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: TipEvent) {
        when (event) {
            is TipEvent.SaveTip -> {

                if (_state.value.base == EMPTY_VALUE) {
                    viewModelScope.launch {
                        _event.emit(
                            UIEvent.ShowToast(
                                message = stringResourcesProvider.getString(R.string.empty_message)
                            )
                        )
                    }
                    return
                } else {
                    val tip = Tip(
                        base = state.value.base,
                        tip = state.value.tip,
                        person = state.value.person,
                        perPerson = state.value.perPerson,
                        total = state.value.total,
                        date = Calendar.getInstance().time
                    )

                    viewModelScope.launch {
                        repository.insertTip(tip = tip)
                        _event.emit(
                            UIEvent.ShowToast(
                                message = stringResourcesProvider.getString(R.string.success_save_message)
                            )
                        )
                    }
                }
            }

            is TipEvent.SetBase -> {
                _state.update {
                    calculateTip(
                        base = event.base
                    )
                }
            }

            is TipEvent.SetRounding -> {
                _state.update {
                    calculateTip(
                        roundUpTip = event.rounding
                    )
                }
            }

            is TipEvent.SetSplit -> {
                _state.update {
                    calculateTip(
                        split = event.person
                    )
                }
            }

            is TipEvent.SetTipPercent -> {
                _state.update {
                    calculateTip(
                        percentage = event.tipPercent
                    )
                }
            }
        }
    }

    private fun calculateTip(
        base: Double = _state.value.base,
        percentage: TipPercent = _state.value.percent,
        split: Int = _state.value.person,
        roundUpTip: RoundingType = _state.value.rounding
    ): TipState {

        val percent = when (percentage) {
            TipPercent.TEN -> TipPercent.TEN.persent
            TipPercent.FIFTEEN -> TipPercent.FIFTEEN.persent
            TipPercent.TWENTY -> TipPercent.TWENTY.persent
            TipPercent.TWENTYFIVE -> TipPercent.TWENTYFIVE.persent
        }

        val tip = roundDouble(percent.times(base))

        var total = roundDouble(base.plus(tip))

        total = when (roundUpTip) {
            RoundingType.NOTHING -> total
            RoundingType.UP -> kotlin.math.ceil(total)
            RoundingType.DOWN -> kotlin.math.floor(total)
        }

        val perPerson = roundDouble(total.div(split))

        val tipState = TipState(
            base = base,
            tip = tip,
            person = split,
            perPerson = perPerson,
            total = total,
            rounding = roundUpTip,
            percent = percentage
        )

        return tipState
    }

    private fun roundDouble(value: Double): Double = Math.round(value * 100.0) / 100.00
}