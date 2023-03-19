package com.stocksapp.network.network

import com.stocksapp.network.utils.NetworkConstants.BASE_URL
import com.stocksapp.network.utils.NetworkConstants.BASE_URL_STOCKS_HOLDINGS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Pranav Bhoraskar on 3/14/23
 */
object Networking {

    private const val NETWORK_CALL_TIMEOUT = 60L

    fun create(): NetworkService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_STOCKS_HOLDINGS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClientBuilder().build())
            .build()
            .create(NetworkService::class.java)
    }

    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
    }
}