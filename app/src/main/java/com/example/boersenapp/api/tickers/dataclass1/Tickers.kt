package com.example.boersenapp.api.tickers.dataclass1

data class Tickers(
    val count: Int,
    val next_url: String,
    val request_id: String,
    val results: List<Result>,
    val status: String
)