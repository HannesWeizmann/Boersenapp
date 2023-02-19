package com.example.boersenapp.api.news.dataclass

data class News(
    val count: Int,
    val next_url: String,
    val request_id: String,
    val results: List<Result>,
    val status: String
)