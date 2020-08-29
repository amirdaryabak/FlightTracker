package com.sedeghzare.flighttracker.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sedeghzare.flighttracker.R
import com.sedeghzare.flighttracker.models.IncomingFlight
import com.sedeghzare.flighttracker.models.OutgoingFlight
import kotlinx.android.synthetic.main.search_item.view.*


class SearchAdapter(
    private val isIncomingFlight: Boolean,
    private val trackedFlights: List<String>
) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    var DURATION: Long = 300
    private var on_attach = true

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<IncomingFlight>() {
        override fun areItemsTheSame(
            oldItem: IncomingFlight,
            newItem: IncomingFlight
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(
            oldItem: IncomingFlight,
            newItem: IncomingFlight
        ): Boolean {
            TODO("Not yet implemented")
        }
    }


    val differ = AsyncListDiffer(this, differCallback)

    private val differCallback2 = object : DiffUtil.ItemCallback<OutgoingFlight>() {
        override fun areItemsTheSame(
            oldItem: OutgoingFlight,
            newItem: OutgoingFlight
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(
            oldItem: OutgoingFlight,
            newItem: OutgoingFlight
        ): Boolean {
            TODO("Not yet implemented")
        }
    }


    val differ2 = AsyncListDiffer(this, differCallback2)

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
        return if (isIncomingFlight) {
            differ.currentList.size
        } else {
            differ2.currentList.size
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (isIncomingFlight) {
//            setAnimation(holder.itemView, position)
            fromLeftToRight(holder.itemView, position)
//            fromRightToLeft(holder.itemView, position)

            val currentItem = differ.currentList[position]
            holder.itemView.apply {
                txt_departure_time.text = currentItem.scheduledTime
                txt_airline.text = currentItem.airline
                txt_flightNumber.text = currentItem.flightNumber
                txt_origin.text = currentItem.origin
                txt_aircraft.text = currentItem.aircraft
                txt_destination.text = currentItem.destination
                btn_track.setOnClickListener {
                    onItemClickListenerIncomingFlight?.let { it(currentItem) }
                }
            }
        } else {
//            setAnimation(holder.itemView, position)
            fromLeftToRight(holder.itemView, position)
//            fromRightToLeft(holder.itemView, position)

            val currentItem = differ2.currentList[position]
            holder.itemView.apply {
                txt_departure_time.text = currentItem.scheduledTime
                txt_airline.text = currentItem.airline
                txt_flightNumber.text = currentItem.flightNumber
                txt_origin.text = currentItem.origin
                txt_aircraft.text = currentItem.aircraft
                txt_destination.text = currentItem.destination
                btn_track.setOnClickListener {
                    onItemClickListenerOutgoingFlight?.let { it(currentItem) }
                }
            }
        }


    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                Log.d("TAG", "onScrollStateChanged: Called $newState")
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    private fun setAnimation(itemView: View, i: Int) {
        var i = i
        if (!on_attach) {
            i = -1
        }
        val isNotFirstItem = i == -1
        i++
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animator.startDelay = if (isNotFirstItem) DURATION / 2 else i * DURATION / 3
        animator.duration = 500
        animatorSet.play(animator)
        animator.start()
    }

    private fun fromLeftToRight(itemView: View, i: Int) {
        var i = i
        if (!on_attach) {
            i = -1
        }
        val not_first_item = i == -1
        i += 1
        itemView.translationX = -400f
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateY = ObjectAnimator.ofFloat(itemView, "translationX", -400f, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animatorTranslateY.startDelay = if (not_first_item) DURATION else i * DURATION
        animatorTranslateY.duration = (if (not_first_item) 2 else 1) * DURATION
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }

    private fun fromRightToLeft(itemView: View, i: Int) {
        var i = i
        if (!on_attach) {
            i = -1
        }
        val not_first_item = i == -1
        i += 1
        itemView.translationX = itemView.x + 400
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateY =
            ObjectAnimator.ofFloat(itemView, "translationX", itemView.x + 400, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animatorTranslateY.startDelay = if (not_first_item) DURATION else i * DURATION
        animatorTranslateY.duration = (if (not_first_item) 2 else 1) * DURATION
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }

    private var onItemClickListenerIncomingFlight: ((IncomingFlight) -> Unit)? = null

    fun setOnItemClickListenerIncomingFlight(listener: (IncomingFlight) -> Unit) {
        onItemClickListenerIncomingFlight = listener
    }

    private var onItemClickListenerOutgoingFlight: ((OutgoingFlight) -> Unit)? = null

    fun setOnItemClickListenerOutgoingFlight(listener: (OutgoingFlight) -> Unit) {
        onItemClickListenerOutgoingFlight = listener
    }
}