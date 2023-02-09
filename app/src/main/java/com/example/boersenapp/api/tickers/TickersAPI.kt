package com.example.boersenapp.api.tickers

import com.example.boersenapp.api.tickers.dataclass1.Tickers
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TickersAPI {
    @GET("/v3/reference/tickers")
    fun getTickers(@Query("active") active: Boolean,@Query("limit")limit: Int, @Query("apiKey") apiKey: String): Call<Tickers>
}