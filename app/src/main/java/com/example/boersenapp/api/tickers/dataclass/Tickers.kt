package com.example.boersenapp.api.tickers.dataclass

data class Tickers(
    val `data`: List<Data>,
    val pagination: Pagination
)