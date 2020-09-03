package com.amirdaryabak.flighttracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amirdaryabak.flighttracker.R
import com.amirdaryabak.flighttracker.models.SearchByFlightNumberAndRoute
import kotlinx.android.synthetic.main.search_item.view.*

class SearchByRouteAdapter(val type: Int) : RecyclerView.Adapter<SearchByRouteAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<SearchByFlightNumberAndRoute>() {
        override fun areItemsTheSame(
            oldItem: SearchByFlightNumberAndRoute,
            newItem: SearchByFlightNumberAndRoute
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(
            oldItem: SearchByFlightNumberAndRoute,
            newItem: SearchByFlightNumberAndRoute
        ): Boolean {
            TODO("Not yet implemented")
        }
    }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.itemView.apply {
            txt_departure_time.text = currentItem.arrivalScheduledTime
            txt_airline.text = currentItem.airline
            txt_flightNumber.text = currentItem.flightNumber
            txt_origin.text = currentItem.origin
            txt_aircraft.text = currentItem.aircraft
            txt_destination.text = currentItem.destination
            if (type == 2) {
                btn_track.visibility = View.GONE
            }
            btn_track.setOnClickListener {
                onItemClickListenerIncomingFlight?.let { it(currentItem) }
            }
        }

    }

    private var onItemClickListenerIncomingFlight: ((SearchByFlightNumberAndRoute) -> Unit)? = null

    fun setOnItemClickListenerIncomingFlight(listener: (SearchByFlightNumberAndRoute) -> Unit) {
        onItemClickListenerIncomingFlight = listener
    }

}