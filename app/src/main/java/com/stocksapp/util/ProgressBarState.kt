package com.stocksapp.util

/**
 * Created by Pranav Bhoraskar on 3/14/23
 *
 * Util class to handle the state of Progress Bar
 */
sealed class ProgressBarState {
    object Loading : ProgressBarState()
    object Idle : ProgressBarState()
}