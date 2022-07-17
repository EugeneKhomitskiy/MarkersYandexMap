package com.example.markersyandexmap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.markersyandexmap.databinding.FragmentSinglePlaceBinding
import com.example.markersyandexmap.viewmodel.PlaceViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SinglePlaceFragment : BottomSheetDialogFragment() {

    private val placeViewModel: PlaceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSinglePlaceBinding.inflate(
            layoutInflater,
            container,false
        )

        placeViewModel.place.observe(viewLifecycleOwner) { place ->
            binding.title.text = place.title
            binding.description.text = place.description
        }

        return binding.root
    }
}