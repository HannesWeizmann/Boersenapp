package com.example.boersenapp.api.tickers.dataclass

data class StockExchange(
    val acronym: String,
    val city: String,
    val country: String,
    val country_code: String,
    val mic: String,
    val name: String,
    val website: String
)