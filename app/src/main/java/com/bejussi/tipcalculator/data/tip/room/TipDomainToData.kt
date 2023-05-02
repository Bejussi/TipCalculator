package com.bejussi.tipcalculator.data.tip.room

import com.bejussi.tipcalculator.core.Mapper
import com.bejussi.tipcalculator.data.tip.room.model.TipData
import com.bejussi.tipcalculator.domain.tip.model.Tip
import javax.inject.Inject

class TipDomainToData @Inject constructor(): Mapper<Tip, TipData> {

    override suspend fun map(from: Tip) = TipData(
        base = from.base,
        tip = from.tip,
        person = from.person,
        perPerson = from.perPerson,
        total = from.total,
        date = from.date
    )
}