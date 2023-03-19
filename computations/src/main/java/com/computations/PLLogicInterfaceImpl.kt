package com.computations

/**
 * Created by Pranav Bhoraskar on 3/19/23
 */
class PLLogicInterfaceImpl: PLLogicInterface {
    override fun calculateCurrentValue(quantity: Int, ltp: Double)= ltp.times(quantity)
    override fun calculateInvestmentValue(avgPrice: Double, quantity: Int) = avgPrice.times(quantity)
    override fun calculatePL(investmentValue: Double, currentValue: Double) = currentValue - investmentValue
}