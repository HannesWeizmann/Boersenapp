package com.example.boersenapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.boersenapp.ui.home.TickersItemsViewModel

class DetailsActivity : AppCompatActivity() {

    private lateinit var Aktienname : TextView
    private lateinit var infos : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        initView()
        setValuestoViews()
    }

    private fun initView(){}

    private fun setValuestoViews()
    {
        //Aktienname.text = intent.getStringExtra("Aktie")
        //infos.text = intent.getStringExtra("")
    }
}