package com.stocksapp.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Created by Pranav Bhoraskar
 */
@Composable
fun ProgressIndicatorMolecule(isLoading: Boolean) {
    if (isLoading) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
        }
    }
}