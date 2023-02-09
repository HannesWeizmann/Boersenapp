package com.example.boersenapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.boersenapp.api.tickers.RetrofitHelper
import com.example.boersenapp.api.tickers.TickersAPI
import com.example.boersenapp.api.tickers.dataclass.Tickers
import com.example.boersenapp.databinding.ActivityMainBinding
import com.example.boersenapp.ui.home.CustomAdapter
import com.example.boersenapp.ui.home.TickersItemsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var customAdapter: CustomAdapter
    lateinit var mList: List<TickersItemsViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // on below line we are initializing our list
        mList = ArrayList()

        // on below line we are initializing our adapter
        customAdapter = CustomAdapter(mList)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customAdapter.notifyDataSetChanged()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favoriten, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // calling on create option menu
    // layout to inflate our menu file.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // below line is to get our inflater
        val inflater = menuInflater

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu)

        // below line is to get our menu item.
        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)

        // getting search view of our item.
        val searchView: SearchView = searchItem.getActionView() as SearchView

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(msg)
                return false
            }
        })
        return true
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: List<TickersItemsViewModel> = ArrayList()

        // running a for loop to compare elements.
        for (item in mList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.text.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.


                //unerklärlicher fehler
                //filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            customAdapter.filterList(filteredlist)
        }
    }
}