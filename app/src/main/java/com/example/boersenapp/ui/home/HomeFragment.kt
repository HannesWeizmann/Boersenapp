package com.example.boersenapp.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
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
import com.example.boersenapp.DetailsActivity
import com.example.boersenapp.R
import com.example.boersenapp.api.historical.HistoricalAPI
import com.example.boersenapp.api.historical.dataclass.Historical
import com.example.boersenapp.api.tickers.RetrofitHelper
import com.example.boersenapp.api.tickers.TickersAPI
import com.example.boersenapp.api.tickers.dataclass.Tickers
import com.example.boersenapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.Date

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

        getDataTickers("d3736ae785ca29411942b3774a34147a", 1000)
        val date1 = LocalDate.parse("2023-01-30")
        val date2 = LocalDate.parse("2023-02-05")
        getDataHistorical("a1626f7c013a2fbc9d4431d2fb8b68b1", "AAPL", date1, date2)

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


    private fun getDataTickers(key:String, limit: Int) {

        val tickerAPi = RetrofitHelper.getInstance().create(TickersAPI::class.java)
        val call: Call<Tickers> = tickerAPi.getTickers(key, limit)
        call.enqueue(object : Callback<Tickers?> {

            override fun onResponse(
                call: Call<Tickers?>,
                response: Response<Tickers?>
            ) {
                if (response.isSuccessful()) {
                    println("Success")
                    println(response.body()?.data?.javaClass?.name)
                    println(response.body()?.data?.get(1)?.toString())

                    //val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

                    val recyclerview: RecyclerView = binding.recyclerview
                    recyclerview.layoutManager = LinearLayoutManager(activity!!) as LayoutManager
                    val data = ArrayList<TickersItemsViewModel>()
                    for (i in 0 until limit) {
                        response.body()?.data?.get(i)?.name?.let { TickersItemsViewModel(it) }
                            ?.let { data.add(it) }
                    }
                    val adapter = CustomAdapter(data)

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


    private fun getDataHistorical(key:String, symbol: String, date_from:LocalDate, date_to:LocalDate){

        val historicalAPI = RetrofitHelper.getInstance().create(HistoricalAPI::class.java)
        val call : Call<Historical> = historicalAPI.getHistorical(key,symbol,date_from,date_to)
        call.enqueue(object: Callback<Historical>{

            override fun onResponse (
                call: Call<Historical?>,
                response: Response<Historical?>
            ){
                println("hoffentlich lese ich das gleiche im Logcat")
                println(response.body()?.toString())
            }

            override fun onFailure(call: Call<Historical>, t: Throwable) {
                println("Error with Historical api")
            }
        })

    }


}