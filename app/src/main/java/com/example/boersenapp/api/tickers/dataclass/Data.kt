package com.example.boersenapp.api.tickers.dataclass

data class Data(
    val country: Any,
    val has_eod: Boolean,
    val has_intraday: Boolean,
    val name: String,
    val stock_exchange: StockExchange,
    val symbol: String
)