package com.example.zeeshanassignmentpaypay.ui.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zeeshanassignmentpaypay.adapter.CurrencyRatesAdapter
import com.example.zeeshanassignmentpaypay.data.model.Currency
import com.example.zeeshanassignmentpaypay.databinding.CurrenyListFragmentBinding
import com.example.zeeshanassignmentpaypay.utils.Status
import com.example.zeeshanassignmentpaypay.viewmodel.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CurrencyListFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: CurrenyListFragmentBinding
    private lateinit var currencyRatesAdapter: CurrencyRatesAdapter
    var completeCurrencyList = mutableListOf<Currency>()
    var currencyNamesList = mutableListOf<String>()
    var selectedCurrency = Currency("", 0.0, 0)

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var spinnerArrayAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CurrenyListFragmentBinding.inflate(layoutInflater)
        setupUI()
        setupObserver()
        return binding.root
    }

    private fun setupUI() {
        setUpCurrencyRecyclerView()
        setUpSearchViewChangeListener()
        setUpSpinner()
    }

    private fun setUpCurrencyRecyclerView() {
        binding.currencyRecyclerView.layoutManager = GridLayoutManager(activity, 4)
        currencyRatesAdapter = CurrencyRatesAdapter(arrayListOf())
        binding.currencyRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.currencyRecyclerView.context,
                (binding.currencyRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.currencyRecyclerView.adapter = currencyRatesAdapter

    }

    private fun setUpSearchViewChangeListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty() && selectedCurrency.name != null) {
                    currencyRatesAdapter.updateCurrencyRates(
                        query.toDouble(),
                        selectedCurrency.name.toString(),
                        completeCurrencyList
                    )
                } else {
                    currencyRatesAdapter.updateCurrencyRates(
                        1.0,
                        selectedCurrency.name.toString(),
                        completeCurrencyList
                    )
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty() && selectedCurrency.name != null) {
                    currencyRatesAdapter.updateCurrencyRates(
                        newText.toDouble(),
                        selectedCurrency.name.toString(),
                        completeCurrencyList
                    )
                } else {
                    currencyRatesAdapter.updateCurrencyRates(
                        1.0,
                        selectedCurrency.name.toString(),
                        completeCurrencyList
                    )
                }
                return false
            }
        })
    }

    private fun setUpSpinner() {
        binding.spinner.onItemSelectedListener = this
        //Creating the ArrayAdapter instance having the country list
        spinnerArrayAdapter = ArrayAdapter<String>(
            requireActivity().applicationContext, android.R.layout.simple_spinner_item,
            currencyNamesList
        )

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = spinnerArrayAdapter
    }

    private fun setupObserver() {
        mainViewModel.exchangeRates.observe(viewLifecycleOwner) {
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
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        mainViewModel.currencyList.observe(viewLifecycleOwner) {
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
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderCurrencyList(currencyList: MutableList<Currency>) {
        currencyRatesAdapter.updateData(currencyList)
        currencyRatesAdapter.notifyDataSetChanged()
        spinnerArrayAdapter.notifyDataSetChanged()
    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
        selectedCurrency = completeCurrencyList.get(position);
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {

    }
}