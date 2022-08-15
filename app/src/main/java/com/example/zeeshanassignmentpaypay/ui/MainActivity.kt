package com.example.zeeshanassignmentpaypay.ui

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zeeshanassignmentpaypay.adapter.CurrencyRatesAdapter
import com.example.zeeshanassignmentpaypay.data.model.Currency
import com.example.zeeshanassignmentpaypay.databinding.ActivityMainBinding
import com.example.zeeshanassignmentpaypay.utils.Status
import com.example.zeeshanassignmentpaypay.viewmodel.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var adapter: CurrencyRatesAdapter
    private lateinit var binding: ActivityMainBinding
    var completeCurrencyList = mutableListOf<Currency>()
    var currencyNamesList = mutableListOf<String>()
    var selectedCurrency = Currency("", 0.0, 0)

    private lateinit var spinnerArrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding.currencyRecyclerView.layoutManager = GridLayoutManager(this, 4)
        adapter = CurrencyRatesAdapter(arrayListOf())
        binding.currencyRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.currencyRecyclerView.context,
                (binding.currencyRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.currencyRecyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty() && selectedCurrency.name != null) {
                    adapter.updateCurrencyRates(
                        query.toDouble(),
                        selectedCurrency.name.toString(),
                        completeCurrencyList
                    )
                } else {
                    adapter.updateCurrencyRates(
                        1.0,
                        selectedCurrency.name.toString(),
                        completeCurrencyList
                    )
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty() && selectedCurrency.name != null) {
                    adapter.updateCurrencyRates(
                        newText.toDouble(),
                        selectedCurrency.name.toString(),
                        completeCurrencyList
                    )
                } else {
                    adapter.updateCurrencyRates(
                        1.0,
                        selectedCurrency.name.toString(),
                        completeCurrencyList
                    )
                }
                return false
            }
        })

        binding.spinner.onItemSelectedListener = this

        //Creating the ArrayAdapter instance having the country list
        spinnerArrayAdapter = ArrayAdapter<String>(
            this, R.layout.simple_spinner_item,
            currencyNamesList
        )
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        binding.spinner.adapter = spinnerArrayAdapter
    }

    private fun setupObserver() {

        mainViewModel.exchangeRates.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    CoroutineScope(Dispatchers.Main).launch() {
                        binding.progressBar.visibility = View.GONE
                        binding.currencyRecyclerView.visibility = View.VISIBLE

                        val currencyList = mutableListOf<Currency>()
                        currencyNamesList.clear()
                        val exchangeRates = it.data
                        val obj = Gson().toJsonTree(exchangeRates?.rates).getAsJsonObject()
                        for ((key, value) in obj.entrySet()) {
                            currencyList.add(Currency(key, value.asDouble))
                            currencyNamesList.add(key)
                            println("Key = $key Value = $value")
                        }

                        binding.spinner.adapter = spinnerArrayAdapter
                        completeCurrencyList = currencyList;
                        renderCurrencyList(completeCurrencyList)
                        mainViewModel.saveCurrenciesListInDatabase(currencyList)

                    }
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.currencyRecyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

        mainViewModel.currencyList.observe(this) {
            Log.e("currencyList.observe", "inside")
            when (it.status) {
                Status.SUCCESS -> {
                    CoroutineScope(Dispatchers.Main).launch() {
                        binding.progressBar.visibility = View.GONE
                        binding.currencyRecyclerView.visibility = View.VISIBLE

                        var currencyList = mutableListOf<Currency>()
                        currencyNamesList.clear()
                        currencyList = it.data as MutableList<Currency>

                        for (item in currencyList) {
                            currencyNamesList.add(item.name.toString())
                            println("name = $item.name.toString()")
                        }

                        binding.spinner.adapter = spinnerArrayAdapter
                        completeCurrencyList = currencyList;
                        renderCurrencyList(completeCurrencyList)
                    }
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.currencyRecyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderCurrencyList(currencyList: MutableList<Currency>) {
        Log.e("currencyList", "inside " + currencyList.size)
        adapter.updateData(currencyList)
        adapter.notifyDataSetChanged()
        spinnerArrayAdapter.notifyDataSetChanged()
    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
        selectedCurrency = completeCurrencyList.get(position);
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {

    }
}