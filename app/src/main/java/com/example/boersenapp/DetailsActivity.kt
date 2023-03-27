package com.example.boersenapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import com.example.boersenapp.api.details.DetailsAPI
import com.example.boersenapp.api.details.dataclass.Details
import com.example.boersenapp.api.historical.HistoricalAPI
import com.example.boersenapp.api.historical.dataclass.Historical
import com.example.boersenapp.api.news.NewsAPI
import com.example.boersenapp.api.news.dataclass.News
import com.example.boersenapp.api.tickers.RetrofitHelper
import com.example.boersenapp.ui.home.TickersItemsViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Period
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DetailsActivity : AppCompatActivity() {

    private companion object{
        private const val CHANNEL_ID = "channel01"
    }
    private val stockNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Drehung des Bildschirms
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR

        //Allgemeine Variablen für den Kursalarm
        val number1 = findViewById<EditText>(R.id.editTextNumber2)
        val number2 = findViewById<EditText>(R.id.editTextNumber3)
        val beschreibung5 = findViewById<TextView>(R.id.Beschreibung5)
        val beschreibung6 = findViewById<TextView>(R.id.Beschreibung6)
        val speichern = findViewById<Button>(R.id.Speichern)
        val loeschen = findViewById<Button>(R.id.Löschen)


        //Übergabe der Aktieninfos an Detailansicht
        val ItemsViewModel = intent.getParcelableExtra<TickersItemsViewModel>("Aktie")
        val key_marketstack = resources.getString(R.string.key_marketstack)
        val checkBox = findViewById<CheckBox>(R.id.Kursalarm)
        if (ItemsViewModel != null) {
            val textView: TextView = findViewById(R.id.Aktienname)
            val kürzel: TextView = findViewById(R.id.kürzel)
            val exchange: TextView = findViewById(R.id.exchange)
            textView.text = ItemsViewModel.name
            kürzel.text = ItemsViewModel.ticker

            //Die gespeicherten Werte des Aktienalarms eintragen
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            val stockName = textView.text.toString()
            val alarm_enabled_key = "alarm_enabled_$stockName"
            val is_alarm_enabled = sharedPref.getBoolean(alarm_enabled_key, false)
            val checkBox = findViewById<CheckBox>(R.id.Kursalarm)
            val upper_limit_key = "limit_upper_$stockName"
            val lower_limit_key = "limit_lower_$stockName"
            val UpperLimit = sharedPref.getInt(upper_limit_key, 0)
            val LowerLimit = sharedPref.getInt(lower_limit_key, 0)
            number1.setText(UpperLimit.toString())
            number2.setText(LowerLimit.toString())

            checkBox.isChecked = is_alarm_enabled

            getNews(ItemsViewModel.ticker, 2)
            //getDetails(ItemsViewModel.ticker)

        }

        //Variablen für den Aktienchart
        val today = LocalDate.now()
        val week = Period.of(0, 0, 7)
        val moth = Period.of(0, 1, 0)
        val year = Period.of(1, 0, 0)
        var dayinpast = today.minus(week)

        val date_ranges = resources.getStringArray(R.array.date_ranges)
        val spinner: Spinner = findViewById(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, date_ranges)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    parent: View?,
                    position: Int,
                    id: Long
                ) {
                    println(date_ranges[position])
                    if (position == 0) {
                        dayinpast = today.minus(week)
                    } else if (position == 1) {
                        dayinpast = today.minus(moth)
                    } else {
                        dayinpast = today.minus(year)
                    }
                    if (ItemsViewModel != null) {
                        println(ItemsViewModel.ticker)
                        getDataHistorical(key_marketstack, ItemsViewModel.ticker, dayinpast, today)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }

        //Funktion zum Enable/Disable der Ober- und Untergrenze Eingabe
        if (checkBox.isChecked == true){
            number1.isEnabled = true
            number2.isEnabled = true
            beschreibung5.isEnabled = true
            beschreibung6.isEnabled = true
            speichern.isEnabled = true
            loeschen.isEnabled = true
        }

        //Funktion für die CheckBox des Kursalarms
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            number1.isEnabled = isChecked
            number2.isEnabled = isChecked
            beschreibung5.isEnabled = isChecked
            beschreibung6.isEnabled = isChecked
            speichern.isEnabled = isChecked
            loeschen.isEnabled = isChecked

            if(!isChecked) {
                val stockName = findViewById<TextView>(R.id.Aktienname).text.toString()
                deletedata(stockName)
            }
        }

        //Handler (Loop) um Kursalarm alle 10min zu überprüfen
        val handler = Handler()
        val delay = 10 * 60 * 1000  //Alle 10 Minuten

        handler.postDelayed(object : Runnable {
            override fun run() {
                compareData(120)
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())

        //Funktionen für Speichern und Löschen Button
        var savebutton = findViewById<Button>(R.id.Speichern)
        savebutton.setOnClickListener {
            savedata()
        }
        var deletebutton = findViewById<Button>(R.id.Löschen)
        deletebutton.setOnClickListener {
            val stockName = findViewById<TextView>(R.id.Aktienname).text.toString()
            deletedata(stockName)
        }

    }

    //Funktion zum holen der historischen Daten zum erstellen des Aktiencharts
    private fun getDataHistorical(
        key: String,
        symbol: String,
        date_from: LocalDate,
        date_to: LocalDate
    ) {

        val baseurl_marketstack = resources.getString(R.string.baseurl_marketstack)
        val preis: TextView = findViewById(R.id.preis)
        val historicalAPI = RetrofitHelper.getInstance(baseurl_marketstack).create(
            HistoricalAPI::class.java
        )
        val call: Call<Historical> = historicalAPI.getHistorical(key, symbol, date_from, date_to)
        call.enqueue(object : Callback<Historical> {

            override fun onResponse(
                call: Call<Historical?>,
                response: Response<Historical?>
            ) {
                println("response")
                println(response.body())
                val entries = ArrayList<Entry>()
                val length = response.body()?.data?.size!! - 1


                if (length >= 0) {
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
                    val primcolor: Int = resources.getColor(R.color.green)

                    val vl = LineDataSet(entries, "Closing Values")
                    vl.setDrawValues(false)
                    vl.setDrawFilled(true)
                    vl.lineWidth = 3f
                    vl.fillColor = primcolor
                    vl.fillAlpha = R.color.othergreen
                    lineChart.xAxis.labelRotationAngle = 0f
                    lineChart.data = LineData(vl)
                    lineChart.axisRight.isEnabled = false
                    lineChart.xAxis.isEnabled = false
                    lineChart.setTouchEnabled(true)
                    lineChart.setPinchZoom(true)
                    lineChart.setNoDataText("No Data")
                    lineChart.animateX(1800, Easing.EaseInExpo)

                } else {
                    preis.text = "No current Data"
                    Toast.makeText(this@DetailsActivity, "No current Data", 5)

                }


            }

            override fun onFailure(call: Call<Historical>, t: Throwable) {
                println("Error with Historical api")
            }
        })

    }

    fun getDetails(ticker: String) {
        val baseurl_details = resources.getString(R.string.baseurl_polygon)
        val key_polygon = resources.getString(R.string.key_polygon)
        val detailsAPI = RetrofitHelper.getInstance(baseurl_details).create(
            DetailsAPI::class.java
        )
        val call: Call<Details> = detailsAPI.getDetails("MSFT", key_polygon)
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


    fun getNews(ticker: String, limit: Int) {

        val baseurl_details = resources.getString(R.string.baseurl_polygon)
        val key_polygon = resources.getString(R.string.key_polygon)
        val newsAPI = RetrofitHelper.getInstance(baseurl_details).create(
            NewsAPI::class.java
        )
        val call: Call<News> = newsAPI.getNews(key_polygon, limit)
        call.enqueue(object : Callback<News?> {

            override fun onResponse(
                call: Call<News?>,
                response: Response<News?>
            ) {
                if (response.isSuccessful()) {
                    val newsList = response.body()?.results
                    if (newsList != null && newsList.isNotEmpty()) {
                        // Extrahiere den ersten Artikel aus der Liste.
                        val article = newsList[0]
                        val title = article.title
                        val description = article.description

                        // Gib den Titel und die Beschreibung in separate Textfelder aus.
                        runOnUiThread {
                            findViewById<TextView>(R.id.titleTextView).text = title
                            findViewById<TextView>(R.id.descriptionTextView).text = description
                        }
                    }
                }
            }

            override fun onFailure(call: Call<News?>, t: Throwable) {
                println("Error with Details API")
            }
        }
        )

    }

    //Funktion um eine Benachrichtigung für den Kursalarm zu erstellen
    fun shownotification() {
        createnotificationchannel()

        val date = Date()
        val notificationid = SimpleDateFormat("ddHHmmss", Locale.GERMAN).format(date).toInt()

        val mainIntent = Intent(this, DetailsActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val mainPendingIntent = PendingIntent.getActivity(this, 1 ,mainIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, "$CHANNEL_ID")
        notificationBuilder.setContentTitle("Kursalarm!")
        notificationBuilder.setContentText("Eine Aktie hat einen von Ihnen definierten Wert überschritten!")
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_name)
        notificationBuilder.priority = NotificationCompat.PRIORITY_MAX
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setContentIntent(mainPendingIntent)

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(notificationid, notificationBuilder.build())

    }
    private fun createnotificationchannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "MyNotification"
            val description = "My notification channel description"

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel("$CHANNEL_ID", name, importance)
            notificationChannel.description = description
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    //Funktionen zum Speichern und Löschen der angegebenen Grenzwerte
    fun savedata(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val obergrenze = findViewById<EditText>(R.id.editTextNumber2).text.toString().toIntOrNull()?:0
        val untergrenze = findViewById<EditText>(R.id.editTextNumber3).text.toString().toIntOrNull()?:0
        val aktienname = findViewById<TextView>(R.id.Aktienname)
        val stockName = aktienname.text.toString()
        val checkbox = findViewById<CheckBox>(R.id.Kursalarm)
        val upper_limit_key = "limit_upper_$stockName"
        val lower_limit_key = "limit_lower_$stockName"
        val alarm_enabled_key = "alarm_enabled_$stockName"

        editor.putBoolean(alarm_enabled_key, checkbox.isChecked)
        editor.putInt(upper_limit_key, obergrenze)
        editor.putInt(lower_limit_key, untergrenze)
        editor.apply()

        stockNames.add(stockName)
    }

    fun deletedata(stockName: String) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val upperLimitKey = "limit_upper_$stockName"
        val lowerLimitKey = "limit_lower_$stockName"
        val alarmEnabledKey = "alarm_enabled_$stockName"

        editor.remove(upperLimitKey)
        editor.remove(lowerLimitKey)
        editor.remove(alarmEnabledKey)
        editor.apply()

        stockNames.remove(stockName)

        findViewById<EditText>(R.id.editTextNumber2).setText("0")
        findViewById<EditText>(R.id.editTextNumber3).setText("0")
    }

    //Funktion zum Vergleichen des aktuellen Wertes und den gesetzten Grenzen
    fun compareData(value: Int) {
        for (stockName in stockNames) {
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            val upperLimitKey = "limit_upper_$stockName"
            val lowerLimitKey = "limit_lower_$stockName"
            val alarmEnabledKey = "alarm_enabled_$stockName"
            val upperLimit = sharedPref.getInt(upperLimitKey, 0)
            val lowerLimit = sharedPref.getInt(lowerLimitKey, 0)
            val alarmEnabled = sharedPref.getBoolean(alarmEnabledKey, false)

            if (value >= upperLimit && alarmEnabled) {
                shownotification()
                Log.d("MyApp", "Hallo++ for $stockName")
            } else if (value <= lowerLimit && alarmEnabled) {
                shownotification()
                Log.d("MyApp", "Test-- for $stockName")
            }
        }
    }

}


