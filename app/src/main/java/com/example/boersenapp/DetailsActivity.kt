package com.example.boersenapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.boersenapp.ui.home.TickersItemsViewModel

class DetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val ItemsViewModel = intent.getParcelableExtra<TickersItemsViewModel>("Aktie")
        if(ItemsViewModel != null){
            val textView : TextView = findViewById(R.id.Aktienname)

            textView.text = ItemsViewModel.text
        }

    }
}