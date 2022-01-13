package com.example.markersyandexmap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.markersyandexmap.R
import com.example.markersyandexmap.databinding.PlaceBinding
import com.example.markersyandexmap.dto.Place

interface PlaceCallback {
    fun onEdit(place: Place)
    fun onRemove(place: Place)
    fun openMap(place: Place)
}

class PlaceAdapter(private val placeCallback: PlaceCallback) :
    ListAdapter<Place, PlaceViewHolder>(PlaceDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = PlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding, placeCallback)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }
}

class PlaceViewHolder(
    private val binding: PlaceBinding,
    private val placeCallback: PlaceCallback
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(place: Place) {
        binding.apply {
            title.text = place.title
            description.text = place.description

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.place_options)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.place_open -> {
                                placeCallback.openMap(place)
                                true
                            }
                            R.id.place_remove -> {
                                placeCallback.onRemove(place)
                                true
                            }
                            R.id.place_edit -> {
                                placeCallback.onEdit(place)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}