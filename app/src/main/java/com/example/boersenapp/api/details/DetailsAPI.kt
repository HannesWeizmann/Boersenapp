package com.example.boersenapp.api.details

import com.example.boersenapp.api.details.dataclass.Details
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailsAPI {

    @GET("/v3/reference/tickers/{ticker}")
    fun getDetails(@Path("ticker") ticker:String, @Query("apiKey")apiKey:String): Call<Details>
}