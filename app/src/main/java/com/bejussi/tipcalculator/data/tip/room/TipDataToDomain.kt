package com.bejussi.tipcalculator.data.tip.room

import com.bejussi.tipcalculator.core.Mapper
import com.bejussi.tipcalculator.data.tip.room.model.TipData
import com.bejussi.tipcalculator.domain.tip.model.Tip
import javax.inject.Inject

class TipDataToDomain @Inject constructor(): Mapper<TipData, Tip> {

    override suspend fun map(from: TipData) = Tip(
        base = from.base,
        tip = from.tip,
        person = from.person,
        perPerson = from.perPerson,
        total = from.total,
        date = from.date
    )
}