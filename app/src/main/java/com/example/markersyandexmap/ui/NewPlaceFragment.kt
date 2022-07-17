package com.example.markersyandexmap.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.markersyandexmap.R
import com.example.markersyandexmap.databinding.FragmentNewPlaceBinding
import com.example.markersyandexmap.viewmodel.PlaceViewModel
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPlaceFragment: Fragment() {

    private val placeViewModel: PlaceViewModel by activityViewModels()

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
        val binding: FragmentNewPlaceBinding = FragmentNewPlaceBinding.inflate(
            inflater,
            container,
            false
        )

        val lat = requireArguments().getDouble("latitude")
        val lng = requireArguments().getDouble("longitude")
        val title = arguments?.getString("title")
        val description = arguments?.getString("description")

        binding.textTitle.setText(title)
        binding.textDescription.setText(description)

        binding.ok.setOnClickListener {
            placeViewModel.changeTitle(binding.title.editText?.text.toString())
            placeViewModel.changeDescription(binding.description.editText?.text.toString())
            placeViewModel.save(
                lat,
                lng
            )
            findNavController().navigate(R.id.action_newPlaceFragment_to_mapFragment)
        }

        return binding.root
    }
}