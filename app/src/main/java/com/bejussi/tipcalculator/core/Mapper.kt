package com.bejussi.tipcalculator.core

interface Mapper<F, T> {

    suspend fun map(from: F): T
}