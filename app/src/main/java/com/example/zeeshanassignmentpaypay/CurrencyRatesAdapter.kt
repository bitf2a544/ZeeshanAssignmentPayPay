package com.example.zeeshanassignmentpaypay

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zeeshanassignmentpaypay.data.model.Currency

import com.example.zeeshanassignmentpaypay.databinding.CurrencyItemLayoutBinding


class CurrencyRatesAdapter(private val currencyList: ArrayList<Currency>) :
    RecyclerView.Adapter<CurrencyRatesAdapter.DataViewHolder>() {

    lateinit var binding: CurrencyItemLayoutBinding

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(currency: Currency) {
            //Log.e("DataViewHolder_onBind", currency.name + " _zzz")
            binding.currencyNameTextView.text = currency.name
            binding.currencyPriceTextView.text = currency.price.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = CurrencyItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return DataViewHolder(binding.root);
    }

    override fun getItemCount(): Int = currencyList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(currencyList[position])

    fun addData(list: List<Currency>) {
        currencyList.addAll(list)
    }
}