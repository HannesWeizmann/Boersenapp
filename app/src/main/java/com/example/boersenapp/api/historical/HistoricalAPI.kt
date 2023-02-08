package com.example.boersenapp.api.historical

import com.example.boersenapp.api.historical.dataclass.Historical
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.util.*


interface HistoricalAPI {

    @GET("/v1/eod")
    fun getHistorical(@Query("access_key") access_key: String, @Query("symbols")symbols: String, @Query("date_from")date_from: LocalDate, @Query("date_to")date_to: LocalDate): Call<Historical>
}