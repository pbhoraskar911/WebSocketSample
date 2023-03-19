package com.stocksapp.domain

import com.stocksapp.data.repository.HoldingsRepository
import com.stocksapp.network.data.HoldingsResponse
import com.stocksapp.network.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Pranav Bhoraskar on 3/18/23
 */
interface HoldingsUseCase {
    suspend fun getHoldings(): Flow<NetworkResult<HoldingsResponse?>>
}

class HoldingsUseCaseImpl @Inject constructor(
    private val holdingsRepository: HoldingsRepository
) : HoldingsUseCase {
    override suspend fun getHoldings(): Flow<NetworkResult<HoldingsResponse?>> =
        holdingsRepository.getHoldings()
}