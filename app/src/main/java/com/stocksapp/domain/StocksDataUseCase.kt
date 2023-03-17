package com.stocksapp.domain

import com.stocksapp.data.repository.StocksAppRepository
import com.stocksapp.network.data.StocksResponse
import com.stocksapp.network.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Pranav Bhoraskar on 3/14/23
 */
interface StocksDataUseCase {
    suspend fun getStocksData(): Flow<NetworkResult<StocksResponse?>>
    suspend fun closeSocketConnection()
}

class StocksDataUseCaseImpl @Inject constructor(
    private val stocksAppRepository: StocksAppRepository
) : StocksDataUseCase {
    override suspend fun getStocksData(): Flow<NetworkResult<StocksResponse?>> {
        return stocksAppRepository.getStocksData()
    }

    override suspend fun closeSocketConnection() {
        stocksAppRepository.closeConnection()
    }
}