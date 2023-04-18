package com.bejussi.tipcalculator.data.tip

import com.bejussi.tipcalculator.data.tip.room.TipDao
import com.bejussi.tipcalculator.data.tip.room.TipDataToDomain
import com.bejussi.tipcalculator.data.tip.room.TipDomainToData
import com.bejussi.tipcalculator.domain.tip.TipRepository
import com.bejussi.tipcalculator.domain.tip.model.Tip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import java.sql.Date
import javax.inject.Inject

class TipRepositoryImpl @Inject constructor(
    val tipDao: TipDao,
    val mapperToData: TipDomainToData,
    val mapperToDomain: TipDataToDomain
): TipRepository {

    override suspend fun insertTip(tip: Tip) {
        val tipData = mapperToData.map(tip)
        tipDao.insertTip(tip = tipData)
    }

    override fun getAllTips(): Flow<List<Tip>> {
        val tips = tipDao.getAllTips().map { list ->
            list.map {
                mapperToDomain.map(it)
            }
        }
        return tips
    }

    override suspend fun deleteTip(tip: Tip) {
        val tipData = mapperToData.map(tip)
        tipDao.deleteTip(tip = tipData)
    }
}