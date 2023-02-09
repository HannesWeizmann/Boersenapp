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

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_card_design_view, parent, false)

        return ViewHolder(view)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.text

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(ItemsViewModel)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val textView: TextView = itemView.findViewById(R.id.textView)

    }

    // method for filtering our recyclerview items.
    fun filterList(filterlist: List<TickersItemsViewModel>) {
        // below line is to add our filtered
        // list in our course array list.
        mList = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

}

