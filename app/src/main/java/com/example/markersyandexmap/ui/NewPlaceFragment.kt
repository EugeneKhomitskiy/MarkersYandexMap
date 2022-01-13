package com.example.markersyandexmap.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.markersyandexmap.R
import com.example.markersyandexmap.databinding.FragmentNewPlaceBinding
import com.example.markersyandexmap.viewmodel.PlaceViewModel

class NewPlaceFragment: Fragment() {

    private val placeViewModel: PlaceViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

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

        binding.title.setText(arguments?.getString("title"))
        binding.description.setText(arguments?.getString("description"))

        binding.ok.setOnClickListener {
            placeViewModel.changeTitle(binding.title.text.toString())
            placeViewModel.changeDescription(binding.description.text.toString())
            placeViewModel.save(
                requireArguments().getDouble("latitude"),
                requireArguments().getDouble("longitude")
            )
            val bundle = Bundle().apply {
                putDouble("lat", requireArguments().getDouble("latitude"))
                putDouble("lng", requireArguments().getDouble("longitude"))
            }
            findNavController().navigate(R.id.action_newPlaceFragment_to_mapFragment, bundle)
        }

        return binding.root
    }
}