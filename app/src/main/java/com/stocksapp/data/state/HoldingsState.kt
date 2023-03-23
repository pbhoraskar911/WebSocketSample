package com.stocksapp.data.state

import com.stocksapp.network.data.HoldingsResponse
import com.stocksapp.util.ProgressBarState

/**
 * Created by Pranav Bhoraskar on 3/22/23
 */
data class HoldingsState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val holdingsResponse: HoldingsResponse? = null
)
