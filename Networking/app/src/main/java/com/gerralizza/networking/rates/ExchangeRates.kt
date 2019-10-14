package com.gerralizza.networking.rates

import java.io.Serializable
import java.util.*

data class ExchangeRate(val name: String, val value: Float) : Serializable
data class ExchangeRates(val base: String, val date: Date, val rates: List<ExchangeRate>)