package com.computations

/**
 * Created by Pranav Bhoraskar on 3/19/23
 */
interface PLLogicInterface {
    fun calculateCurrentValue(quantity:Int, ltp: Double): Double
    fun calculateInvestmentValue(avgPrice: Double, quantity: Int): Double
    fun calculatePL(investmentValue: Double, currentValue: Double): Double
}