package com.example.boersenapp.api.news

import com.example.boersenapp.api.news.dataclass.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("/v2/reference/news")
    fun getNews(@Query("apiKey")apiKey:String, @Query("limit")limit:Int): Call<News>
}