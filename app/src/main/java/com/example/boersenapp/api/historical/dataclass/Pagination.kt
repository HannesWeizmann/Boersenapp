package com.example.boersenapp.api.historical.dataclass

data class Pagination(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val total: Int
)