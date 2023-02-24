package com.example.boersenapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.boersenapp.DetailsActivity
import com.example.boersenapp.R
import com.example.boersenapp.api.tickers.RetrofitHelper
import com.example.boersenapp.api.tickers.TickersAPI
import com.example.boersenapp.api.tickers.dataclass.Tickers
import com.example.boersenapp.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var data = ArrayList<TickersItemsViewModel>()
val adapter = CustomAdapter(data)

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val key_marketstack = resources.getString(R.string.key_marketstack)
        //val exchange = resources.getString(R.string.exchange)
        getDataTickers(key_marketstack, 100)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //holt die Aktien aus der API mit Angabe einer Anzahl wie viele es sein sollen
    private fun getDataTickers(key:String, limit: Int) {
        val baseurl_marketstack = resources.getString(R.string.baseurl_marketstack)
        val tickerapi = RetrofitHelper.getInstance(baseurl_marketstack).create(TickersAPI::class.java)
        val call: Call<Tickers> = tickerapi.getTickers(key, limit)
        call.enqueue(object : Callback<Tickers?> {

            override fun onResponse(
                call: Call<Tickers?>,
                response: Response<Tickers?>
            ) {
                if (response.isSuccessful()) {
                    val recyclerview: RecyclerView = binding.recyclerview
                    recyclerview.layoutManager = LinearLayoutManager(activity!!) as LayoutManager
                    for (i in 0 until limit) {
                        val name = response.body()?.data?.get(i)?.name
                        val ticker = response.body()?.data?.get(i)?.symbol
                        if(name != null && ticker != null){
                            val item = TickersItemsViewModel(ticker, name)
                            data.add(item)
                        }
                    }

                    recyclerview.adapter = adapter

                    //übergibt den aktiennamen der Detailansicht
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