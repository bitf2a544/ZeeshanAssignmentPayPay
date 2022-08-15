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

 /*   private val mutableLiveDataCurrecnyList = MutableLiveData<Resource<List<Currency>>>()
    val currencyList: LiveData<Resource<List<Currency>>> get() = mutableLiveDataCurrecnyList
*/

    private val mutableLiveDataExchangeRate = MutableLiveData<Resource<ExchangeRates>>()
    val exchangeRates: LiveData<Resource<ExchangeRates>> get() = mutableLiveDataExchangeRate


    init {
        fetchLatestExchangesRates()
    }

    private fun fetchLatestExchangesRates() {

        viewModelScope.launch {
            try {
                mutableLiveDataExchangeRate.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    mainRepository.getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY).let {
                        if (it.isSuccessful) {

                          /*  var currencyList = ArrayList<Currency>()
                            var currencyNamesList = ArrayList<String>()
                            var exchangeRates = it.body();
                            var obj = Gson().toJsonTree(exchangeRates?.rates).getAsJsonObject()
                            for ((key, value) in obj.entrySet()) {
                                currencyList.add(Currency(key, value.asDouble))
                                currencyNamesList.add(key)
                                println("Key = $key Value = $value")
                            }*/
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


     /*   viewModelScope.launch {
            try {
                mutableLiveDataCurrecnyList.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    mainRepository.getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY).let {
                        if (it.isSuccessful) {

                                var currencyList = ArrayList<Currency>()
                                var currencyNamesList = ArrayList<String>()
                                var exchangeRates = it.body();
                                var obj = Gson().toJsonTree(exchangeRates?.rates).getAsJsonObject()
                                for ((key, value) in obj.entrySet()) {
                                    currencyList.add(Currency(key, value.asDouble))
                                    currencyNamesList.add(key)
                                    println("Key = $key Value = $value")
                                }
                                mutableLiveDataCurrecnyList.postValue(Resource.success(currencyList))

                        } else {
                            mutableLiveDataCurrecnyList.postValue(
                                Resource.error(
                                    it.errorBody().toString(), null
                                )
                            )
                        }
                    }
                } else mutableLiveDataCurrecnyList.postValue(
                    Resource.error(
                        "No internet connection",
                        null
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
    }
}