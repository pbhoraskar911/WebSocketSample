package com.stocksapp.data.repository

import com.stocksapp.network.data.HoldingsResponse
import com.stocksapp.network.network.NetworkResult
import com.stocksapp.network.network.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Pranav Bhoraskar on 3/18/23
 */
interface HoldingsRepository {
    suspend fun getHoldings(): Flow<NetworkResult<HoldingsResponse?>>
}

class HoldingsRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
) : HoldingsRepository {
    override suspend fun getHoldings(): Flow<NetworkResult<HoldingsResponse?>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = networkService.getHoldings()
            if (response?.isSuccessful == true) emit(NetworkResult.Success(response.body()))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e))
        }
    }
}