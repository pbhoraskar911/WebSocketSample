package com.stocksapp.domain

import com.computations.PLLogicInterface
import com.stocksapp.data.repository.HoldingsRepository
import com.stocksapp.network.data.Holdings
import com.stocksapp.network.data.HoldingsResponse
import com.stocksapp.network.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Pranav Bhoraskar on 3/18/23
 */
interface HoldingsUseCase {
    suspend fun getHoldings(): Flow<NetworkResult<HoldingsResponse?>>
    fun calculatePL(holding: Holdings): Double
    fun calculateTotalCurrentValue(listOfHoldings: List<Holdings>): Double
    fun calculateTotalPL(listOfHoldings: List<Holdings>): Double
    fun calculateTotalInvestment(listOfHoldings: List<Holdings>): Double
    fun calculateTodayProfitLoss(listOfHoldings: List<Holdings>): Double
}

class HoldingsUseCaseImpl @Inject constructor(
    private val holdingsRepository: HoldingsRepository,
    private val plLogicInterface: PLLogicInterface
) : HoldingsUseCase {
    override suspend fun getHoldings(): Flow<NetworkResult<HoldingsResponse?>> =
        holdingsRepository.getHoldings()

    override fun calculatePL(holding: Holdings): Double {
        val result = plLogicInterface.calculatePL(
            plLogicInterface.calculateInvestmentValue(
                holding.avg_price.toDouble(),
                holding.quantity
            ),
            plLogicInterface.calculateCurrentValue(holding.quantity, holding.ltp)
        )
        return String.format("%.2f", result).toDouble()
    }

    override fun calculateTotalCurrentValue(listOfHoldings: List<Holdings>): Double {
        var totalCurrentValue = 0.0
        listOfHoldings.forEach { holding ->
            totalCurrentValue += plLogicInterface.calculateCurrentValue(
                holding.quantity,
                holding.ltp
            )
        }
        return totalCurrentValue
    }

    override fun calculateTotalPL(listOfHoldings: List<Holdings>): Double {
        var totalProfitLoss = 0.0
        listOfHoldings.forEach { holding ->
            totalProfitLoss += calculatePL(holding)
        }
        return totalProfitLoss
    }

    override fun calculateTotalInvestment(listOfHoldings: List<Holdings>): Double {
        var totalInvestment = 0.0
        listOfHoldings.forEach { holding ->
            totalInvestment += plLogicInterface.calculateInvestmentValue(
                holding.avg_price.toDouble(),
                holding.quantity
            )
        }
        return totalInvestment
    }

    override fun calculateTodayProfitLoss(listOfHoldings: List<Holdings>): Double {
        var todayProfitLoss = 0.0
        listOfHoldings.forEach { holding ->
            todayProfitLoss += ((holding.close - holding.ltp) * holding.quantity)
        }
        return todayProfitLoss
    }
}