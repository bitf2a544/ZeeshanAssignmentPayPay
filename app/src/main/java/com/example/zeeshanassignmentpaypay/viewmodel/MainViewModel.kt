package com.example.zeeshanassignmentpaypay.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.zeeshanassignmentpaypay.BuildConfig
import com.example.zeeshanassignmentpaypay.data.model.Currency
import com.example.zeeshanassignmentpaypay.data.model.ExchangeRates
import com.example.zeeshanassignmentpaypay.data.repository.MainRepository
import com.example.zeeshanassignmentpaypay.utils.NetworkHelper
import com.example.zeeshanassignmentpaypay.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val mutableLiveDataExchangeRate = MutableLiveData<Resource<ExchangeRates>>()
    val exchangeRates: LiveData<Resource<ExchangeRates>> get() = mutableLiveDataExchangeRate

    private val mutableLiveDataCurrencyList = MutableLiveData<Resource<List<Currency>>>()
    val currencyList: LiveData<Resource<List<Currency>>> get() = mutableLiveDataCurrencyList

    init {
        fetchLatestExchangesRates()
    }

    private fun fetchLatestExchangesRates() {
        mutableLiveDataExchangeRate.postValue(Resource.loading(null))

        CoroutineScope(Dispatchers.IO).launch {
            var currencyList = getCurrenciesListFromDatabase();

            if (currencyList == null || currencyList.isEmpty()) {
                viewModelScope.launch {
                    try {
                        //mutableLiveDataExchangeRate.postValue(Resource.loading(null))
                        if (networkHelper.isNetworkConnected()) {
                            mainRepository.getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY)
                                .let {
                                    if (it.isSuccessful) {
                                        mutableLiveDataExchangeRate.postValue(Resource.success(it.body()))
                                    } else {
                                        mutableLiveDataExchangeRate.postValue(
                                            Resource.error(
                                                it.errorBody().toString(), null
                                            )
                                        )
                                    }
                                }
                        } else mutableLiveDataExchangeRate.postValue(
                            Resource.error(
                                "No internet connection",
                                null
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                viewModelScope.launch {
                    try {
                        mutableLiveDataCurrencyList.postValue(Resource.loading(null))
                        mutableLiveDataCurrencyList.postValue(Resource.success(currencyList))

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }

    fun saveCurrenciesListInDatabase(list: MutableList<Currency>) {
        CoroutineScope(Dispatchers.IO).launch() {
            Log.e("saveCurrenciesListInDatabase", "inside_" + list.size)
            mainRepository.insertCurrencyListInDatabase(list)
        }
    }

    private fun getCurrenciesListFromDatabase(): MutableList<Currency> {
        return runBlocking {
            return@runBlocking mainRepository.getCurrencyListFromDatabase() as MutableList<Currency>
        }
    }
}