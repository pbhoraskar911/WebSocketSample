package com.stocksapp.network.connectivity

import android.net.ConnectivityManager
import com.stocksapp.network.util.currentConnectivityState
import com.stocksapp.network.util.observeConnectivityAsFlow
import kotlinx.coroutines.flow.Flow

/**
 * Created by Pranav Bhoraskar on 3/22/23
 */
interface ConnectivityObserver {
    /**
     * Gives the realtime updates of a [ConnectionState]
     */
    val connectionState: Flow<ConnectionState>

    /**
     * Retrieves the current [ConnectionState]
     */
    val currentConnectionState: ConnectionState
}

class ConnectivityObserverImpl(
    private val connectivityManager: ConnectivityManager
) : ConnectivityObserver {

    override val connectionState: Flow<ConnectionState>
        get() = connectivityManager.observeConnectivityAsFlow()

    override val currentConnectionState: ConnectionState
        get() = connectivityManager.currentConnectivityState
}