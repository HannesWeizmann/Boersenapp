package com.example.boersenapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.nav_bar)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        // Set default fragment
        val fragment = HomeFragment()
        openFragment(fragment)
    }

    private val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                val fragment = HomeFragment()
                openFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_portfolio -> {
                val fragment = PortfolioFragment()
                openFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_settings -> {
                val fragment = SettingsFragment()
                openFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_bar, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
