package com.bejussi.tipcalculator.domain.tip.model

import java.util.*


data class Tip(
    val id: Int? = null,
    val base: Double,
    val tip: Double,
    val person: Int,
    val perPerson: Double,
    val total: Double,
    val date: String
)
