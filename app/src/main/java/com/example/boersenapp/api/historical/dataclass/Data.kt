package com.example.boersenapp.api.historical.dataclass

data class Data(
    val adj_close: Double,
    val adj_high: Double,
    val adj_low: Double,
    val adj_open: Double,
    val adj_volume: Double,
    val close: Double,
    val date: String,
    val dividend: Double,
    val exchange: String,
    val high: Double,
    val low: Double,
    val `open`: Double,
    val split_factor: Double,
    val symbol: String,
    val volume: Double
)