package com.example.zeeshanassignmentpaypay.adapter

import android.util.Log
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
            //Log.e("DataViewHolder_onBind", currency.name + " _zzz")
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
        Log.e("updateData", "complete list changed")

        temporaryCurrencyList = list.map(Currency::clone) as MutableList<Currency>;
        currencyList = mutableListOf()
        currencyList = list.map(Currency::clone) as MutableList<Currency>;
    }

    fun updateCurrenyRates(
        queryValue: Double,
        selectedCuurencyName: String,
        currencyList1: MutableList<Currency>
    ) {
        currencyList = currencyList1.map(Currency::clone).toList() as MutableList<Currency>;
        Log.e(
            "updateCurrenyRates",
            "selectedQuery:" + queryValue + " currNamwe:" + selectedCuurencyName
        )
        for (i in currencyList.indices) {
            var currencyItem = currencyList[i]
            if (!currencyItem.name!!.contains(selectedCuurencyName)) {
                currencyItem.price = (currencyItem.price!! * queryValue)
                notifyItemChanged(i)
            }

        }
    }
}