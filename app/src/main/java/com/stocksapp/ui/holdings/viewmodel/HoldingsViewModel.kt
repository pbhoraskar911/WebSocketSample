package com.stocksapp.ui.holdings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stocksapp.domain.HoldingsUseCase
import com.stocksapp.network.data.Holdings
import com.stocksapp.network.data.HoldingsResponse
import com.stocksapp.network.network.NetworkResult
import com.stocksapp.util.ProgressBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pranav Bhoraskar on 3/18/23
 */
@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val holdingsUseCase: HoldingsUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<ProgressBarState>(ProgressBarState.Idle)
    val loadingState: StateFlow<ProgressBarState> get() = _loadingState

    private val _holdingsResponseState = MutableStateFlow(HoldingsState())
    val holdingsResponseState: StateFlow<HoldingsState> get() = _holdingsResponseState

    fun fetchHoldings() = viewModelScope.launch {
        holdingsUseCase.getHoldings().collect {
            when (val networkResult = NetworkResult.Success(it).data) {
                is NetworkResult.Success -> {
                    val movieDetailResponseState =
                        HoldingsState(holdingsResponse = networkResult.data)
                    _holdingsResponseState.value = movieDetailResponseState
                    _loadingState.value = ProgressBarState.Idle
                }
                is NetworkResult.Loading -> {
                    _loadingState.value = ProgressBarState.Loading
                }
                else -> {
                    _loadingState.value = ProgressBarState.Idle
                }
            }
        }
    }

    fun calculatePL(holding: Holdings): Double {
        return holdingsUseCase.calculatePL(holding)
    }

    fun calculateTotalCurrentValue(listOfHoldings: List<Holdings>): Double {
        return holdingsUseCase.calculateTotalCurrentValue(listOfHoldings)
    }

    fun calculateTotalPL(listOfHoldings: List<Holdings>): Double {
        return holdingsUseCase.calculateTotalPL(listOfHoldings)
    }

    fun calculateTotalInvestment(listOfHoldings: List<Holdings>): Double {
        return holdingsUseCase.calculateTotalInvestment(listOfHoldings)
    }

    fun calculateTodayProfitLoss(listOfHoldings: List<Holdings>): Double {
        return holdingsUseCase.calculateTodayProfitLoss(listOfHoldings)
    }

}

data class HoldingsState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val holdingsResponse: HoldingsResponse? = null
)