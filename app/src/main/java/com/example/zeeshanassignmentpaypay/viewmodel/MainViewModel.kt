package com.example.zeeshanassignmentpaypay.viewmodel

import androidx.lifecycle.*
import com.example.zeeshanassignmentpaypay.BuildConfig
import com.example.zeeshanassignmentpaypay.data.model.Currency
import com.example.zeeshanassignmentpaypay.data.model.ExchangeRates
import com.example.zeeshanassignmentpaypay.data.repository.MainRepository
import com.example.zeeshanassignmentpaypay.utils.NetworkHelper
import com.example.zeeshanassignmentpaypay.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val mutableLiveDataCurrecnysList = MutableLiveData<Resource<List<Currency>>>()
    private val mutableLiveDataExchangeRates = MutableLiveData<Resource<ExchangeRates>>()


    val exchangeRate: LiveData<Resource<ExchangeRates>> get() = mutableLiveDataExchangeRates

    val currencyList2: LiveData<Resource<List<Currency>>> get() = mutableLiveDataCurrecnysList

    var currencyList = ArrayList<Currency>()

    init {
        fetchLatestExchangesRates()
    }

    private fun fetchLatestExchangesRates() {
        viewModelScope.launch {
            mutableLiveDataExchangeRates.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY).let {
                    if (it.isSuccessful) {
                        var exchangeRates = it.body();
                        var obj = Gson().toJsonTree(exchangeRates?.rates).getAsJsonObject()
                        for ((key, value) in obj.entrySet()) {
                            currencyList.add(Currency(key, value.asDouble))
                            println("Key = $key Value = $value")
                        }
                        mutableLiveDataCurrecnysList.postValue(Resource.success(currencyList))
                        //   mutableLiveDataExchangeRates.postValue(Resource.success(exchangeRates))
                    } else {
                        mutableLiveDataExchangeRates.postValue(
                            Resource.error(
                                it.errorBody().toString(), null
                            )
                        )
                    }
                }
            } else mutableLiveDataExchangeRates.postValue(
                Resource.error(
                    "No internet connection",
                    null
                )
            )
        }
    }
}