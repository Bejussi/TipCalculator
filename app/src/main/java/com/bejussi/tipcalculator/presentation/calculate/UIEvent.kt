package com.bejussi.tipcalculator.presentation.calculate

sealed class UIEvent {
    data class ShowToast(val message: String): UIEvent()
}