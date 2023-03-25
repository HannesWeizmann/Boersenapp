package com.example.boersenapp

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.boersenapp.api.details.dataclass.Details
import com.example.boersenapp.api.tickers.RetrofitHelper
import com.example.boersenapp.api.tickers.TickersAPI
import com.example.boersenapp.databinding.ActivityMainBinding
import com.example.boersenapp.ui.home.CustomAdapter
import com.example.boersenapp.ui.home.TickersItemsViewModel
import com.example.boersenapp.ui.home.adapter
import com.example.boersenapp.ui.home.data
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import com.example.boersenapp.DetailsActivity



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Drehung des Bildschirms
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR



        //Prüft die internet Verbindung
        if (checkForInternet(this)) {
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_LONG).show()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favoriten
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    // aufrufen von oncreateoptionmenu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Die untere Zeile ist, um unseren Inflater zu bekommen
        val inflater = menuInflater

        inflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)

        // Suchansicht unseres Artikels erhalten
        val searchView: SearchView = searchItem.getActionView() as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {

                // Aufruf einer Methode zum Filtern unserer Recyclerview
                filter(msg)
                return false
            }
        })
        return true
    }

    //Methode zum Filtern der Recyclerview
    private fun filter(text: String) {
        // Erstellen einer neuen Array-Liste zum Filtern unserer Daten
        val filteredlist: ArrayList<TickersItemsViewModel> = ArrayList()

        //Ausführen einer for-Schleife zum Vergleichen von Elementen
        for (item in data) {

            if (item.name.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))) {
                //Wenn der Artikel zusammenpasst, fügen wir ihn zu unseren gefilterten liste hinzu
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // kein Artikel in der gefilterten Liste hinzugefügt wird, wird ein toas-nachricht angezeigt
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            //zum Schluss wird die gefilterte liste dem adapter übergeben
            adapter.filterList(filteredlist)
        }
    }

    //Funktion zum Überprüfen ob eine Internet Verbindung vorhanden ist
    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}