package com.stocksapp.network.websocket

import com.stocksapp.network.data.StocksResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

/**
 * Created by Pranav Bhoraskar on 3/15/23
 */
interface WebSocketService {
    suspend fun connect(url: String): Flow<StocksResponse>
}