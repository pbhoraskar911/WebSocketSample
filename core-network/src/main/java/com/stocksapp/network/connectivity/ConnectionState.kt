package com.stocksapp.network.connectivity

/**
 * Created by Pranav Bhoraskar on 3/22/23
 */
sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}