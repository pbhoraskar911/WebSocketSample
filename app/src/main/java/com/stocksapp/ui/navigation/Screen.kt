package com.stocksapp.ui.navigation

import androidx.navigation.NamedNavArgument

/**
 * Created by Pranav Bhoraskar on 3/14/23
 */
sealed class Screen(open val route: String = "", val arguments: List<NamedNavArgument>) {
    object StocksList : Screen("stocks_list", emptyList())
}