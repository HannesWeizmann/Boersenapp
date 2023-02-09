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

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_card_design_view, parent, false)

        return ViewHolder(view, mListener)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.text
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(ItemView) {

        val textView: TextView = itemView.findViewById(R.id.textView)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
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

