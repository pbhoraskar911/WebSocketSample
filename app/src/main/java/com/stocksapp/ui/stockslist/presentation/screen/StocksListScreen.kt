package com.stocksapp.ui.stockslist.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stocksapp.R
import com.stocksapp.ui.compose.ProgressIndicatorMolecule
import com.stocksapp.ui.stockslist.presentation.viewmodel.StocksListScreenState
import com.stocksapp.ui.stockslist.presentation.viewmodel.StocksListViewModel
import com.stocksapp.util.ProgressBarState

/**
 * Created by Pranav Bhoraskar on 3/14/23
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksListScreen(viewModel: StocksListViewModel) {
    val lifecycleEvent = rememberLifecycleEvent()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val stocksResponseState by viewModel.stocksResponseState.collectAsStateWithLifecycle()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_PAUSE) {
            viewModel.closeSocketConnection()
        }
        if(lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            viewModel.startSocketConnection()
        }
    }

    LoadStockScreenUI(loadingState, stocksResponseState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadStockScreenUI(
    loadingState: ProgressBarState,
    stocksResponseState: StocksListScreenState
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.stocks_app)) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                stocksResponseState.stocksListResponseState?.price?.let {
                    Text(
                        text = "BTC-USD - $$it",
                        modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(
                            fontSize = 24.sp
                        )
                    )
                }
            }
        }
    }
    ProgressIndicatorMolecule(isLoading = loadingState == ProgressBarState.Loading)
}

@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var state by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            state = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return state
}