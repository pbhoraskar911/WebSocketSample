package com.stocksapp.ui.holdings.screen;

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stocksapp.R
import com.stocksapp.network.data.Holdings
import com.stocksapp.ui.compose.ProgressIndicatorMolecule
import com.stocksapp.ui.holdings.viewmodel.HoldingsState
import com.stocksapp.ui.holdings.viewmodel.HoldingsViewModel
import com.stocksapp.ui.stockslist.presentation.screen.rememberLifecycleEvent
import com.stocksapp.util.ProgressBarState

/**
 * Created by Pranav Bhoraskar on 3/18/23
 */
@Composable
fun HoldingsScreen(viewModel: HoldingsViewModel) {
    val lifecycleEvent = rememberLifecycleEvent()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val holdingsResponseState by viewModel.holdingsResponseState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME)
            viewModel.fetchHoldings()
    }

    HoldingsScreenUi(loadingState, holdingsResponseState)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoldingsScreenUi(loadingState: ProgressBarState, holdingsResponseState: HoldingsState) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.holdings)) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.LightGray)
        ) {
            HoldingsWidget(holdingsResponseState.holdingsResponse?.data)
        }
    }
    ProgressIndicatorMolecule(isLoading = loadingState == ProgressBarState.Loading)
}

@Composable
fun HoldingsWidget(holdingsList: List<Holdings>?) {
    val screenHeight = (LocalConfiguration.current.screenHeightDp) / 3
    Column {
        LazyColumn(
            contentPadding = PaddingValues(0.dp),
        ) {
            if (holdingsList.isNullOrEmpty()) return@LazyColumn
            items(holdingsList.size) { index ->
                HoldingItem(holdingsList[index])
                if (index < holdingsList.lastIndex)
                    Divider(color = Color.Gray, thickness = 0.5.dp)
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight.dp)
                .background(Color.White)
        ) {}
    }
}

@Composable
fun HoldingItem(holdings: Holdings) {

    Box(modifier = Modifier.background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = holdings.symbol,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.align(CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Ltp: ${holdings.ltp}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.align(CenterVertically)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = "${holdings.quantity}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.align(CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Avg. Price: ${holdings.avg_price}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.align(CenterVertically)
                )
            }
        }
    }
}