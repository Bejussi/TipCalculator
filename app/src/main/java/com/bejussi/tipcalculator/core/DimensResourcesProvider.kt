package com.bejussi.tipcalculator.core

import android.content.Context
import androidx.annotation.DimenRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DimensResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getDimens(@DimenRes dimensResId: Int): Int {
        return context.resources.getDimensionPixelSize(dimensResId)
    }
}