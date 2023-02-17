package com.example.boersenapp.ui.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.boersenapp.DetailsActivity
import com.example.boersenapp.api.tickers.RetrofitHelper
import com.example.boersenapp.api.tickers.TickersAPI
import com.example.boersenapp.api.tickers.dataclass1.Tickers
import com.example.boersenapp.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

var data = ArrayList<TickersItemsViewModel>()
val adapter = CustomAdapter(data)

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        getDataTickers("tvrXanzn0M7kpFFyZLJKn4rTAG4rQJGv", 10)
        val date1 = LocalDate.parse("2023-02-06")
        val date2 = LocalDate.parse("2023-02-10")
        //getDataHistorical("a1626f7c013a2fbc9d4431d2fb8b68b1", "AAPL", date1, date2)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun getDataTickers(key:String, limit: Int) {

        val tickerapi = RetrofitHelper.getInstance("https://api.polygon.io").create(TickersAPI::class.java)
        val call: Call<Tickers> = tickerapi.getTickers(true, limit, key)
        call.enqueue(object : Callback<Tickers?> {

            override fun onResponse(
                call: Call<Tickers?>,
                response: Response<Tickers?>
            ) {
                if (response.isSuccessful()) {
                    println("Success")
                    println(response.body()?.results?.get(1)?.ticker)

                    val recyclerview: RecyclerView = binding.recyclerview
                    recyclerview.layoutManager = LinearLayoutManager(activity!!) as LayoutManager
                    for (i in 0 until limit) {
                        val name = response.body()?.results?.get(i)?.name
                        val ticker = response.body()?.results?.get(i)?.ticker
                        if(name != null && ticker != null){
                            val item = TickersItemsViewModel(ticker, name)
                            data.add(item)
                        }

                    }

                    recyclerview.adapter = adapter

                    adapter.onItemClick = {
                        val intent = Intent(requireContext(), DetailsActivity::class.java)
                        intent.putExtra("Aktie", it)
                        startActivity(intent)
                    }

                }
            }
            override fun onFailure(call: Call<Tickers?>, t: Throwable) {
                println("Error with Tickers API")

            }
        }
        )
    }
}