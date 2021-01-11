package com.test.findvenues.presentation.venuelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.findvenues.R
import com.test.findvenues.databinding.LayoutVenuesAdapterBinding

internal class VenueListAdapter(private val listener: ClickListener) : RecyclerView.Adapter<VenueListItemViewHolder>() {

    var venues: List<VenueListView> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueListItemViewHolder {
        return VenueListItemViewHolder(LayoutVenuesAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VenueListItemViewHolder, position: Int) {
        holder.bind(venues[position], listener)
    }

    override fun getItemCount(): Int {
        return venues.size
    }

    interface ClickListener {
        fun onVenueItemClicked(venueId: String)
    }
}

internal class VenueListItemViewHolder(private val binding: LayoutVenuesAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(venue: VenueListView, listener: VenueListAdapter.ClickListener) {
        with(binding) {
            title.text = venue.name
            location.text = venue.location ?: root.context.getText(R.string.venue_list_address_not_provided)
            root.setOnClickListener { listener.onVenueItemClicked(venue.id) }
        }
    }
}
