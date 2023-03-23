package com.stocksapp.data.repository

import com.google.gson.Gson
import com.stocksapp.network.data.StocksResponse
import com.stocksapp.network.network.NetworkResult
import com.stocksapp.network.util.NetworkConstants.BASE_URL
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

/**
 * Created by Pranav Bhoraskar on 3/14/23
 */

interface StocksAppRepository {
    suspend fun getStocksData(): Flow<NetworkResult<StocksResponse?>>
    suspend fun closeConnection()
}

class StocksAppRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : StocksAppRepository {

    private companion object {
        const val SUBSCRIBE =
            "{\"type\":\"subscribe\",\"channels\":[{\"name\":\"ticker\",\"product_ids\":[\"BTC-USD\"]}]}"
    }

    private var webSocket: WebSocket? = null

    override suspend fun getStocksData(): Flow<NetworkResult<StocksResponse?>> = channelFlow {
        send(NetworkResult.Loading)
        try {
            connectWebSocket(BASE_URL).collectLatest {
                send(NetworkResult.Success(it))
            }
        } catch (e: Exception) {
            send(NetworkResult.Error(e))
        }
    }

    override suspend fun closeConnection() {
        webSocket?.close(1000, "Connection closed.")
    }


    private fun connectWebSocket(url: String): Flow<StocksResponse> = callbackFlow {
        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                webSocket.close(1000, reason)
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: okhttp3.Response?
            ) {
                super.onFailure(webSocket, t, response)
                webSocket.close(1000, response.toString())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val fromJson: StocksResponse = Gson().fromJson(text, StocksResponse::class.java)
                trySend(fromJson)
            }

            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
                webSocket.send(SUBSCRIBE)
            }
        })
        awaitClose {}
    }
}
