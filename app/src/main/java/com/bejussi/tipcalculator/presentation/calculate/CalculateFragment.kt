package com.bejussi.tipcalculator.presentation.calculate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bejussi.tipcalculator.R
import com.bejussi.tipcalculator.core.makeToast
import com.bejussi.tipcalculator.databinding.FragmentCalculateBinding
import com.bejussi.tipcalculator.presentation.calculate.model.RoundingType
import com.bejussi.tipcalculator.presentation.calculate.model.TipEvent
import com.bejussi.tipcalculator.presentation.calculate.model.TipPercent
import com.bejussi.tipcalculator.presentation.core.UIEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


const val EMPTY_VALUE = 0.0

@AndroidEntryPoint
class CalculateFragment : Fragment() {

    lateinit var binding: FragmentCalculateBinding

    private val calculateTipViewModel: CalculateTipViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCalculateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            calculateTipViewModel.state.collect {
                binding.apply {
                    baseResultText.text = it.base.toString()
                    tipResultText.text = it.tip.toString()
                    perPersonResultText.text = it.perPerson.toString()
                    totalResultText.text = it.total.toString()
                    splitByTitleText.text = getString(R.string.split_by, it.person)
                }
            }
        }

        lifecycleScope.launch {
            calculateTipViewModel.event.collectLatest {
                when (it) {
                    is UIEvent.ShowToast -> requireContext().makeToast(it.message)
                }
            }
        }

        binding.billEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isNotEmpty()) {
                    calculateTipViewModel.onEvent(TipEvent.SetBase(p0.toString().toDouble()))
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.isEmpty()) {
                    calculateTipViewModel.onEvent(TipEvent.SetBase(EMPTY_VALUE))
                }
            }
        })

        binding.tenChip.setOnClickListener {
            calculateTipViewModel.onEvent(TipEvent.SetTipPercent(TipPercent.TEN))
        }

        binding.fifteenChip.setOnClickListener {
            calculateTipViewModel.onEvent(TipEvent.SetTipPercent(TipPercent.FIFTEEN))
        }

        binding.twentyChip.setOnClickListener {
            calculateTipViewModel.onEvent(TipEvent.SetTipPercent(TipPercent.TWENTY))
        }

        binding.twentyfiveChip.setOnClickListener {
            calculateTipViewModel.onEvent(TipEvent.SetTipPercent(TipPercent.TWENTYFIVE))
        }


        binding.splitSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                calculateTipViewModel.onEvent(TipEvent.SetSplit(p0!!.progress))
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.nothingRoundingChip.setOnClickListener {
            calculateTipViewModel.onEvent(TipEvent.SetRounding(RoundingType.NOTHING))
        }

        binding.upRoundingChip.setOnClickListener {
            calculateTipViewModel.onEvent(TipEvent.SetRounding(RoundingType.UP))
        }

        binding.downRoundingChip.setOnClickListener {
            calculateTipViewModel.onEvent(TipEvent.SetRounding(RoundingType.DOWN))
        }

        binding.saveButton.setOnClickListener {
            calculateTipViewModel.onEvent(TipEvent.SaveTip)
        }
    }
}