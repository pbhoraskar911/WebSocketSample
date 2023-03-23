package com.stocksapp.network.data

import com.google.gson.annotations.SerializedName


/**
 * Created by Pranav Bhoraskar on 3/18/23
 */
data class HoldingsResponse(
    @SerializedName("client_id") val clientId: String?,
    val data: List<Holdings>?,
    @SerializedName("response_type") val responseType: String?,
    val error: Any,
    val timestamp: Long
)

data class Holdings(
    val symbol: String,
    val quantity: Int,
    val ltp: Double,

    val avg_price: String,
    val close: Double,
    val cnc_used_quantity: Int,
    val collateral_qty: Int,
    val collateral_type: String,
    val collateral_update_qty: Int,
    val company_name: String,
    val haircut: Double,
    val holdings_update_qty: Int,
    val isin: String,
    val product: String,
    val t1_holding_qty: Int,
    val token_bse: String,
    val token_nse: String,
    val withheld_collateral_qty: Int,
    val withheld_holding_qty: Int
)
