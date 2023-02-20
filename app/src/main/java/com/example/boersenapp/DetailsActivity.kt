package com.example.boersenapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boersenapp.api.details.DetailsAPI
import com.example.boersenapp.api.details.dataclass.Details
import com.example.boersenapp.api.historical.HistoricalAPI
import com.example.boersenapp.api.historical.dataclass.Historical
import com.example.boersenapp.api.news.NewsAPI
import com.example.boersenapp.api.news.dataclass.News
import com.example.boersenapp.api.tickers.RetrofitHelper
import com.example.boersenapp.api.tickers.dataclass.Tickers
import com.example.boersenapp.ui.home.TickersItemsViewModel
import com.example.boersenapp.ui.home.adapter
import com.example.boersenapp.ui.home.data
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.time.LocalDate
import java.time.Period

class DetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Drehung des Bildschirms
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR


        val ItemsViewModel = intent.getParcelableExtra<TickersItemsViewModel>("Aktie")
        val key_marketstack = resources.getString(R.string.key_marketstack)
        if(ItemsViewModel != null){
            val textView : TextView = findViewById(R.id.Aktienname)
            val exchange : TextView = findViewById(R.id.exchange)
            textView.text = ItemsViewModel.name
            getNews(ItemsViewModel.ticker, 2)
            //getDetails(ItemsViewModel.ticker)

        }


        val today = LocalDate.now()
        val week = Period.of(0,0,7)
        val moth  = Period.of(0,1,0)
        val year = Period.of(1,0,0)
        var dayinpast  = today.minus(week)

        val date_ranges = resources.getStringArray(R.array.date_ranges)
        val spinner: Spinner  = findViewById(R.id.spinner)
        if(spinner != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, date_ranges)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, parent: View?, position: Int, id: Long) {
                    println(date_ranges[position])
                    if(position ==0){
                        dayinpast  =today.minus(week)
                    }
                    else if (position == 1){
                        dayinpast = today.minus(moth)
                    }
                    else{
                        dayinpast  = today.minus(year)
                    }
                    if(ItemsViewModel != null){
                        println(ItemsViewModel.ticker)
                        getDataHistorical(key_marketstack, ItemsViewModel.ticker, dayinpast, today)
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
                }
        }

    }

    private fun getDataHistorical(key:String, symbol: String, date_from: LocalDate, date_to: LocalDate){

        val baseurl_marketstack = resources.getString(R.string.baseurl_marketstack)
        val preis: TextView = findViewById(R.id.preis)
        val historicalAPI = RetrofitHelper.getInstance(baseurl_marketstack).create(
            HistoricalAPI::class.java)
        val call : Call<Historical> = historicalAPI.getHistorical(key,symbol,date_from,date_to)
        call.enqueue(object: Callback<Historical> {

            override fun onResponse (
                call: Call<Historical?>,
                response: Response<Historical?>
            ){
                println("response")
                println(response.body())
                val entries = ArrayList<Entry>()
                val length = response.body()?.data?.size!! -1


                if(length >= 0) {
                    for (i in length downTo 0) {
                        val index = length - i
                        entries.add(
                            Entry(
                                index.toFloat(),
                                response.body()?.data!![i].close.toFloat()
                            )
                        )
                    }
                    //preis.text = response.body()?.data!![0].close.toString()
                    preis.text = entries[length].y.toString()

                    val lineChart: LineChart = findViewById(R.id.lineChart)
                    val primcolor: Int = resources.getColor(R.color.purple_500)

                    val vl = LineDataSet(entries, "Closing Values")
                    vl.setDrawValues(false)
                    vl.setDrawFilled(true)
                    vl.lineWidth = 3f
                    vl.fillColor = primcolor
                    lineChart.xAxis.labelRotationAngle = 0f
                    lineChart.data = LineData(vl)
                    lineChart.axisRight.isEnabled = false
                    lineChart.xAxis.isEnabled = false
                    lineChart.setTouchEnabled(true)
                    lineChart.setPinchZoom(true)
                    lineChart.setNoDataText("No Data")
                    lineChart.animateX(1800, Easing.EaseInExpo)

                }
                else{
                    preis.text = "No current Data"
                    Toast.makeText(this@DetailsActivity, "No current Data", 5)

                }


            }

            override fun onFailure(call: Call<Historical>, t: Throwable) {
                println("Error with Historical api")
            }
        })

    }

    fun getDetails(ticker:String){
        val baseurl_details = resources.getString(R.string.baseurl_polygon)
        val key_polygon  = resources.getString(R.string.key_polygon)
        val detailsAPI = RetrofitHelper.getInstance(baseurl_details).create(
        DetailsAPI::class.java)
        val call: Call<Details> = detailsAPI.getDetails("MSFT",key_polygon)
        call.enqueue(object : Callback<Details?> {

            override fun onResponse(
                call: Call<Details?>,
                response: Response<Details?>
            ) {
                if (response.isSuccessful()) {
                    println(response.body())
                }
            }
            override fun onFailure(call: Call<Details?>, t: Throwable) {
                println("Error with Details API")
            }
        }
        )

    }
    fun getNews(ticker:String, limit: Int ){

        val baseurl_details = resources.getString(R.string.baseurl_polygon)
        val key_polygon  = resources.getString(R.string.key_polygon)
        val newsAPI = RetrofitHelper.getInstance(baseurl_details).create(
            NewsAPI::class.java)
        val call: Call<News> = newsAPI.getNews(key_polygon,limit)
        call.enqueue(object : Callback<News?> {

            override fun onResponse(
                call: Call<News?>,
                response: Response<News?>
            ) {
                if (response.isSuccessful()) {
                    println(response.body())
                }
            }
            override fun onFailure(call: Call<News?>, t: Throwable) {
                println("Error with Details API")
            }
        }
        )

    }
}