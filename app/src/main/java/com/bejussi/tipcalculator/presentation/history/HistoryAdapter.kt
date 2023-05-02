package com.bejussi.tipcalculator.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bejussi.tipcalculator.databinding.TipItemBinding
import com.bejussi.tipcalculator.domain.tip.model.Tip
import com.bejussi.tipcalculator.presentation.settings.billing.BillingAdapter

class HistoryAdapter(
    private val tipActionListener: TipActionListener
) : ListAdapter<Tip, HistoryAdapter.TipViewHolder>(DiffCallback()) {

    class TipViewHolder(
        val binding: TipItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tip: Tip) {
            binding.apply {
                dateText.text = tip.date.toString()
                baseResultText.text = tip.base.toString()
                tipResultText.text = tip.tip.toString()
                personResultText.text = tip.person.toString()
                perPersonResultText.text = tip.perPerson.toString()
                totalResultText.text = tip.total.toString()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        return TipViewHolder(
            TipItemBinding.inflate(
                LayoutInflater.from(parent.context),parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        val currentTip = getItem(position)
        holder.bind(currentTip)
        holder.binding.shareButton.setOnClickListener {
            tipActionListener.shareTip(currentTip)
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Tip>() {
        override fun areItemsTheSame(oldItem: Tip, newItem: Tip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tip, newItem: Tip): Boolean {
            return oldItem == newItem
        }

    }
}