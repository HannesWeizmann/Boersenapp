package com.example.boersenapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boersenapp.DetailsActivity
import com.example.boersenapp.R

class CustomAdapter(private var mList: List<TickersItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    var onItemClick : ((TickersItemsViewModel) ->Unit)? = null

    // erstellt neue Views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //erstellt die card_view_design-Ansicht auf, die verwendet, um das Listenelement zu halten
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_card_design_view, parent, false)

        return ViewHolder(view)
    }


    // bindet die Listenelemente an eine view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        holder.textView.text = ItemsViewModel.name

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(ItemsViewModel)
        }
    }


    // gibt die Anzahl der Elemente in der Liste zurück
    override fun getItemCount(): Int {
        return mList.size
    }

    // Enthält die Ansichten zum Hinzufügen zu Text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val textView: TextView = itemView.findViewById(R.id.textView)

    }

    // Methode zum Filtern unserer Recyclerview-items
    fun filterList(filterlist: List<TickersItemsViewModel>) {

        mList = filterlist

        notifyDataSetChanged()
    }

}

