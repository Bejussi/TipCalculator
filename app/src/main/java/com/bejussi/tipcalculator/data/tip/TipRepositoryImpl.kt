package com.bejussi.tipcalculator.data.tip

import com.bejussi.tipcalculator.data.tip.room.TipDao
import com.bejussi.tipcalculator.data.tip.room.TipDomainToData
import com.bejussi.tipcalculator.domain.tip.TipRepository
import com.bejussi.tipcalculator.domain.tip.model.Tip
import javax.inject.Inject

class TipRepositoryImpl @Inject constructor(
    val tipDao: TipDao,
    val mapper: TipDomainToData
): TipRepository {

    override suspend fun insertTip(tip: Tip) {
        val tipData = mapper.map(tip)
        tipDao.insertTip(tip = tipData)
    }
}