package com.bejussi.tipcalculator.presentation.calculate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bejussi.tipcalculator.domain.tip.TipRepository
import com.bejussi.tipcalculator.domain.tip.model.Tip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalculateTipViewModel @Inject constructor(
    private val repository: TipRepository
): ViewModel() {

    private val _state = MutableStateFlow(TipState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TipState())

    fun onEvent(event: TipEvent) {
        when(event) {
            is TipEvent.SaveTip -> {
                val base = state.value.base

                if (base.isFinite()) {
                    return
                }

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
                }
            }

            is TipEvent.SetBase -> {
                _state.update {
                    calculateTip(
                        base = event.base,
                        percentage = it.percent,
                        split = it.person,
                        roundUpTip = it.rounding
                    )
                }
            }

            is TipEvent.SetRounding -> {
                _state.update {
                    calculateTip(
                        base = it.base,
                        percentage = it.percent,
                        split = it.person,
                        roundUpTip = event.rounding
                    )
                }
            }

            is TipEvent.SetSplit -> {
                _state.update {
                    calculateTip(
                        base = it.base,
                        percentage = it.percent,
                        split = event.person,
                        roundUpTip = it.rounding
                    )
                }
            }

            is TipEvent.SetTipPercent -> {
                _state.update {
                    calculateTip(
                        base = it.base,
                        percentage = event.tipPercent,
                        split = it.person,
                        roundUpTip = it.rounding
                    )
                }
            }
        }
    }

    private fun calculateTip(
        base: Double,
        percentage: TipPercent,
        split: Int,
        roundUpTip: RoundingType
    ): TipState {

        val percent = when(percentage) {
            TipPercent.TEN -> TipPercent.TEN.persent
            TipPercent.FIFTEEN -> TipPercent.FIFTEEN.persent
            TipPercent.TWENTY -> TipPercent.TWENTY.persent
            TipPercent.TWENTYFIVE -> TipPercent.TWENTYFIVE.persent
        }

        var tip = percent.times(base)

        tip = when (roundUpTip) {
            RoundingType.UP -> kotlin.math.ceil(tip)
            RoundingType.DOWN -> kotlin.math.floor(tip)
        }

        val total = base.plus(tip)
        val perPerson = total.div(split)

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
}