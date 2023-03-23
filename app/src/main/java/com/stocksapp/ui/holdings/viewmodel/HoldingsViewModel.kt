package com.stocksapp.ui.holdings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stocksapp.data.state.HoldingsState
import com.stocksapp.domain.HoldingsUseCase
import com.stocksapp.network.connectivity.ConnectionState
import com.stocksapp.network.connectivity.ConnectivityObserver
import com.stocksapp.network.data.Holdings
import com.stocksapp.network.network.NetworkResult
import com.stocksapp.util.ProgressBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pranav Bhoraskar on 3/18/23
 */
@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val holdingsUseCase: HoldingsUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _loadingState = MutableStateFlow<ProgressBarState>(ProgressBarState.Idle)
    val loadingState: StateFlow<ProgressBarState> get() = _loadingState

    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> get() = _connectionState

    private val _holdingsResponseState = MutableStateFlow(HoldingsState())
    val holdingsResponseState: StateFlow<HoldingsState> get() = _holdingsResponseState

    init {
        observeConnectivity()
    }

    private fun observeConnectivity() {
        connectivityObserver.connectionState
            .distinctUntilChanged()
            .map { it === ConnectionState.Available }
            .onEach {
                _connectionState.value = it
            }
            .launchIn(viewModelScope)
    }

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