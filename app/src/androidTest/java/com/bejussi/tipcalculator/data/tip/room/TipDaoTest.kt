package com.bejussi.tipcalculator.data.tip.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.bejussi.tipcalculator.data.tip.room.model.TipData
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TipDaoTest {

    private lateinit var database: TipDatabase
    private lateinit var dao: TipDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TipDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.tipDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTip_returnsTrue() = runTest {
        val tip = TipData(
            base = 258.0,
            tip = 38.7,
            person = 4,
            perPerson = 74.18,
            total = 296.7,
            date = "22 апр., 2023"
        )
        dao.insertTip(tip)

        val job = async(Dispatchers.IO) {
            dao.getAllTips().collect {
                assertThat(it).contains(tip)
            }
        }
        job.cancelAndJoin()
    }

    @Test
    fun searchTip_returnTip() = runTest {
        val tip = TipData(
            base = 258.0,
            tip = 38.7,
            person = 4,
            perPerson = 74.18,
            total = 296.7,
            date = "22 апр., 2023"
        )
        dao.insertTip(tip)

        val job = async(Dispatchers.IO) {
            dao.getTipsByDate(tip.date).collect {
                assertThat(it).contains(tip)
            }
        }
        job.cancelAndJoin()
    }
}