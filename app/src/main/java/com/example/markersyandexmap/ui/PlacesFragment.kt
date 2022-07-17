package com.example.markersyandexmap.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.markersyandexmap.R
import com.example.markersyandexmap.adapter.PlaceAdapter
import com.example.markersyandexmap.adapter.PlaceCallback
import com.example.markersyandexmap.databinding.FragmentPlacesBinding
import com.example.markersyandexmap.dto.Place
import com.example.markersyandexmap.viewmodel.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesFragment : Fragment() {

    val placeViewModel: PlaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        menu.clear()
    }

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
                placeViewModel.edit(place)
                val bundle = Bundle().apply {
                    putString("title", place.title)
                    putString("description", place.description)
                }
                findNavController().navigate(
                    R.id.action_placesFragment_to_newPlaceFragment,
                    bundle
                )
            }

            override fun onRemove(place: Place) {
                placeViewModel.removeById(place.id)
            }

            override fun openMap(place: Place) {
                val bundle = Bundle().apply {
                    putDouble("lat", place.latitude)
                    putDouble("lng", place.longitude)
                }
                findNavController().navigate(R.id.action_placesFragment_to_mapFragment, bundle)
            }
        })
        binding.list.adapter = adapter
        binding.list.itemAnimator = null

        placeViewModel.data.observe(viewLifecycleOwner) { places ->
            adapter.submitList(places)
        }

        placeViewModel.edited.observe(viewLifecycleOwner) { place ->
            if (place.id == 0) {
                return@observe
            }
        }

        return binding.root
    }
}