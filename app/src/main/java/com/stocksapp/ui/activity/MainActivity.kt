package com.stocksapp.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.stocksapp.ui.navigation.Screen
import com.stocksapp.ui.stockslist.presentation.screen.StocksListScreen
import com.stocksapp.ui.stockslist.presentation.viewmodel.StocksListViewModel
import com.stocksapp.util.Constants.NAV_ANIMATION_DURATION
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Pranav Bhoraskar on 3/14/23
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreenComposable()
            }
        }
    }
}

@Composable
fun MainScreenComposable() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        StocksAppNavigation()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StocksAppNavigation() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.StocksList.route,
    ) {
        addStocksListScreen(navController)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addStocksListScreen(navController: NavHostController) {
    composable(
        route = Screen.StocksList.route,
        enterTransition = {
            when (initialState.destination.route) {
                Screen.StocksList.route -> slideInHorizontally(
                    initialOffsetX = { NAV_ANIMATION_DURATION },
                    animationSpec = tween(NAV_ANIMATION_DURATION)
                ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION))
                else -> null
            }
        }
    ) {
        val stocksListViewModel: StocksListViewModel = hiltViewModel()
        StocksListScreen(viewModel = stocksListViewModel)
    }
}