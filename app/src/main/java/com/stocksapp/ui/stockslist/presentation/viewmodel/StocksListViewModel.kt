package com.stocksapp.ui.stockslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stocksapp.domain.StocksDataUseCase
import com.stocksapp.network.data.StocksResponse
import com.stocksapp.network.network.NetworkResult
import com.stocksapp.util.ProgressBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pranav Bhoraskar on 3/14/23
 */
@HiltViewModel
class StocksListViewModel @Inject constructor(private val stocksDataUseCase: StocksDataUseCase) :
    ViewModel() {

    private val _loadingState = MutableStateFlow<ProgressBarState>(ProgressBarState.Idle)
    val loadingState: StateFlow<ProgressBarState> get() = _loadingState

    private val _stocksResponseState = MutableStateFlow(StocksListScreenState())
    val stocksResponseState: StateFlow<StocksListScreenState> get() = _stocksResponseState

    private fun getStocksData() = viewModelScope.launch(Dispatchers.IO) {
        stocksDataUseCase.getStocksData().collect {
            when (val networkResult: NetworkResult<StocksResponse?> =
                NetworkResult.Success(it).data) {
                is NetworkResult.Error -> {
                    _loadingState.value = ProgressBarState.Idle
                }
                NetworkResult.Loading -> {
                    _loadingState.value = ProgressBarState.Loading
                }
                is NetworkResult.Success -> {
                    val response =
                        StocksListScreenState(stocksListResponseState = networkResult.data)
                    _stocksResponseState.value = response
                    _loadingState.value = ProgressBarState.Idle
                }
            }
        }
    }

    suspend fun closeSocketConnection() = stocksDataUseCase.closeSocketConnection()
    fun startSocketConnection() {
        getStocksData()
    }
}

data class StocksListScreenState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val stocksListResponseState: StocksResponse? = null
)