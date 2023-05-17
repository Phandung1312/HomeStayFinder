package com.personal.homestayfinder.common

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

object Utils {
    fun toFormattedNumberString(text : String?): String {
        val number = text?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val numberFormat = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
        val symbol = DecimalFormatSymbols(Locale.getDefault())
        symbol.groupingSeparator = '.'
        numberFormat.decimalFormatSymbols = symbol
        return numberFormat.format(number)
    }
}