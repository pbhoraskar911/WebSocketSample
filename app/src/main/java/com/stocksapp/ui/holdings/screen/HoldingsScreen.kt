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
import androidx.compose.foundation.shape.CutCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun HoldingsScreen(
    viewModel: HoldingsViewModel,
    calculatePL: (Holdings) -> Double,
    calculateTotalCurrentValue: (List<Holdings>) -> Double,
    calculateTotalPL: (List<Holdings>) -> Double,
    calculateTotalInvestment: (List<Holdings>) -> Double,
    calculateTodayProfitLoss: (List<Holdings>) -> Double
) {
    val lifecycleEvent = rememberLifecycleEvent()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val holdingsResponseState by viewModel.holdingsResponseState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME)
            viewModel.fetchHoldings()
    }

    HoldingsScreenUi(
        loadingState = loadingState,
        holdingsResponseState = holdingsResponseState,
        calculatePL = calculatePL,
        calculateTotalCurrentValue = calculateTotalCurrentValue,
        calculateTotalPL = calculateTotalPL,
        calculateTotalInvestment,
        calculateTodayProfitLoss
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoldingsScreenUi(
    loadingState: ProgressBarState,
    holdingsResponseState: HoldingsState,
    calculatePL: (Holdings) -> Double,
    calculateTotalCurrentValue: (List<Holdings>) -> Double,
    calculateTotalPL: (List<Holdings>) -> Double,
    calculateTotalInvestment: (List<Holdings>) -> Double,
    calculateTodayProfitLoss: (List<Holdings>) -> Double
) {
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
            HoldingsWidget(
                holdingsList = holdingsResponseState.holdingsResponse?.data,
                calculatePL = calculatePL,
                calculateTotalCurrentValue = calculateTotalCurrentValue,
                calculateTotalPL = calculateTotalPL,
                calculateTotalInvestment,
                calculateTodayProfitLoss
            )
            ProgressIndicatorMolecule(isLoading = loadingState == ProgressBarState.Loading)
        }
    }
}

@Composable
fun HoldingsWidget(
    holdingsList: List<Holdings>?,
    calculatePL: (Holdings) -> Double,
    calculateTotalCurrentValue: (List<Holdings>) -> Double,
    calculateTotalPL: (List<Holdings>) -> Double,
    calculateTotalInvestment: (List<Holdings>) -> Double,
    calculateTodayProfitLoss: (List<Holdings>) -> Double
) {
    if (holdingsList.isNullOrEmpty()) return
    Column {
        LazyColumn(
            contentPadding = PaddingValues(0.dp),
        ) {
            items(holdingsList.size) { index ->
                HoldingItem(holdings = holdingsList[index], calculatePL = calculatePL)
                if (index < holdingsList.lastIndex)
                    Divider(color = Color.Gray, thickness = 0.5.dp)
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        BottomScreenComposable(
            holdingsList,
            calculateTotalCurrentValue,
            calculateTotalPL,
            calculateTotalInvestment,
            calculateTodayProfitLoss
        )
    }
}

@Composable
fun BottomScreenComposable(
    holdingsList: List<Holdings>,
    totalCurrentValue: (List<Holdings>) -> Double,
    calculateTotalPL: (List<Holdings>) -> Double,
    calculateTotalInvestment: (List<Holdings>) -> Double,
    calculateTodayProfitLoss: (List<Holdings>) -> Double
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CutCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            BottomRowComposable(
                stringResource(R.string.string_current_value),
                totalCurrentValue.invoke(holdingsList)
            )
            Spacer(modifier = Modifier.height(8.dp))
            BottomRowComposable(
                stringResource(R.string.string_total_investment),
                calculateTotalInvestment.invoke(holdingsList)
            )
            Spacer(modifier = Modifier.height(8.dp))
            BottomRowComposable(
                stringResource(R.string.string_today_profit_loss),
                calculateTodayProfitLoss.invoke(holdingsList)
            )
            Spacer(modifier = Modifier.height(32.dp))
            BottomRowComposable(
                stringResource(R.string.string_profit_loss),
                calculateTotalPL.invoke(holdingsList)
            )
        }
    }
}

@Composable
fun BottomRowComposable(text: String, result: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
    ) {
        Text(text = text, style = TextStyle(fontWeight = FontWeight.W700, fontSize = 16.sp))
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${stringResource(id = R.string.rupee_symbol)} $result",
            style = TextStyle(
                fontWeight = FontWeight.W500, fontSize = 14.sp,
                color = if (result < 0.0) Color.Red else Color.Green
            )
        )
    }
}

@Composable
fun HoldingItem(
    holdings: Holdings,
    calculatePL: (Holdings) -> Double
) {

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
                    text = stringResource(R.string.string_ltp_label),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.align(CenterVertically)
                )

                Text(
                    text = "${stringResource(id = R.string.rupee_symbol)} ${holdings.ltp}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W700,
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
                    text = stringResource(R.string.string_profit_loss_label),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.align(CenterVertically)
                )

                Text(
                    text = "${stringResource(id = R.string.rupee_symbol)} ${calculatePL.invoke(holdings)}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.align(CenterVertically)
                )
            }
        }
    }
}