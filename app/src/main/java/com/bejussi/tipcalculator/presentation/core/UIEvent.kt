package com.bejussi.tipcalculator.presentation.core

sealed class UIEvent {
    data class ShowToast(val message: String): UIEvent()
}