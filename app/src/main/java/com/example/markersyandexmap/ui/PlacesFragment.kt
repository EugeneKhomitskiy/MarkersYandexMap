package com.example.markersyandexmap.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.markersyandexmap.R
import com.example.markersyandexmap.adapter.PlaceAdapter
import com.example.markersyandexmap.adapter.PlaceCallback
import com.example.markersyandexmap.databinding.FragmentPlacesBinding
import com.example.markersyandexmap.dto.Place
import com.example.markersyandexmap.viewmodel.PlaceViewModel
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlacesFragment : Fragment() {

    val placeViewModel: PlaceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPlacesBinding = FragmentPlacesBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PlaceAdapter(object : PlaceCallback {

            override fun onEdit(place: Place) {
                val bundle = Bundle().apply {
                    putString("title", place.title)
                    putString("description", place.description)
                }
                placeViewModel.edit(place)
                findNavController().navigate(
                    R.id.action_placesFragment_to_newPlaceFragment,
                    bundle
                )
            }

            override fun onRemove(place: Place) {
                placeViewModel.removeById(place.id)
            }

            override fun openMap(place: Place) {
                placeViewModel.savePosition(Point(place.latitude, place.longitude))
                findNavController().navigate(R.id.action_placesFragment_to_mapFragment)
            }
        })
        binding.list.adapter = adapter
        binding.list.itemAnimator = null

        placeViewModel.data.observe(viewLifecycleOwner) { places ->
            adapter.submitList(places)
            binding.emptyText.isVisible = places.isEmpty()
        }

        placeViewModel.edited.observe(viewLifecycleOwner) { place ->
            if (place.id == 0) {
                return@observe
            }
        }

        return binding.root
    }
}
