package com.gerralizza.networking.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gerralizza.networking.R

class ExchangeRatesAdapter : RecyclerView.Adapter<ExchangeRatesAdapter.RateViewHolder>() {
    val rates: ArrayList<ExchangeRate> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RateViewHolder(parent)
    override fun getItemCount() = rates.size
    override fun onBindViewHolder(holder: RateViewHolder, position: Int) = holder.onBind(rates[position])

    fun setRates(rates: List<ExchangeRate>) {
        this.rates.clear()
        this.rates.addAll(rates)
        notifyDataSetChanged()
    }

    class RateViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.rate_item, parent, false)
    ) {
        private val nameView = itemView.findViewById<TextView>(R.id.name)
        private val valueView = itemView.findViewById<TextView>(R.id.value)

        fun onBind(rate: ExchangeRate) {
            nameView.text = rate.name
            valueView.text = rate.value.toString()
        }
    }
}