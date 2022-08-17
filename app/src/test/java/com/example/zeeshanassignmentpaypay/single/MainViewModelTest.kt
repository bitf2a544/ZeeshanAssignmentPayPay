package com.example.zeeshanassignmentpaypay.single

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.zeeshanassignmentpaypay.BuildConfig
import com.example.zeeshanassignmentpaypay.data.model.Currency
import com.example.zeeshanassignmentpaypay.data.remote.api.ApiHelper
import com.example.zeeshanassignmentpaypay.repository.MainRepository
import com.example.zeeshanassignmentpaypay.utils.NetworkHelper
import com.example.zeeshanassignmentpaypay.utils.Resource
 
import com.example.zeeshanassignmentpaypay.utils.TestCoroutineRule
import com.example.zeeshanassignmentpaypay.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    @Inject
    lateinit var mainRepository: MainRepository

    @Mock
    private lateinit var  networkHelper: NetworkHelper
    @Mock
    @Inject
     lateinit var  apiHelper: ApiHelper

    @Mock
    private lateinit var apiUsersObserver: Observer<Resource<List<Currency>>>

    @Before
    fun setUp() {
        // do something if required
    }
//List<Currency>
    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(emptyList<Currency>())
                .`when`(mainRepository)
                .getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY)

            val viewModel = MainViewModel(mainRepository, networkHelper)
            viewModel.getCurrencyListromViewModel().observeForever(apiUsersObserver)
          //  verify(networkHelper).isNetworkConnected()
            verify(mainRepository).getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY)
            verify(apiUsersObserver).onChanged(Resource.success(emptyList()))
            viewModel.getCurrencyListromViewModel().removeObserver(apiUsersObserver)
        }
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message For You"
            doThrow(RuntimeException(errorMessage))
                .`when`(mainRepository)
                .getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY)
            val viewModel = MainViewModel(mainRepository, networkHelper)
            viewModel.getCurrencyListromViewModel().observeForever(apiUsersObserver)
         //   verify(networkHelper).isNetworkConnected()
            verify(mainRepository).getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY)
            verify(apiUsersObserver).onChanged(
                Resource.error(
                    RuntimeException(errorMessage).toString(),
                    null
                )
            )
       //     viewModel.getUsers().removeObserver(apiUsersObserver)
            viewModel.getCurrencyListromViewModel().removeObserver(apiUsersObserver)
        }
    }

    @After
    fun tearDown() {
        // do something if required
    }

}