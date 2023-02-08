package com.example.boersenapp.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import androidx.databinding.adapters.SearchViewBindingAdapter.OnQueryTextChange
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.boersenapp.R
import com.example.boersenapp.api.tickers.RetrofitHelper
import com.example.boersenapp.api.tickers.TickersAPI
import com.example.boersenapp.api.tickers.dataclass.Tickers
import com.example.boersenapp.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        getDataTickers()

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root



/*
        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun getDataTickers(){

        val tickerAPi = RetrofitHelper.getInstance().create(TickersAPI::class.java)
        val accesskey ="a1626f7c013a2fbc9d4431d2fb8b68b1"
        val limit = 20
        val call: Call<Tickers> = tickerAPi.getTickers(accesskey, limit)
        call.enqueue(object : Callback<Tickers?> {

            override fun onResponse(
                call: Call<Tickers?>,
                response: Response<Tickers?>
            )
            {
                if (response.isSuccessful()) {
                    println("Success")
                    println(response.body()?.data?.javaClass?.name)
                    println(response.body()?.data?.get(1)?.toString())

                    //val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

                    val recyclerview: RecyclerView = binding.recyclerview
                    recyclerview.layoutManager = LinearLayoutManager(activity!!) as LayoutManager
                    val data = ArrayList<TickersItemsViewModel>()
                    for (i in 0 until limit ){
                        response.body()?.data?.get(i)?.name?.let { TickersItemsViewModel(it) }
                            ?.let { data.add(it) }
                    }
                    val adapter = CustomAdapter(data)

                    recyclerview.adapter = adapter

                }
            }

            override fun onFailure(call: Call<Tickers?>, t: Throwable) {
                println("Error with Tickers API")

            }
        }
        )


    }
}