package com.example.boersenapp.api.news.dataclass

data class Result(
    val article_url: String,
    val author: String,
    val description: String,
    val id: String,
    val image_url: String,
    val keywords: List<String>,
    val published_utc: String,
    val publisher: Publisher,
    val tickers: List<String>,
    val title: String
)