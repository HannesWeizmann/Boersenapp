package com.example.boersenapp.api.tickers.dataclass

data class Pagination(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val total: Int
)