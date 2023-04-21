package com.bejussi.tipcalculator.presentation.history

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bejussi.tipcalculator.R
import com.bejussi.tipcalculator.core.DimensResourcesProvider
import com.bejussi.tipcalculator.core.makeToast
import com.bejussi.tipcalculator.databinding.FragmentHistoryBinding
import com.bejussi.tipcalculator.domain.tip.model.Tip
import com.bejussi.tipcalculator.presentation.core.UIEvent
import com.bejussi.tipcalculator.presentation.history.model.HistoryTipEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    @Inject
    lateinit var dimensResourcesProvider: DimensResourcesProvider

    private val historyViewModel: HistoryViewModel by viewModels()

    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lifecycleScope.launch {
            historyViewModel.state.collect {
                adapter.submitList(it.tipList) {
                    binding.tipsRecyclerView.post {
                        binding.tipsRecyclerView.scrollToPosition(0)
                    }
                }
                binding.cancelButton.isVisible = it.cancelVisibility
            }
        }

        lifecycleScope.launch {
            historyViewModel.event.collectLatest {
                when (it) {
                    is UIEvent.ShowToast -> requireContext().makeToast(it.message)
                }
            }
        }

        binding.calendarButton.setOnClickListener {
            showDatePicker()
        }

        binding.cancelButton.setOnClickListener {
            historyViewModel.onEvent(HistoryTipEvent.CancelSearchTip)
        }

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { view, mYear, mMonth, mDay ->
            calendar.set(mYear, mMonth, mDay)
            val sdf = SimpleDateFormat("dd MMM, yyyy")
            val date = sdf.format(calendar.time)

            historyViewModel.onEvent(HistoryTipEvent.SearchTip(date))
        }, year, month, day)

        datePicker.show()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter(object : TipActionListener {
            override fun shareTip(tip: Tip) {
                sendIntent(tip)
            }
        })
        binding.tipsRecyclerView.adapter = adapter
        binding.tipsRecyclerView.addItemDecoration(
            HistoryItemDecoration(
                dimensResourcesProvider.getDimens(
                    R.dimen.recycler_item_margin_top
                )
            )
        )
    }

    fun sendIntent(tip: Tip) {
        val text = getString(
            R.string.send_data,
            tip.date,
            tip.base,
            tip.tip,
            tip.person,
            tip.perPerson,
            tip.total
        )
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    companion object {
        const val REQUEST_KEY = "REQUEST_KEY"
    }
}