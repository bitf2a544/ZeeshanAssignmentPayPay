package com.example.zeeshanassignmentpaypay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zeeshanassignmentpaypay.data.model.Currency
import com.example.zeeshanassignmentpaypay.databinding.ActivityMain2Binding
import com.example.zeeshanassignmentpaypay.utils.Status
import com.example.zeeshanassignmentpaypay.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity2 : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var adapter: CurrencyRatesAdapter
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding.currencyRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CurrencyRatesAdapter(arrayListOf())
        binding.currencyRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.currencyRecyclerView.context,
                (binding.currencyRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.currencyRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        mainViewModel.currencyList2.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.currencyRecyclerView.visibility = View.VISIBLE
                    it.data?.let { currency -> renderCurrencyList(currency) }

                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.currencyRecyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun renderCurrencyList(currencyList: List<Currency>) {
        Log.e("currencyList", "inside " + currencyList.size)
        adapter.addData(currencyList)
        adapter.notifyDataSetChanged()
    }
}