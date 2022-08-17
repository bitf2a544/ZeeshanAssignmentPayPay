package com.example.zeeshanassignmentpaypay.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.zeeshanassignmentpaypay.BuildConfig
import com.example.zeeshanassignmentpaypay.data.local.dao.CurrencyDao
import com.example.zeeshanassignmentpaypay.data.local.db.AppDatabase
import com.example.zeeshanassignmentpaypay.data.model.ExchangeRates
import com.example.zeeshanassignmentpaypay.data.remote.api.ApiHelper
import com.example.zeeshanassignmentpaypay.data.remote.api.ApiHelperImpl
import com.example.zeeshanassignmentpaypay.utils.MockWebServerBaseTest2
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import retrofit2.Response
import javax.inject.Inject


@RunWith(JUnit4::class)
//class MainRepositoryTest :  MockWebServerBaseTest() {
class MainRepositoryTest :  MockWebServerBaseTest2() {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    @Mock
    lateinit var photosRepository: MainRepository
    //private lateinit var apiService: ApiService
    @Mock
      lateinit var currencyDao: CurrencyDao
    @Mock
      lateinit var appDatabase: AppDatabase

    @Mock
    lateinit var  apiHelper: ApiHelper

    @Inject
    lateinit var  context: Context


   // override fun isMockServerEnabled() = true

    @Before
    fun start() {
        apiService = provideTestApiService()
        apiHelper = provideApiHelper(ApiHelperImpl(apiService))

        context = mock(Context::class.java)
        appDatabase = provideAppDatabase( context)
        currencyDao = provideCurrencyDao(appDatabase)
        photosRepository = MainRepository(apiHelper,currencyDao)
    }

    @Test
     fun `get all exchange rates test`() {
        runBlocking {

            Mockito.`when`(photosRepository.getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY)).thenReturn(Response.success(ExchangeRates()))
            val response = photosRepository.getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY)
            assertEquals(true, response !=null)
        }

    }


/*
    @Test
    fun `given response ok when fetching results then return a list with elements`() {
        runBlocking {
            mockHttpResponse("json/photos_response_one_item.json", HttpURLConnection.HTTP_OK)
            val apiResponse = photosRepository.getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY)

            assertNotNull(apiResponse)
            assertEquals(apiResponse.body(), 1)
        }
    }

    @Test
    fun `given response ok when fetching empty results then return an empty list`() {
        runBlocking {
            mockHttpResponse("json/photos_response_empty_list.json", HttpURLConnection.HTTP_OK)
            val apiResponse = photosRepository.getLatestExchangeRatesFromNetwork(BuildConfig.API_KEY)

            assertNotNull(apiResponse)
            assertEquals(apiResponse.body(), 0)
        }
    }*/

   /* @Test
    fun `given response failure when fetching results then return exception`() {
        runBlocking {
            mockHttpResponse(MainRepository.GENERAL_ERROR_CODE)
            val apiResponse = photosRepository.getPhotosFromApi(sol, page)

            assertNotNull(apiResponse)
            val expectedValue = Result.Error(Exception(SOMETHING_WRONG))
            assertEquals(expectedValue.exception.message, (apiResponse as Result.Error).exception.message)
        }
    }*/
}
