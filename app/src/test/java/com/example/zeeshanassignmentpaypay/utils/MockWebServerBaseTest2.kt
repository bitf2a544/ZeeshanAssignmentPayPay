package com.example.zeeshanassignmentpaypay.utils

import android.content.Context
import androidx.room.Room
import com.example.zeeshanassignmentpaypay.BuildConfig
import com.example.zeeshanassignmentpaypay.data.local.dao.CurrencyDao
import com.example.zeeshanassignmentpaypay.data.local.db.AppDatabase
import com.example.zeeshanassignmentpaypay.data.remote.api.ApiHelper
import com.example.zeeshanassignmentpaypay.data.remote.api.ApiHelperImpl
import com.example.zeeshanassignmentpaypay.data.remote.api.ApiService
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

open class MockWebServerBaseTest2 {

    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: ApiService
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gson = Gson()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService::class.java)
    }


   /* @Test
    fun `get all exchange rates test`() {
        runBlocking {
             val mockResponse = MockResponse()
            mockWebServer.enqueue(mockResponse.setBody("[]"))
            val response = apiService.getLatestExchangeRates(BuildConfig.API_KEY)
            val request = mockWebServer.takeRequest()
            assertEquals("api/latest.json",request.path)
            assertEquals(true, response.body()!=null)
        }
    }*/

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }


    private fun provideBaseUrl() = BuildConfig.BASE_URL


    private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    fun provideTestApiService(   ) :ApiService{
        return provideRetrofit(provideOkHttpClient(),provideBaseUrl()).create(ApiService::class.java)
    }

    private fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper



    fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }

    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "CurrencyExchangeDatabase"
        ).build()
    }
}