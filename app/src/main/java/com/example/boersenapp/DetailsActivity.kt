package com.example.boersenapp

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.boersenapp.api.historical.HistoricalAPI
import com.example.boersenapp.api.historical.dataclass.Historical
import com.example.boersenapp.api.tickers.RetrofitHelper
import com.example.boersenapp.ui.home.TickersItemsViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class DetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Drehung des Bildschirms
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR



        val ItemsViewModel = intent.getParcelableExtra<TickersItemsViewModel>("Aktie")
        if(ItemsViewModel != null){
            val textView : TextView = findViewById(R.id.Aktienname)
            val exchange : TextView = findViewById(R.id.exchange)
            textView.text = ItemsViewModel.name
        }

        val date1 = LocalDate.parse("2023-02-06")
        val date2 = LocalDate.parse("2023-02-10")
        val key_marketstack = resources.getString(R.string.key_marketstack)
        if (ItemsViewModel != null) {
            getDataHistorical(key_marketstack, ItemsViewModel.ticker, date1, date2)
        }

    }

    private fun getDataHistorical(key:String, symbol: String, date_from: LocalDate, date_to: LocalDate){

        val baseurl_marketstack = resources.getString(R.string.baseurl_marketstack)
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


                for(i in length downTo 0){
                    val index = length - i
                    entries.add(Entry(index.toFloat(), response.body()?.data!![i].close.toFloat()))
                }

                val preis: TextView = findViewById(R.id.preis)
                preis.text = response.body()?.data!![0].close.toString()

                val lineChart: LineChart = findViewById(R.id.lineChart)

                val vl = LineDataSet(entries, "My Type")
                vl.setDrawValues(false)
                vl.setDrawFilled(true)
                vl.lineWidth = 3f
                lineChart.xAxis.labelRotationAngle = 0f
                lineChart.data = LineData(vl)

            }

            override fun onFailure(call: Call<Historical>, t: Throwable) {
                println("Error with Historical api")
            }
        })

    }
}