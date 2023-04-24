package com.bejussi.tipcalculator.presentation.history

import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.bejussi.tipcalculator.R
import com.bejussi.tipcalculator.core.StringResourcesProvider
import com.bejussi.tipcalculator.domain.tip.TipRepository
import com.bejussi.tipcalculator.domain.tip.model.Tip
import com.bejussi.tipcalculator.presentation.history.model.HistoryTipEvent
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HistoryViewModelTest {

    @Mock
    private val repository = Mockito.mock(TipRepository::class.java)
    private val stringResourcesProvider = StringResourcesProvider(ApplicationProvider.getApplicationContext())
    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setup() {
        viewModel = HistoryViewModel(
            repository = repository,
            stringResourcesProvider = stringResourcesProvider
        )
    }

    @Test
    fun searchTip_emptyResult() = runTest {
        val job = launch {
            viewModel.event.test {
                val emission = awaitItem()
                Truth.assertThat(emission).isEqualTo(stringResourcesProvider.getString(R.string.empty_date_list))
                cancelAndConsumeRemainingEvents()
            }
        }
        viewModel.onEvent(HistoryTipEvent.SearchTip("22 апр., 2022"))
        job.cancelAndJoin()
    }

    @Test
    fun searchTip_updateState() = runTest {
        val tip = Tip(
            base = 258.0,
            tip = 38.7,
            person = 4,
            perPerson = 74.18,
            total = 296.7,
            date = "23 апр., 2023"
        )
        val insertJob = launch {
            repository.insertTip(tip)
        }
        insertJob.cancelAndJoin()

        val job = launch {
            viewModel.state.test {
                val emission = awaitItem()
                Truth.assertThat(emission.tipList.size).isEqualTo(1)
                cancelAndConsumeRemainingEvents()
            }
        }
        viewModel.onEvent(HistoryTipEvent.SearchTip("23 апр., 2023"))
        job.cancelAndJoin()
    }

    @Test
    fun cancelTip_returnTrue() = runTest {
        Truth.assertThat(viewModel.onEvent(HistoryTipEvent.CancelSearchTip))
            .isNotEqualTo(false)
    }
}