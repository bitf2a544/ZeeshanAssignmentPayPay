package com.example.zeeshanassignmentpaypay.di.module

import com.example.zeeshanassignmentpaypay.BuildConfig
import com.example.zeeshanassignmentpaypay.data.api.ApiHelper
import com.example.zeeshanassignmentpaypay.data.api.ApiHelperImpl
import com.example.zeeshanassignmentpaypay.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/*
ApplicationComponent is Deprecated in Dagger Version 2.30
ApplicationComponent removed in Dagger Version 2.31
Alternatively SingletonComponent should be used instead of ApplicationComponent
*/


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    // Fields from default config.

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

}