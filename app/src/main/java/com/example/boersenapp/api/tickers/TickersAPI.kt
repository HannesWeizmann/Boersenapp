package com.example.boersenapp.api.tickers

import com.example.boersenapp.api.tickers.dataclass.Tickers
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TickersAPI {
    @GET("/v1/tickers")
    fun getTickers(@Query("access_key") access_key: String, @Query("limit") limit: Int) : Call<Tickers>
}