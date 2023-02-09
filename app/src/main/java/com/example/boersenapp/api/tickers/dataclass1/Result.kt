package com.example.boersenapp.api.tickers.dataclass1

data class Result(
    val active: Boolean,
    val cik: String,
    val composite_figi: String,
    val currency_name: String,
    val last_updated_utc: String,
    val locale: String,
    val market: String,
    val name: String,
    val primary_exchange: String,
    val share_class_figi: String,
    val ticker: String,
    val type: String
)