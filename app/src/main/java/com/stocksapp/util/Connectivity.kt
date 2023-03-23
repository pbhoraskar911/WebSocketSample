package com.stocksapp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.stocksapp.network.connectivity.ConnectionState
import com.stocksapp.network.util.connectivityManager
import com.stocksapp.network.util.currentConnectivityState
import com.stocksapp.network.util.observeConnectivityAsFlow

/**
 * Created by Pranav Bhoraskar on 3/22/23
 */
@Composable
fun currentConnectionState(): ConnectionState {
    val connectivityManager = LocalContext.current.connectivityManager
    return remember { connectivityManager.currentConnectivityState }
}

@Composable
fun connectivityState(): State<ConnectionState> {
    val connectivityManager = LocalContext.current.connectivityManager
    return produceState(initialValue = connectivityManager.currentConnectivityState) {
        connectivityManager.observeConnectivityAsFlow().collect { value = it }
    }
}