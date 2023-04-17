package com.bejussi.tipcalculator.presentation.calculate

import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.bejussi.tipcalculator.R
import com.bejussi.tipcalculator.core.StringResourcesProvider
import com.bejussi.tipcalculator.domain.tip.TipRepository
import com.bejussi.tipcalculator.presentation.calculate.model.RoundingType
import com.bejussi.tipcalculator.presentation.calculate.model.TipEvent
import com.bejussi.tipcalculator.presentation.calculate.model.TipPercent
import com.bejussi.tipcalculator.presentation.calculate.model.TipState
import com.google.common.truth.Truth.assertThat
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
class CalculateTipViewModelTest {

    @Mock
    private val repository = Mockito.mock(TipRepository::class.java)
    private val stringResourcesProvider = StringResourcesProvider(ApplicationProvider.getApplicationContext())
    private lateinit var viewModel: CalculateTipViewModel

    @Before
    fun setup() {
        viewModel = CalculateTipViewModel(
            repository = repository,
            stringResourcesProvider = stringResourcesProvider
        )
    }

    @Test
    fun saveTip_emptyBase() = runTest {
        viewModel.onEvent(TipEvent.SetBase(EMPTY_VALUE))
        val job = launch {
            viewModel.event.test {
                val emission = awaitItem()
                assertThat(emission).isEqualTo(stringResourcesProvider.getString(R.string.empty_message))
                cancelAndConsumeRemainingEvents()
            }
        }
        viewModel.onEvent(TipEvent.SaveTip)
        job.cancelAndJoin()
    }

    @Test
    fun saveTip_insertTip() = runTest {
        val job = launch {
            viewModel.onEvent(TipEvent.SetBase(256.0))
            viewModel.onEvent(TipEvent.SetRounding(RoundingType.NOTHING))
            viewModel.onEvent(TipEvent.SetSplit(2))
            viewModel.onEvent(TipEvent.SetTipPercent(TipPercent.FIFTEEN))
        }
        job.cancelAndJoin()
        assertThat(viewModel.onEvent(TipEvent.SaveTip)).isNotEqualTo(false)
    }

    @Test
    fun saveTip_setEvent() = runTest {
        viewModel.onEvent(TipEvent.SetBase(256.0))
        val job = launch {
            viewModel.event.test {
                val emission = awaitItem()
                assertThat(emission).isEqualTo(stringResourcesProvider.getString(R.string.success_save_message))
                cancelAndConsumeRemainingEvents()
            }
        }
        viewModel.onEvent(TipEvent.SaveTip)
        job.cancelAndJoin()
    }

    @Test
    fun setBase_updateState() = runTest {
        val job = launch {
            viewModel.state.test {
                val emission = awaitItem()
                assertThat(emission.base).isEqualTo(256.0)
                cancelAndConsumeRemainingEvents()
            }
        }
        viewModel.onEvent(TipEvent.SetBase(256.0))
        job.cancelAndJoin()
    }

    @Test
    fun setRounding_updateState() = runTest {
        val job = launch {
            viewModel.state.test {
                val emission = awaitItem()
                assertThat(emission.rounding).isEqualTo(RoundingType.NOTHING)
                cancelAndConsumeRemainingEvents()
            }
        }
        viewModel.onEvent(TipEvent.SetRounding(RoundingType.NOTHING))
        job.cancelAndJoin()
    }

    @Test
    fun setSplit_updateState() = runTest {
        val job = launch {
            viewModel.state.test {
                val emission = awaitItem()
                assertThat(emission.person).isEqualTo(2)
                cancelAndConsumeRemainingEvents()
            }
        }
        viewModel.onEvent(TipEvent.SetSplit(2))
        job.cancelAndJoin()
    }

    @Test
    fun setTipPercent_updateState() = runTest {
        val job = launch {
            viewModel.state.test {
                val emission = awaitItem()
                assertThat(emission.percent).isEqualTo(TipPercent.FIFTEEN)
                cancelAndConsumeRemainingEvents()
            }
        }
        viewModel.onEvent(TipEvent.SetTipPercent(TipPercent.FIFTEEN))
        job.cancelAndJoin()
    }

    @Test
    fun calculateTip() = runTest {
        val job = launch {
            val tip = viewModel.calculateTip(
                base = 124.0,
                percentage = TipPercent.TWENTY,
                split = 3,
                roundUpTip = RoundingType.UP
            )
            val tipState = TipState(
                base = 124.0,
                tip = 24.8,
                person = 3,
                perPerson = 49.67,
                total = 149.0,
                rounding = RoundingType.UP,
                percent = TipPercent.TWENTY
            )
            assertThat(tip).isEqualTo(tipState)
        }
        job.cancelAndJoin()
    }
}