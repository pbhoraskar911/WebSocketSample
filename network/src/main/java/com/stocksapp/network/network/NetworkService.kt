package com.stocksapp.network.network

import com.stocksapp.network.data.StocksResponse
import com.stocksapp.network.utils.Urls.HISTORY_DATA
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by Pranav Bhoraskar on 3/14/23
 */
interface NetworkService {

    @GET(HISTORY_DATA)
    suspend fun getStocksHistoricalData(
        @Header("x-api-key") apiKey: String
    ): Response<List<StocksResponse?>>?

}