package com.example.boersenapp.api.historical.dataclass

data class Historical(
    val `data`: List<Data>,
    val pagination: Pagination
)