package com.example.boersenapp.api.tickers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    //val baseUrl = "https://api.polygon.io"

    fun getInstance(baseurl:String): Retrofit {
        return Retrofit.Builder().baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}