package com.example.zeeshanassignmentpaypay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zeeshanassignmentpaypay.data.model.Currency
import com.example.zeeshanassignmentpaypay.databinding.CurrencyItemLayoutBinding

class CurrencyRatesAdapter(private var currencyList: MutableList<Currency>) :
    RecyclerView.Adapter<CurrencyRatesAdapter.DataViewHolder>() {
    lateinit var temporaryCurrencyList: MutableList<Currency>
    lateinit var binding: CurrencyItemLayoutBinding

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(currency: Currency) {

            binding.currencyNameTextView.text = currency.name
            binding.currencyPriceTextView.text = currency.price.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = CurrencyItemLayoutBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return DataViewHolder(binding.root);
    }

    override fun getItemCount(): Int = currencyList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(currencyList[position])

    fun updateData(list: MutableList<Currency>) {
        temporaryCurrencyList = list.map(Currency::clone) as MutableList<Currency>;
        currencyList = mutableListOf()
        currencyList = list.map(Currency::clone) as MutableList<Currency>;
    }

    fun updateCurrencyRates(
        queryValue: Double,
        selectedCurrencyName: String,
        currencyList1: MutableList<Currency>
    ) {
        currencyList = currencyList1.map(Currency::clone).toList() as MutableList<Currency>;
        for (i in currencyList.indices) {
            val currencyItem = currencyList[i]
            if (!currencyItem.name!!.contains(selectedCurrencyName)) {
                currencyItem.price = (currencyItem.price!! * queryValue)
                notifyItemChanged(i)
            }

        }
    }
}